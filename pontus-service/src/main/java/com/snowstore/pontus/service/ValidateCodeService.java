package com.snowstore.pontus.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.ValidateCode;
import com.snowstore.pontus.domain.ValidateCode.Scene;
import com.snowstore.pontus.repository.CustomerRepository;
import com.snowstore.pontus.repository.ValidateCodeRepository;
import com.snowstore.pontus.service.datagram.EsbHelper;
import com.snowstore.pontus.service.factory.SmsTemplateFactory;
import com.snowstore.pontus.service.utils.TokenUtil;
import com.snowstore.pontus.service.vo.SendValidateCodeForm;

@Transactional
@Service
public class ValidateCodeService {

	@Autowired
	ValidateCodeRepository validateCodeRepository;

	@Autowired
	private StringRedisTemplate template;

	@Autowired
	CustomerRepository customerRepository;

	@Value("${msg.timeout:5}")
	private long timeout;

	@Autowired
	EsbHelper esbHelper;

	@Autowired
	Map<String, SmsTemplateFactory> map;

	private Map<Scene, String> templateMap;

	@PostConstruct
	public void init() {
		templateMap = new HashMap<Scene, String>();
		templateMap.put(Scene.REGISTER, "registerSmsTemplateFactory");
		templateMap.put(Scene.RETRIEVE, "retrieveSmsTemplateFactory");
		templateMap.put(Scene.SIGN, "signSmsTemplateFactory");
		templateMap.put(Scene.AUDIT, "auditTemplateFactory");

	}

	public String sendValidateCode(SendValidateCodeForm sendValidateCodeForm) {

		// 注册验证下手机号是否注册过
		if (Scene.REGISTER.getCode().equals(sendValidateCodeForm.getScene().getCode())) {
			if (null != customerRepository.findByPhone(sendValidateCodeForm.getPhone())) {
				throw new PontusServiceException("手机号已经注册过!");
			}
		}

		// 找回密码验证下手机号是否注册过
		if (Scene.RETRIEVE.getCode().equals(sendValidateCodeForm.getScene().getCode())) {
			if (null == customerRepository.findByPhone(sendValidateCodeForm.getPhone())) {
				throw new PontusServiceException("手机号未注册过!");
			}
		}

		String key = sendValidateCodeForm.getPhone() + ":" + sendValidateCodeForm.getScene().getValue() + ":" + sendValidateCodeForm.getSystem().getValue();
		if (template.hasKey(key)) {
			String code = template.opsForValue().get(key);
			String template = map.get(templateMap.get(sendValidateCodeForm.getScene())).createSmsTemplate(code);
			esbHelper.sendSmsRequest(template, sendValidateCodeForm.getPhone(), true);
			return code;

		}

		String code = TokenUtil.getRandomString(6);
		ValidateCode validateCode = new ValidateCode();
		validateCode.setCode(code);
		validateCode.setScene(sendValidateCodeForm.getScene().getValue());
		validateCode.setSystem(sendValidateCodeForm.getSystem().getValue());
		if (null != sendValidateCodeForm.getCustomerId()) {
			Customer customer = customerRepository.findOne(sendValidateCodeForm.getCustomerId());
			if (null == customer) {
				throw new PontusServiceException("手机号未注册过!");
			}
			sendValidateCodeForm.setPhone(customer.getPhone());
			validateCode.setCustomer(customer);
		}
		validateCodeRepository.save(validateCode);
		template.opsForValue().set(key, code, timeout, TimeUnit.MINUTES);
		String template = map.get(templateMap.get(sendValidateCodeForm.getScene())).createSmsTemplate(code);
		esbHelper.sendSmsRequest(template, sendValidateCodeForm.getPhone(), true);
		return code;
	}

	public Map<Scene, String> getTemplateMap() {
		return templateMap;
	}

	public String sendPhoneVCode(String phone, String preCode, Scene scene) {
		try {
			String code = preCode == null ? TokenUtil.getRandomString(6) : preCode;
			String template = map.get(templateMap.get(scene)).createSmsTemplate(code);
			esbHelper.sendSmsRequest(template, phone, true);
			return code;
		} catch (Exception e) {
			return null;
		}
	}

	public String sendSignVCodeForRegister(String phone) {
		try {
			String code = TokenUtil.getRandomString(6);
			String template = map.get(templateMap.get(Scene.SIGN)).createSmsTemplate(code);
			esbHelper.sendSmsRequest(template, phone, true);
			return code;
		} catch (Exception e) {
			return null;
		}
	}
}
