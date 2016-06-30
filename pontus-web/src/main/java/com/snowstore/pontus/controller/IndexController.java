package com.snowstore.pontus.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.ValidateCode.Scene;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.service.BankInfoService;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.MemberAttachmentService;
import com.snowstore.pontus.service.TransferService;
import com.snowstore.pontus.service.ValidateCodeService;
import com.snowstore.pontus.service.userDetails.WebCustomDetailsService;
import com.snowstore.pontus.service.userDetails.WebUserDetailsImpl;
import com.snowstore.pontus.service.vo.CustomerRegisterFullFrom;
import com.snowstore.pontus.service.vo.SimpleTransferQueryForm;
import com.snowstore.pontus.web.annotation.AvoidDuplicateSubmission;
import com.snowstore.pontus.web.annotation.TokenAction;
import com.snowstore.pontus.web.vo.IdentifyAuthenticationForm;
import com.snowstore.pontus.web.vo.PhoneCode;

@Controller
public class IndexController {
	@Autowired
	private Validator validator;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ValidateCodeService validateCodeService;
	@Autowired
	private WebCustomDetailsService webCustomDetailsService;
	@Autowired
	private BankInfoService bankInfoService;
	@Autowired
	private TransferService transferService;
	@Autowired
	private MemberAttachmentService memberAttachmentService;
	@Autowired(required = false)
	private PasswordEncoder bcryptEncoder;

	@RequestMapping("/")
	public String index(Model model) {
		SimpleTransferQueryForm form = new SimpleTransferQueryForm();
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "createdDate"));
		form.setState(Enums.TransferState.VALID.getValue());
		form.setWorkFlow(Arrays.asList(Enums.TransferFlow.PENDING.getValue(), Enums.TransferFlow.TRADING.getValue()));
		model.addAttribute("transferContracts", transferService.queryTransfer(form, new PageRequest(0, 3, new Sort(orders))));
		return "index";
	}

	@RequestMapping("/trade-center")
	public String tradeCenter(Model model) {
		return "tradeCenter";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@CookieValue(value = "username", defaultValue = "") String username, Model model) {
		if (StringUtils.isNotBlank(username)) {
			model.addAttribute("username", username);
		}
		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return "register";
	}

	@AvoidDuplicateSubmission(tokenAction = TokenAction.PREPARATION)
	@RequestMapping(value = "/regist-id-auth")
	public String registIdAuthentication(@Valid CustomerRegisterFullFrom registerFullForm, BindingResult result, HttpSession session) {
		PhoneCode phoneVCode = (PhoneCode) session.getAttribute(registerFullForm.getPhone());

		validator.validate(registerFullForm, result); // 被验证参数与错误类型绑定
		if (result.hasErrors()) {
			return "register";
		}

		if (!registerFullForm.getIsAgree() && !registerFullForm.getIsLegalPWD()) { // 未同意协议条款，或两次密码不一致
			return "register";
		}

		if (phoneVCode == null || !phoneVCode.getCode().equals(registerFullForm.getCode())) { // 验证手机验证码
			return "register";
		}

		try {
			customerService.regist(registerFullForm, false);
			Customer customer = customerService.findByPhone(registerFullForm.getPhone());
			WebUserDetailsImpl webuser = new WebUserDetailsImpl(customer.getId(), customer.getPhone(), customer.getPassword());
			this.quickLogin(webuser); // 注册后，直接登录
		} catch (Exception e) {
			return "register";
		}

		return "registIdAuthentication";
	}

	@AvoidDuplicateSubmission(tokenAction = TokenAction.SUBMIT, dupUrl = "/")
	@RequestMapping(value = "/submit-auth", method = RequestMethod.POST)
	@ResponseBody
	public String registIdSuccess(@Valid IdentifyAuthenticationForm authenForm, BindingResult result) {
		validator.validate(authenForm, result); // 被验证参数与错误类型绑定
		if (result.hasErrors()) {
			return "false";
		}

		try {
			Long userId = webCustomDetailsService.getCustomerId();

			List<Customer> custs = customerService.findByIdCard(authenForm.getIdCardAccount());
			if (custs != null && custs.size() > 0) {
				return "false";
			}

			Customer customer = customerService.get(userId);
			authenForm.copyAuthenFormToCustomer(customer);
			customer.setCertiType("身份证");

			BankInfo bank = new BankInfo();
			authenForm.copyAuthenFormToBankInfo(bank);
			bank.setCustomer(customer);
			bank.setDefaulted(Boolean.TRUE);

			customerService.checkCustomerAndBankInfo(customer, bank);

			customerService.save(customer);
			processSaveAttachment(userId, authenForm);
			bankInfoService.save(bank);
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}

		return "true";
	}

	@RequestMapping(value = "/identify-success")
	public String registIdSuccess2() {
		return "registIdSuccess";
	}

	@RequestMapping(value = "/forget-pwd-one")
	public String forgetPwdOne() {
		return "forgetPwdOne";
	}

	@RequestMapping(value = "/forget-pwd-two")
	public String forgetPwdTwo(String phone, String imgCode, HttpSession session, Model model) {
		String expected = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (imgCode == null || !imgCode.equals(expected)) {
			return "forgetPwdOne";
		}

		Customer customer = customerService.findByPhone(phone);
		if (customer == null) {
			return "forgetPwdOne";
		}

		model.addAttribute("phone", phone);

		return "forgetPwdTwo";
	}

	@RequestMapping(value = "/forget-pwd-three")
	public String forgetPwdThree(@RequestParam("phone") String phone, @RequestParam("code") String code, HttpSession session, Model model) {
		PhoneCode phoneVCode = (PhoneCode) session.getAttribute(phone);
		if (phoneVCode == null || !phoneVCode.getCode().equals(code)) { // 验证手机验证码
			return "forgetPwdTwo";
		}
		model.addAttribute("phone", phone);
		return "forgetPwdThree";
	}

	@RequestMapping(value = "/forget-pwd-four")
	public String forgetPwdFour(@RequestParam("phone") String phone, String password, String password2) {
		if (password == null || !password.equals(password2)) {
			return "forgetPwdThree";
		}

		try {
			Customer customer = customerService.findByPhone(phone);
			if (customer == null) {
				return "forgetPwdOne";
			}
			customer.setPassword(bcryptEncoder.encode(password));
			customerService.save(customer);
		} catch (Exception e) {
			e.printStackTrace();
			return "forgetPwdOne";
		}

		return "forgetPwdFour";
	}

	@ResponseBody
	@RequestMapping(value = "/check-img-code")
	public boolean checkImgCode(@RequestParam("code") String code, HttpServletRequest request) {
		String imgCode = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (imgCode != null && imgCode.equals(code)) {
			return true;
		}
		return false;
	}

	@ResponseBody
	@RequestMapping(value = "/check-unique-phone")
	public boolean checkUniquePhone(@RequestParam("phone") String phone) {
		Customer customer = customerService.findByPhone(phone);
		if (customer == null) {
			return true;
		}
		return false;
	}

	@ResponseBody
	@RequestMapping(value = "/check-register-phone")
	public boolean checkRegisterPhone(@RequestParam("phone") String phone) {
		Customer customer = customerService.findByPhone(phone);
		if (customer != null) {
			return true;
		}
		return false;
	}

	@ResponseBody
	@RequestMapping(value = "/check-phone-vcode")
	public boolean checkPhoneVCode(@RequestParam("phone") String phone, @RequestParam("code") String code, HttpSession session) {
		PhoneCode phoneVCode = (PhoneCode) session.getAttribute(phone);
		if (phoneVCode == null || !phoneVCode.getCode().equals(code)) {
			return false;
		}
		return true;
	}

	@ResponseBody
	@RequestMapping(value = "/send-phone-code", method = RequestMethod.POST)
	public Object sendPhoneCode(@RequestParam("phone") String phone, @RequestParam("imgCode") String imgCode, HttpSession session) {
		String expected = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

		if (imgCode == null || !imgCode.equals(expected)) {
			return "{\"code\":1,\"attachment\":\"未能发送验证码！\"}";
		}
		return sendVCode(phone, session, Scene.REGISTER);
	}

	private String sendVCode(String phone, HttpSession session, Scene scene) {
		String code = null;
		PhoneCode phoneCode = (PhoneCode) session.getAttribute(phone);
		if (phoneCode != null && System.currentTimeMillis() - phoneCode.getTime() <= 30 * 60 * 60 * 1000) {
			code = validateCodeService.sendPhoneVCode(phone, phoneCode.getCode(), scene);
		} else {
			code = validateCodeService.sendPhoneVCode(phone, null, scene);
		}
		if (code != null) {
			if (phoneCode == null) {
				phoneCode = new PhoneCode();
				phoneCode.setCode(code);
				phoneCode.setTime(System.currentTimeMillis());
				session.setAttribute(phone, phoneCode);
			}
			return "{\"code\":0,\"attachment\":\"已发送验证码！\"}";
		} else {
			return "{\"code\":1,\"attachment\":\"未能发送验证码！\"}";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/send-phone-code2", method = RequestMethod.POST)
	public Object sendPhoneCodeToChangePWD(@RequestParam("phone") String phone, HttpSession session) {
		return sendVCode(phone, session, Scene.RETRIEVE);
	}

	@RequestMapping("/not-found")
	public String notFound() {
		return "error/notFound";
	}

	private void quickLogin(WebUserDetailsImpl user) {
		UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	}

	private void processSaveAttachment(Long userId, IdentifyAuthenticationForm form) {
		Long imgId;
		try {
			if (form.getIdCardPositive() != null && form.getIdCardPositive() != "") {
				imgId = Long.parseLong(form.getIdCardPositive());
				memberAttachmentService.bind(imgId, userId, Enums.AttachType.IDCARD_POSITIVE.getValue());
			}
			if (form.getIdCardOpposite() != null && form.getIdCardOpposite() != "") {
				imgId = Long.parseLong(form.getIdCardOpposite());
				memberAttachmentService.bind(imgId, userId, Enums.AttachType.IDCARD_OPPOSITE.getValue());
			}
			if (form.getPersonalCard() != null && form.getPersonalCard() != "") {
				imgId = Long.parseLong(form.getPersonalCard());
				memberAttachmentService.bind(imgId, userId, Enums.AttachType.CARD_PERSON.getValue());
			}
		} catch (NumberFormatException e) {
			return;
		}
	}
}
