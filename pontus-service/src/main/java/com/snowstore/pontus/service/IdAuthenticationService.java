package com.snowstore.pontus.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snowstore.hera.connector.vo.tpp.D01000004_R;
import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.repository.BankInfoRepository;
import com.snowstore.pontus.repository.CustomerRepository;
import com.snowstore.pontus.service.constants.TppConstant;
import com.snowstore.pontus.service.datagram.EsbHelper;

@Service
@Transactional
public class IdAuthenticationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdAuthenticationService.class);

	@Autowired
	private BankInfoRepository bankInfoRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private EsbHelper esbHelper;

	/**
	 * 身份认证完成
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月6日 下午1:49:23
	 * @param bankInfo
	 * @param customerId
	 */
	public void complete(BankInfo bankInfoForm, String customerId) {
		BankInfo bankInfo = bankInfoRepository.findByCustomerId(Long.valueOf(customerId));
		if (null == bankInfo) {
			bankInfo = new BankInfo();
		}
		bankInfo.setName(bankInfoForm.getName());
		bankInfo.setAccount(bankInfoForm.getAccount());
		bankInfo.setSubbranch(bankInfoForm.getSubbranch());
		bankInfo.setProvince(bankInfoForm.getProvince());
		bankInfo.setCity(bankInfoForm.getCity());
		bankInfo.setDefaulted(Boolean.TRUE);

		Customer customer = customerRepository.findOne(Long.valueOf(customerId));
		bankInfo.setCustomer(customer);
		bankInfoRepository.save(bankInfo);
		customer.getBankSet().add(bankInfo);
		customer.setAccount(bankInfo.getAccount());
		customer.setResidenceProvince(bankInfo.getProvince());
		customer.setResidenceCity(bankInfo.getCity());
		customer.setCertiType(Enums.CertiType.ID_CARD.getValue());
		customerRepository.save(customer);

		// 向TPP身份认证
		D01000004_R resp = esbHelper.sendTpp01000004(customer, bankInfo);
		// //TODO测试
		// resp.setOperateCode(TppConstant.ResponseCode.SUCCESS);
		LOGGER.debug("TPP验证账户返回码【" + resp.getOperateCode() + "】，返回信息【" + resp.getMemo() + "】");
		if (TppConstant.ResponseCode.SUCCESS.equals(resp.getOperateCode())) {
			if (!ifGrown_up(customer.getIdCardAccount())) {
				throw new PontusServiceException("请使用18周岁以上的居民身份证！");
			}

			bankInfo.setStatus(Enums.BankInfoState.PASSED.getValue());
			customer.setWorkFlow(Enums.CustomerWorkFlow.PASSED.getValue());
		} else {
			LOGGER.error("TPP身份认证失败[" + resp.getMemo() + "]");
			throw new PontusServiceException("认证失败！请填写正确身份认证信息重新认证。如有疑问，请联系相应平台客服。谢谢！");
		}
	}

	public boolean ifGrown_up(String idCardAccount) {
		if (idCardAccount.length() < 18) {
			idCardAccount = idCardAccount.substring(0, 6) + "19" + idCardAccount.substring(6);
		}
		int year = Integer.parseInt(idCardAccount.substring(6, 10));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date update = null;
		try {
			update = sdf.parse(String.valueOf(year + 18) + idCardAccount.substring(10, 14));
		} catch (ParseException e) {

		}
		Date today = new Date();
		return today.after(update);
	}
}
