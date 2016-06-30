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
import org.springframework.web.bind.annotation.RequestParam;

import com.snowstore.pontus.domain.Assignee;
import com.snowstore.pontus.domain.QuoteContractAttachment;
import com.snowstore.pontus.domain.Transfer;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.service.AssigneeService;
import com.snowstore.pontus.service.QuoteContractAttachmentService;
import com.snowstore.pontus.service.TransferService;
import com.snowstore.pontus.service.userDetails.WebCustomDetailsService;
import com.snowstore.pontus.service.vo.ExtFileImageVo;
import com.snowstore.pontus.utils.ContentTypeMapper;

@Controller
@RequestMapping(value = "/transfer")
public class TransferController {
	@Autowired
	private TransferService transferService;
	@Autowired
	private QuoteContractAttachmentService quoteContractAttachmentService;
	@Autowired
	private AssigneeService assigneeService;
	@Autowired
	private WebCustomDetailsService webCustomDetailsService;

	private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

	@RequestMapping(value = "/detail")
	public String transferInfo(Model model, Long id) {
		Transfer transfer = transferService.findOne(id);
		model.addAttribute("item", transfer);

		List<Assignee> asgn = assigneeService.findByTransferAndCustommer(transfer.getId(), webCustomDetailsService.getCustomerId());
		model.addAttribute("asgn", !asgn.isEmpty() ? "" : null);

		getImageContent(model, transfer);

		return "transferInfo";
	}

	@RequestMapping(value = "/get")
	public void findAttachment(@RequestParam("objId") String objId, HttpServletResponse response) {
		QuoteContractAttachment attac = quoteContractAttachmentService.getQuoteContractAttachmentByObjectId(objId);
		try {
			response.setContentType(ContentTypeMapper.getContentType(attac.getName()));
			response.getOutputStream().write(quoteContractAttachmentService.getContent(attac));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/center-detail")
	public String centerTransferInfo(Model model, Long transferId) {
		try {
			Transfer transfer = transferService.findOne(transferId);
			model.addAttribute("item", transfer);

			getImageContent(model, transfer);

			return "centerTransferDetail";
		} catch (Exception e) {
			LOGGER.error("提交预约失败", e);
			return "redirect:/qc/center";
		}
	}

	private void getImageContent(Model model, Transfer transfer) {
		List<QuoteContractAttachment> quoteContractAttachments = null;
		List<String> images = new ArrayList<String>();
		if (transfer != null && transfer.getQuoteContract() != null) {
			quoteContractAttachments = quoteContractAttachmentService.getObjectIdByContractId(transfer.getQuoteContract().getId());
			this.getImages(transfer.getQuoteContract().getId(), images);
		}

		List<String> guaranteeIds = new ArrayList<>();
		List<String> transferIds = new ArrayList<>();

		for (QuoteContractAttachment qca : quoteContractAttachments) {
			if (Enums.AttachType.GUARANTEE_PROTOCOL.getValue().equals(qca.getAttachType())) {
				guaranteeIds.add(qca.getObjectId());
			} else if (Enums.AttachType.TRANSFER_PROTOCOL.getValue().equals(qca.getAttachType())) {
				transferIds.add(qca.getObjectId());
			}
		}

		model.addAttribute("guaranteeIds", guaranteeIds);
		model.addAttribute("transferIds", transferIds);
		model.addAttribute("images", images);
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
