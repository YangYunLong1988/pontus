package com.snowstore.pontus.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.MemberAttachment;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.service.AssigneeService;
import com.snowstore.pontus.service.BankInfoService;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.MemberAttachmentService;
import com.snowstore.pontus.service.QuoteContractService;
import com.snowstore.pontus.service.TransferService;
import com.snowstore.pontus.service.userDetails.WebCustomDetailsService;
import com.snowstore.pontus.service.vo.IdentityForm;
import com.snowstore.pontus.utils.ContentTypeMapper;
import com.snowstore.pontus.web.annotation.AvoidDuplicateSubmission;
import com.snowstore.pontus.web.annotation.TokenAction;
import com.snowstore.pontus.web.vo.IdentifyAuthenticationForm;

@Controller
@RequestMapping("/cust")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private Validator validator;
	@Autowired
	private WebCustomDetailsService webCustomDetailsService;
	@Autowired
	private BankInfoService bankInfoService;
	@Autowired
	private MemberAttachmentService memberAttachmentService;
	@Autowired
	private QuoteContractService quoteContractService;
	@Autowired
	private TransferService transferService;
	@Autowired
	private AssigneeService assigneeService;

	@RequestMapping(value = "/edit-identity", method = RequestMethod.GET)
	public String editIdentity() {
		return "editIdentity";
	}

	@RequestMapping(value = "/edit-identity", method = RequestMethod.POST)
	public String pushIdentity(IdentityForm form) {
		customerService.identityAuthentication(null, form);
		return "editIdentity";
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.GET)
	public String changePassword(String oldPassword, String newPassword, String aginNewPassword) {
		return "changePassword";
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public String pushPassword(String oldPassword, String newPassword, String againNewPassword, RedirectAttributes attr) {
		Long customerId = webCustomDetailsService.getCustomerId();
		if (!customerService.authenticationCustomer(customerId, oldPassword)) {
			attr.addFlashAttribute("msg", "原密码输入错误");
		} else {
			customerService.changePassword(customerId, newPassword);
			attr.addFlashAttribute("msg", "密码已修改");
		}
		return "redirect:/cust/change-password";
	}

	@AvoidDuplicateSubmission(tokenAction = TokenAction.PREPARATION)
	@RequestMapping(value = "/identify-authentication")
	public String identifyAuthentication(Model model) {
		try {

			Long userId = webCustomDetailsService.getCustomerId();
			Customer customer = customerService.get(userId);
			if (Enums.CustomerWorkFlow.PASSED.getValue().equals(customer.getWorkFlow())) {
				BankInfo bank = customer.getBankInfo();
				model.addAttribute("customer", customer);
				model.addAttribute("bank", bank);

				model.addAttribute("idPositive", memberAttachmentService.findImgIdByUserIdAndWorkFlow(userId, Enums.AttachType.IDCARD_POSITIVE.getValue()));
				model.addAttribute("idOpposite", memberAttachmentService.findImgIdByUserIdAndWorkFlow(userId, Enums.AttachType.IDCARD_OPPOSITE.getValue()));
				model.addAttribute("idCardPerson", memberAttachmentService.findImgIdByUserIdAndWorkFlow(userId, Enums.AttachType.CARD_PERSON.getValue()));
				return "identifyAuthentication2";
			} else {
				return "identifyAuthentication";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/";
		}
	}

	@AvoidDuplicateSubmission(tokenAction = TokenAction.SUBMIT, dupUrl = "/")
	@RequestMapping(value = "/submit-auth", method = RequestMethod.POST)
	@ResponseBody
	public String identifySuccess(@Valid IdentifyAuthenticationForm authenForm, BindingResult result, Model model) {
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
		return "identifySuccess";
	}

	@RequestMapping(value = "/my", method = RequestMethod.GET)
	public String customerAccount(Model model) {
		try {
			Long userId = webCustomDetailsService.getCustomerId();
			Customer customer = customerService.get(userId);
			BankInfo bank = customer.getBankInfo();
			model.addAttribute("customer", customer);
			model.addAttribute("bank", bank);

			Long quoteNums = quoteContractService.countAll(userId);
			model.addAttribute("quoteNums", quoteNums);

			Long canTransferNums = quoteContractService.countByCustomerandWorkFlow(userId, Enums.QuoteContractWorkFlow.RENEWED.getValue());
			model.addAttribute("canTransferNums", canTransferNums);

			Long transferNums = transferService.countTransferRecord(userId);
			model.addAttribute("transferNums", transferNums);

			Long transfeeNums = assigneeService.countTransfeeRecord(userId);
			model.addAttribute("transfeeNums", transfeeNums);

			return "customerIndex";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/";
		}

	}

	@ResponseBody
	@RequestMapping(value = "/check-unique-idcard")
	public boolean checkUniqueIdcard(@RequestParam("idcard") String idcard) {
		List<Customer> custs = customerService.findByIdCard(idcard);
		if (custs != null && custs.size() > 0) {
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/myinfo", method = RequestMethod.GET)
	public String personInfo(Model model) {
		Customer customer = customerService.get(webCustomDetailsService.getCustomerId());
		model.addAttribute("user", customer);
		return "personInfo";
	}

	@RequestMapping(value = "/change-email", method = RequestMethod.POST)
	public String pushPassword(String email, Model model) {
		Customer customer = customerService.get(webCustomDetailsService.getCustomerId());
		customer = customerService.changeEmail(email, customer);
		return "redirect:/cust/myinfo";
	}

	@RequestMapping(value = "/change-adress", method = RequestMethod.POST)
	public String pushAdress(String province, String city, Model model) {
		Customer customer = customerService.get(webCustomDetailsService.getCustomerId());
		customer = customerService.changeAdress(province, city, customer);
		return "redirect:/cust/myinfo";
	}

	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public WebResult upload(String type, @RequestParam("file") MultipartFile file) {
		try {
			if (!Enums.AttachType.IDCARD_POSITIVE.getValue().equals(type) && !Enums.AttachType.IDCARD_OPPOSITE.getValue().equals(type) && !Enums.AttachType.CARD_PERSON.getValue().equals(type)) {
				return WebResult.newExceptionWebResult();
			}
			MemberAttachment memberAttachment = memberAttachmentService.saveMemberAttachment(file.getBytes(), file.getOriginalFilename(), type);
			return WebResult.newSuccessWebResult(memberAttachment.getId());
		} catch (Exception e) {
			return WebResult.newExceptionWebResult();
		}
	}

	@RequestMapping(value = "/thumbnail")
	public void attacThumbnail(Long id, HttpServletResponse response) {
		MemberAttachment attac = memberAttachmentService.get(id);
		try {
			response.setContentType(ContentTypeMapper.getType(attac.getName()));
			response.getOutputStream().write(memberAttachmentService.getContent(attac));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/mybankcard", method = RequestMethod.GET)
	public String myBankList(Model model) {
		List<BankInfo> banks = bankInfoService.findAll(webCustomDetailsService.getCustomerId());
		model.addAttribute("banks", banks);
		return "mybank";
	}

	@RequestMapping(value = "/addbank", method = RequestMethod.GET)
	public String addbankcard(Model model) {
		return "addBank";
	}

	@RequestMapping(value = "/addbank", method = RequestMethod.POST)
	public String addbankcard(Model model, BankInfo bank) {
		bank.setAccount(bank.getAccount().replaceAll(" ", ""));
		Customer customer = customerService.get(webCustomDetailsService.getCustomerId());
		bank.setCustomer(customer);
		bank.setStatus(Enums.BankInfoState.PASSED.getValue());
		try {
			bankInfoService.addBankInfo(bank, customer.getId(), customer.getIdCardName());
			model.addAttribute("msg", "添加成功");
		} catch (Exception e) {
			model.addAttribute("msg", "添加失败，请检查您的银行卡信息");
		}
		return "addBankSuccess";
	}

	@RequestMapping(value = "/defaultbank-change")
	public String changeDefaultBank(Model model, Long id) {
		List<BankInfo> banks = bankInfoService.findAll(webCustomDetailsService.getCustomerId());
		for (BankInfo bank : banks) {
			if (bank.getId().equals(id)) {
				bank.setDefaulted(true);
			} else {
				bank.setDefaulted(false);
			}
			bankInfoService.save(bank);
		}
		return "redirect:/cust/mybankcard";
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
