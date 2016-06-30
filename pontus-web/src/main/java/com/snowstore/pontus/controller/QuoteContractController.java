package com.snowstore.pontus.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.snowstore.pontus.domain.AssigneeAttachment;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.domain.QuoteContractAttachment;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.Platform;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;
import com.snowstore.pontus.enums.Enums.RepaymentMode;
import com.snowstore.pontus.service.AssigneeAttachmentService;
import com.snowstore.pontus.service.AssigneeService;
import com.snowstore.pontus.service.ChocQuoteContractService;
import com.snowstore.pontus.service.CommonService;
import com.snowstore.pontus.service.ContractService;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.OriginContractAttachmentService;
import com.snowstore.pontus.service.OriginalContractService;
import com.snowstore.pontus.service.QuoteContractAttachmentService;
import com.snowstore.pontus.service.QuoteContractService;
import com.snowstore.pontus.service.RenewalAgreementService;
import com.snowstore.pontus.service.SpecialContractDetailsService;
import com.snowstore.pontus.service.SpecialContractService;
import com.snowstore.pontus.service.TransferService;
import com.snowstore.pontus.service.ValidateCodeService;
import com.snowstore.pontus.service.userDetails.WebCustomDetailsService;
import com.snowstore.pontus.service.vo.AddQuoteContractForm;
import com.snowstore.pontus.service.vo.AssigneeQueryForm;
import com.snowstore.pontus.service.vo.ContractQueryForm;
import com.snowstore.pontus.service.vo.ExtFileImageVo;
import com.snowstore.pontus.service.vo.SimpleTransferQueryForm;
import com.snowstore.pontus.utils.ContentTypeMapper;
import com.snowstore.pontus.web.vo.AddQuoteContractWebForm;

@Controller
@RequestMapping(value = "/qc")
public class QuoteContractController {

	private final static Logger LOGGER = LoggerFactory.getLogger(QuoteContractController.class);

	@Autowired
	private QuoteContractAttachmentService quoteContractAttachmentService;
	@Autowired
	private QuoteContractService quoteContractService;
	@Autowired
	private ChocQuoteContractService chocQuoteContractService;
	@Autowired
	private WebCustomDetailsService webCustomDetailsService;
	@Autowired
	private TransferService transferService;
	@Autowired
	private AssigneeService assigneeService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private RenewalAgreementService renewalAgreementService;
	@Autowired
	private ValidateCodeService validateCodeService;
	@Autowired
	private AssigneeAttachmentService assigneeAttachmentService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private SpecialContractDetailsService specialContractDetailsService;
	@Autowired
	private OriginalContractService originalContractService;
	@Autowired
	private SpecialContractService specialContractService;
	@Autowired
	private OriginContractAttachmentService originContractAttachmentService;

	@RequestMapping(value = "/detail")
	public String detail(Model model, Long id) {
		QuoteContract contract = quoteContractService.get(id);
		model.addAttribute("contract", contract);
		return "quoteContractDetail";
	}

	private String[] platforms = { Platform.易联天下.toString(), Platform.金鹿财行.toString(), Platform.当天财富.toString(), Platform.玖玖金服.toString(), Platform.翰典.toString(), Platform.趣逗理财.toString(), Platform.翎秀.toString(), Platform.魔环.toString(), Platform.基冉.toString(), Platform.宝驼贷.toString(), Platform.极陀.toString(), Platform.其它.toString() };
	private String[] paybackTypes = { RepaymentMode.END.getValue(), RepaymentMode.MONTH.getValue(), RepaymentMode.SEASON.getValue(), RepaymentMode.SEMESTER.getValue() };

	@RequestMapping(value = "/center")
	public String center(SimpleTransferQueryForm form, String direction, String property, @RequestParam(value = "pn", defaultValue = "0") Integer pn, @RequestParam(value = "ps", defaultValue = "20") Integer ps, Model model) {
		List<Order> orders = new ArrayList<Order>();
		if (StringUtils.isNoneBlank(direction, property)) {
			orders.add(new Order(Direction.fromString(direction), property));
		}
		orders.add(new Order(Direction.DESC, "createdDate"));
		form.setState(Enums.TransferState.VALID.getValue());
		form.setWorkFlow(Arrays.asList(Enums.TransferFlow.PENDING.getValue(), Enums.TransferFlow.TRADING.getValue()));
		model.addAttribute("transferContracts", transferService.queryTransfer(form, new PageRequest(pn, ps, new Sort(orders))));
		model.addAttribute("platforms", platforms);
		model.addAttribute("paybackTypes", paybackTypes);
		model.addAttribute("form", form);
		model.addAttribute("direction", direction);
		model.addAttribute("property", property);
		return "quoteContractCenter";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, Model model) {
		if (id != null) {
			model.addAttribute("contract", quoteContractService.get(id));
			model.addAttribute("tprottcols", quoteContractAttachmentService.queryQuoteContractAttachment(id, Enums.AttachType.TRANSFER_PROTOCOL.getValue()));
			model.addAttribute("gprottcols", quoteContractAttachmentService.queryQuoteContractAttachment(id, Enums.AttachType.GUARANTEE_PROTOCOL.getValue()));
		} else {
			model.addAttribute("contract", new QuoteContract());
			model.addAttribute("tprottcols", new ArrayList<QuoteContractAttachment>());
			model.addAttribute("gprottcols", new ArrayList<QuoteContractAttachment>());
		}
		model.addAttribute("platforms", platforms);
		model.addAttribute("paybackTypes", paybackTypes);
		return "quoteContractEdit";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String pushEdit(AddQuoteContractWebForm form, Long id, @RequestParam List<Long> attacIds, Model model) {
		AddQuoteContractForm addQuoteContractForm = new AddQuoteContractForm();
		try {
			BeanUtils.copyProperties(addQuoteContractForm, form);
			addQuoteContractForm.setCustomerId(webCustomDetailsService.getCustomerId());
			QuoteContract quoteContract = chocQuoteContractService.saveQuoteContract(addQuoteContractForm, id, attacIds);
			if (null == quoteContract) {
				return "redirect:/qc/edit";
			}
			return "redirect:/qc/edit-success";
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("添加挂牌合同异常", e);
			return "redirect:/qc/edit";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/check-posted")
	public boolean checkPosted(Long id, String contractCode, Model model) {
		List<String> workFlows = new ArrayList<String>();
		workFlows.add(Enums.QuoteContractWorkFlow.PASSED.getValue());
		workFlows.add(Enums.QuoteContractWorkFlow.PENDING.getValue());
		workFlows.add(Enums.QuoteContractWorkFlow.RENEWED.getValue());
		workFlows.add(Enums.QuoteContractWorkFlow.TRANSFERING.getValue());
		workFlows.add(Enums.QuoteContractWorkFlow.SUCCESS.getValue());
		workFlows.add(Enums.QuoteContractWorkFlow.REJECT.getValue());

		List<QuoteContract> quoteContracts = quoteContractService.findByContractCodeAndCustomerIdAndWorkFlows(webCustomDetailsService.getCustomerId(), contractCode, workFlows);
		OriginalContract originalContract = originalContractService.findByContractCode(contractCode);
		if (null == originalContract || Enums.OriginalContractState.INVALID.getValue().equals(originalContract.getStatus())) {
			return false;
		}
		if (quoteContracts.isEmpty()) {
			return true;
		} else {
			for (QuoteContract quoteContract : quoteContracts) {
				if (quoteContract.getId().equals(id) && Enums.QuoteContractWorkFlow.REJECT.getValue().equals(quoteContract.getWorkFlow())) {
					return true;
				}
				if (Enums.QuoteContracState.INVALID.getValue().equals(quoteContract.getState())) {
					return false;
				}
			}
			return false;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/match-orig", method = RequestMethod.POST)
	public WebResult matchOrig(AddQuoteContractWebForm form) {
		Customer customer = customerService.get(webCustomDetailsService.getCustomerId());
		if (contractService.findByContractCodeAndPlatformAndCertiNo(form.getContractCode(), form.getPlatform(), customer.getIdCardAccount()) == null) {
			return WebResult.newExceptionWebResult();
		} else {
			return WebResult.newSuccessWebResult();
		}
	}

	@RequestMapping(value = "/edit-success", method = RequestMethod.GET)
	public String pushEditSuccess(Model model) {
		return "quoteContractEditSuccess";
	}

	@RequestMapping(value = "/list")
	public String qcListhold(Model model, @RequestParam(value = "pn", defaultValue = "0") Integer pn, @RequestParam(value = "ps", defaultValue = "20") Integer ps) {
		List<String> flow = new ArrayList<String>();
		flow.add(QuoteContractWorkFlow.PENDING.getValue());
		flow.add(QuoteContractWorkFlow.REJECT.getValue());
		flow.add(QuoteContractWorkFlow.PASSED.getValue());
		flow.add(QuoteContractWorkFlow.RENEWED.getValue());
		flow.add(QuoteContractWorkFlow.TRANSFERING.getValue());
		flow.add(QuoteContractWorkFlow.SUCCESS.getValue());
		model.addAttribute("quoteContracts", quoteContractService.findAll(flow, webCustomDetailsService.getCustomerId(), new PageRequest(pn, ps, Direction.DESC, "createdDate")));
		return "quoteContractList";
	}

	@RequestMapping(value = "/agreement-sign", method = RequestMethod.GET)
	public String signAgreement(Long id, Model model) {
		List<String> images = new ArrayList<String>();
		boolean flag = true;
		int currentPage = 1;
		while (flag) {
			ExtFileImageVo extFileImageVo = quoteContractAttachmentService.getProtocol(id, currentPage);
			images.add(extFileImageVo.getContent());
			if (extFileImageVo.getTotalNo() <= currentPage) {
				flag = false;
			}
			currentPage++;
		}
		model.addAttribute("agreementImages", images);
		model.addAttribute("contract", quoteContractService.get(id));
		return "quoteContractSignAgreement";
	}

	@RequestMapping(value = "/agreement-sign", method = RequestMethod.POST)
	public String pushSignAgreement(Long id, String code, HttpSession session, RedirectAttributes attrs, Model model) {
		String sessionCode = (String) session.getAttribute("sign_code");
		if (StringUtils.isNotBlank(code) && StringUtils.equals(sessionCode, code)) {
			try {
				renewalAgreementService.sign(id);
				return "redirect:/qc/agreement-signsucc";
			} catch (Exception e) {
				attrs.addFlashAttribute("msg", "签约失败");
				return "redirect:/qc/agreement-sign?id=" + id;
			}
		} else {
			attrs.addFlashAttribute("msg", "验证输入错误");
			return "redirect:/qc/agreement-sign?id=" + id;
		}
	}

	@RequestMapping(value = "/agreement-signsucc")
	public String agreementSignsucc(Long id, String code, HttpSession session, Model model) {
		return "quoteContractAgreementSignsucc";
	}

	@ResponseBody
	@RequestMapping(value = "/send-signcode")
	public WebResult sendSigncode(Long id, HttpSession session) {
		try {
			QuoteContract contact = quoteContractService.get(id);
			String code = validateCodeService.sendSignVCodeForRegister(contact.getCustomer().getPhone());
			session.setAttribute("sign_code", code);
			return WebResult.newSuccessWebResult(code);
		} catch (Exception e) {
			return WebResult.newExceptionWebResult();
		}
	}

	@RequestMapping(value = "/stage-detail")
	public String stageDetail(Long id, Model model) {
		try {
			QuoteContract contract = quoteContractService.get(id);
			model.addAttribute("contract", contract);

			List<QuoteContractAttachment> quoteContractAttachments = quoteContractAttachmentService.getObjectIdByContractId(contract.getId());
			List<String> attIds = new ArrayList<>();

			for (QuoteContractAttachment qca : quoteContractAttachments) {
				if (Enums.AttachType.GUARANTEE_PROTOCOL.getValue().equals(qca.getAttachType()) || Enums.AttachType.TRANSFER_PROTOCOL.getValue().equals(qca.getAttachType())) {
					attIds.add(qca.getObjectId());
				}
			}
			model.addAttribute("attIds", attIds);
			return "quoteContractStageDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/qc/list";
		}
	}

	@RequestMapping(value = "/transferable")
	public String quoteContractTransferable(Model model, @RequestParam(value = "pn", defaultValue = "0") Integer pn, @RequestParam(value = "ps", defaultValue = "20") Integer ps) {
		ContractQueryForm form = new ContractQueryForm();
		form.setCustomerId(webCustomDetailsService.getCustomerId());
		form.setWorkFlow(Enums.QuoteContractWorkFlow.RENEWED.getValue());
		model.addAttribute("dataItems", quoteContractService.findAll(form, new PageRequest(pn, ps, Direction.DESC, "createdDate")));
		return "quoteContractTransferable";
	}

	@RequestMapping(value = "/transf-record")
	public String quoteContractTransfRecord(Model model, @RequestParam(value = "pn", defaultValue = "0") Integer pn, @RequestParam(value = "ps", defaultValue = "20") Integer ps) {
		SimpleTransferQueryForm form = new SimpleTransferQueryForm();
		form.setState(Enums.TransferState.VALID.getValue());
		model.addAttribute("dataItems", transferService.queryTransferOrder(form, webCustomDetailsService.getCustomerId(), new PageRequest(pn, ps)));
		return "quoteContractTransfRecord";
	}

	@RequestMapping(value = "/transfee-record")
	public String quoteContractTransfeeRecord(Model model, @RequestParam(value = "pn", defaultValue = "0") Integer pn, @RequestParam(value = "ps", defaultValue = "20") Integer ps) {
		AssigneeQueryForm form = new AssigneeQueryForm();
		model.addAttribute("dataItems", assigneeService.findAllOrder(form, webCustomDetailsService.getCustomerId(), new PageRequest(pn, ps)));
		return "quoteContractTransfeeRecord";
	}

	@RequestMapping(value = "/transf", method = RequestMethod.GET)
	public String quoteContractTransfEdit(Long id, Model model) {
		QuoteContract quoteContract = quoteContractService.get(id);
		model.addAttribute("contract", quoteContract);
		model.addAttribute("extValue", commonService.extValue(quoteContract.getOriginalContract().getPrincipal().add(quoteContract.getOriginalContract().getUnPayedInterest())));
		return "quoteContractTransfEdit";
	}

	@RequestMapping(value = "/trans-buyback", method = RequestMethod.GET)
	public String quoteContractTransBuyback(Model model, @RequestParam(value = "pn", defaultValue = "0") Integer pn, @RequestParam(value = "ps", defaultValue = "20") Integer ps) {
		List<String> flow = new ArrayList<String>();
		List<Long> contractIds = originalContractService.findByCustomerId(webCustomDetailsService.getCustomerId());
		model.addAttribute("contracts", specialContractService.findAll(contractIds, new PageRequest(pn, ps, Direction.DESC, "createdDate")));
		return "contractTransBuyback";
	}

	@RequestMapping(value = "/buyback-detail", method = RequestMethod.GET)
	public String quoteContractBuyBackDetail(Long id, Model model) {
		OriginalContract originalContract = originalContractService.get(id);
		model.addAttribute("contract", originalContract);

		List<String> guaranteeIds = new ArrayList<>();
		List<String> transferIds = new ArrayList<>();
		List<String> images = new ArrayList<String>();

		if (originalContract.getQuoteContract() != null) {

			this.getImages(originalContract.getQuoteContract().getId(), images);

			List<QuoteContractAttachment> quoteContractAttachments = quoteContractAttachmentService.getObjectIdByContractId(originalContract.getQuoteContract().getId());
			for (QuoteContractAttachment qca : quoteContractAttachments) {
				if (Enums.AttachType.GUARANTEE_PROTOCOL.getValue().equals(qca.getAttachType())) {
					guaranteeIds.add(qca.getObjectId());
				} else if (Enums.AttachType.TRANSFER_PROTOCOL.getValue().equals(qca.getAttachType())) {
					transferIds.add(qca.getObjectId());
				}
			}
		}

		model.addAttribute("guaranteeIds", guaranteeIds);
		model.addAttribute("transferIds", transferIds);
		model.addAttribute("images", images);

		model.addAttribute("specialDetails", specialContractDetailsService.findByContractId(id));
		return "contractBuybackDetail";
	}

	@RequestMapping(value = "/transf", method = RequestMethod.POST)
	public String pushQuoteContractTransf(Long id, BigDecimal rate, RedirectAttributes attr, Model model) {
		try {
			transferService.transferSubmit(id, rate);
			return "redirect:/qc/transf-success";
		} catch (Exception e) {
			LOGGER.error("合同转让失败 id:{} | rate:{}", id, rate, e);
			return "redirect:/qc/transf-fail";
		}
	}

	@RequestMapping(value = "/cancel-transf")
	public String cancelTransf(Long id, RedirectAttributes attr, Model model) {
		try {
			transferService.transferCancel(id);
		} catch (Exception e) {
			LOGGER.error("取消合同异常 id:{} ", id, e);
		}
		return "redirect:/qc/transf-record";
	}

	@RequestMapping(value = "/transf-success")
	public String quoteContractTransfSuccess(Model model) {
		model.addAttribute("msg", "转让成功");
		return "quoteContractTransfResult";
	}

	@RequestMapping(value = "/transf-fail")
	public String quoteContractTransfFail(Model model) {
		model.addAttribute("msg", "转让失败");
		return "quoteContractTransfResult";
	}

	@ResponseBody
	@RequestMapping(value = "/upload")
	public WebResult upload(String t, @RequestParam("file") MultipartFile file) {
		try {
			if (!Enums.AttachType.GUARANTEE_PROTOCOL.getValue().equals(t) && !Enums.AttachType.TRANSFER_PROTOCOL.getValue().equals(t)) {
				return WebResult.newExceptionWebResult();
			}
			if (file.getBytes().length > 5242880) {
				return WebResult.newExceptionWebResult("文件大小超过限制（5M）");
			}
			QuoteContractAttachment quoteContractAttachment = quoteContractAttachmentService.saveQuoteContractAttachment(file.getBytes(), file.getOriginalFilename(), t);
			return WebResult.newSuccessWebResult(quoteContractAttachment.getId());
		} catch (Exception e) {
			return WebResult.newExceptionWebResult();
		}
	}

	@RequestMapping(value = "/attac-thumbnail")
	public void attacThumbnail(Long id, HttpServletResponse response) {
		// QuoteContractAttachment quoteContractAttachment =
		// quoteContractAttachmentService.get(id);
		QuoteContractAttachment attac = quoteContractAttachmentService.get(id);
		try {
			response.setContentType(ContentTypeMapper.getContentType(attac.getName()));
			response.getOutputStream().write(quoteContractAttachmentService.getContent(attac));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/transfee-attac-thumbnail")
	public void transfeeAttacThumbnail(Long id, HttpServletResponse response) {
		// QuoteContractAttachment quoteContractAttachment =
		// quoteContractAttachmentService.get(id);
		AssigneeAttachment attac = assigneeAttachmentService.get(id);
		try {
			response.setContentType(ContentTypeMapper.getContentType(attac.getName()));
			response.getOutputStream().write(assigneeAttachmentService.getContent(attac));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/calculate-transf-price")
	public WebResult transferPrice(Long id, BigDecimal rate) {
		try {
			BigDecimal price = transferService.getTransferPrice(quoteContractService.get(id), rate);
			return WebResult.newSuccessWebResult(price);
		} catch (Exception e) {
			return WebResult.newExceptionWebResult();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/transfee-upload")
	public WebResult transfeeUpload(String t, @RequestParam("file") MultipartFile file) {
		try {
			if (!Enums.AttachType.TRANSFER_PROTOCOL.getValue().equals(t) && !Enums.AttachType.CONFIRM_PROTOCOL.getValue().equals(t) && !Enums.AttachType.PAY_PROTOCOL.getValue().equals(t) && !Enums.AttachType.TRANSFER_NOTIFY_PROTOCOL.getValue().equals(t)) {
				return WebResult.newExceptionWebResult();
			}
			if (file.getBytes().length > 5242880) {
				return WebResult.newExceptionWebResult("文件大小超过限制（5M）");
			}

			AssigneeAttachment assigneeAttachment = assigneeAttachmentService.saveAssigneeAttachment(file.getBytes(), file.getOriginalFilename(), t);
			return WebResult.newSuccessWebResult(assigneeAttachment.getId());
		} catch (Exception e) {
			return WebResult.newExceptionWebResult();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/transfee-push-attac")
	public WebResult transfeePushAttac(Long id, @RequestParam List<Long> attacIds) {
		try {
			assigneeAttachmentService.bind(id, attacIds);
			return WebResult.newSuccessWebResult();
		} catch (Exception e) {
			return WebResult.newExceptionWebResult();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/transfee-poll-attac")
	public WebResult transfeePollAttac(Long id) {
		try {
			Map<String, List<Long>> content = new HashMap<>();
			List<AssigneeAttachment> transfer = assigneeAttachmentService.getAssigneeAttachment(id, Enums.AttachType.TRANSFER_PROTOCOL.getValue());
			List<AssigneeAttachment> confirm = assigneeAttachmentService.getAssigneeAttachment(id, Enums.AttachType.CONFIRM_PROTOCOL.getValue());
			List<AssigneeAttachment> pay = assigneeAttachmentService.getAssigneeAttachment(id, Enums.AttachType.PAY_PROTOCOL.getValue());
			List<AssigneeAttachment> transferNotify = assigneeAttachmentService.getAssigneeAttachment(id, Enums.AttachType.TRANSFER_NOTIFY_PROTOCOL.getValue());
			content.put("transfer", toListLong(transfer));
			content.put("confirm", toListLong(confirm));
			content.put("pay", toListLong(pay));
			content.put("transferNotify", toListLong(transferNotify));
			return WebResult.newSuccessWebResult(content);
		} catch (Exception e) {
			return WebResult.newExceptionWebResult();
		}
	}

	private List<Long> toListLong(List<AssigneeAttachment> attacs) {
		List<Long> ids = new ArrayList<Long>();
		for (AssigneeAttachment attac : attacs) {
			ids.add(attac.getId());
		}
		return ids;
	}

	private void getImages(Long id, List<String> images) {
		boolean flag = true;
		int currentPage = 1;
		while (flag) {
			ExtFileImageVo extFileImageVo = quoteContractAttachmentService.getProtocol(id, currentPage);
			images.add(extFileImageVo.getContent());
			if (extFileImageVo.getTotalNo() <= currentPage) {
				flag = false;
			}
			currentPage++;
		}
	}
}
