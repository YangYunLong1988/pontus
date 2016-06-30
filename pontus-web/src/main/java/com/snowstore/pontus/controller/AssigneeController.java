package com.snowstore.pontus.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.snowstore.pontus.domain.AssigneeAttachment;
import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.domain.Transfer;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.service.AssigneeAttachmentService;
import com.snowstore.pontus.service.AssigneeService;
import com.snowstore.pontus.service.BankInfoService;
import com.snowstore.pontus.service.TransferService;
import com.snowstore.pontus.service.userDetails.WebCustomDetailsService;
import com.snowstore.pontus.service.vo.AssigneeForm;
import com.snowstore.pontus.utils.ContentTypeMapper;

@Controller
@RequestMapping("/assignee")
public class AssigneeController {
	@Autowired
	private BankInfoService bankInfoService;
	@Autowired
	private WebCustomDetailsService webCustomDetailsService;
	@Autowired
	private AssigneeService assigneeService;
	@Autowired
	private TransferService transferService;
	@Autowired
	private AssigneeAttachmentService assigneeAttachmentService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AssigneeController.class);

	@RequestMapping(value = "/bank", method = RequestMethod.GET)
	public String editIdentity(String id, Model model) {
		List<BankInfo> banks = bankInfoService.findAll(webCustomDetailsService.getCustomerId());
		model.addAttribute("banks", banks);
		model.addAttribute("transferId", id);
		return "assigneeBank";
	}

	@RequestMapping(value = "/appoint", method = RequestMethod.POST)
	public String appoint(AssigneeForm assigneeVo) {
		try {
			assigneeService.assigneeSubmit(assigneeVo.getTransferId(), assigneeVo.getBankId(), webCustomDetailsService.getCustomerId());
			LOGGER.error("提交预约成功");
			return "appointSuccess";
		} catch (Exception e) {
			LOGGER.error("提交预约失败", e);
			return "redirect:/qc/center";
		}
	}

	@RequestMapping(value = "/center-detail")
	public String centerTransferInfo(Model model, Long transferId) {
		try {
			Transfer transfer = transferService.findOne(transferId);
			model.addAttribute("item", transfer);

			List<AssigneeAttachment> assigneeAttachments = null;
			if (transfer != null && transfer.getTradedAssignee() != null) {
				assigneeAttachments = assigneeAttachmentService.getObjectIdByAssigneeId(transfer.getTradedAssignee().getId());
			}

			List<String> transferIds = new ArrayList<>();
			List<String> payIds = new ArrayList<>();
			List<String> confirmIds = new ArrayList<>();
			List<String> transNotIds = new ArrayList<>();

			for (AssigneeAttachment aa : assigneeAttachments) {
				if (Enums.AttachType.TRANSFER_PROTOCOL.getValue().equals(aa.getAttachType())) {
					transferIds.add(aa.getObjectId());
				} else if (Enums.AttachType.PAY_PROTOCOL.getValue().equals(aa.getAttachType())) {
					payIds.add(aa.getObjectId());
				} else if (Enums.AttachType.CONFIRM_PROTOCOL.getValue().equals(aa.getAttachType())) {
					confirmIds.add(aa.getObjectId());
				} else if (Enums.AttachType.TRANSFER_NOTIFY_PROTOCOL.getValue().equals(aa.getAttachType())) {
					transNotIds.add(aa.getObjectId());
				}
			}
			model.addAttribute("transferIds", transferIds);
			model.addAttribute("payIds", payIds);
			model.addAttribute("confirmIds", confirmIds);
			model.addAttribute("transNotIds", transNotIds);

			return "centerAssigneeDetail";
		} catch (Exception e) {
			LOGGER.error("产看受让详情失败", e);
			return "redirect:/qc/center";
		}
	}

	@RequestMapping(value = "/get")
	public void findAttachment(@RequestParam("objId") String objId, HttpServletResponse response) {
		AssigneeAttachment attac = assigneeAttachmentService.getQuoteContractAttachmentByObjectId(objId);
		try {
			response.setContentType(ContentTypeMapper.getContentType(attac.getName()));
			response.getOutputStream().write(assigneeAttachmentService.getContent(attac));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
