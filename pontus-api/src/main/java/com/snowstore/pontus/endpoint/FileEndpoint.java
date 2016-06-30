package com.snowstore.pontus.endpoint;

import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.ValidateCode.Scene;
import com.snowstore.pontus.domain.ValidateCode.System;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.AttachTableAlias;
import com.snowstore.pontus.repository.CustomerRepository;
import com.snowstore.pontus.service.AssigneeAttachmentService;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.MemberAttachmentService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.service.QuoteContractAttachmentService;
import com.snowstore.pontus.service.RenewalAgreementService;
import com.snowstore.pontus.service.vo.ExtFileImageVo;
import com.snowstore.pontus.vo.FileMagicNumberProcessor;
import com.snowstore.pontus.vo.FileMagicNumberProcessor.FileType;
import com.snowstore.pontus.vo.req.FileDownLoadReq;
import com.snowstore.pontus.vo.req.FileGetReq;
import com.snowstore.pontus.vo.req.FileReq;
import com.snowstore.pontus.vo.req.FileSignReq;
import com.snowstore.pontus.vo.resp.FileDownLoadResp;
import com.snowstore.pontus.vo.resp.FileGetResp;
import com.snowstore.pontus.vo.resp.FileResp;
import com.snowstore.pontus.vo.resp.FileSignResp;

/**
 * @Project: pontus-api
 * @Author zy
 * @Company:
 * @Create Time: 2016年5月5日 下午1:14:39
 */
@Path("/file")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class FileEndpoint {

	@Autowired
	private MemberAttachmentService memberAttachmentService;
	@Autowired
	private QuoteContractAttachmentService quoteContractAttachmentService;
	@Autowired
	private AssigneeAttachmentService assigneeAttachmentService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private RenewalAgreementService renewalAgreementService;

	private static final Logger LOGGER = LoggerFactory.getLogger(FileEndpoint.class);

	private static EnumSet<FileType> fileTypeList = EnumSet.of(FileType.JPG, FileType.PNG, FileType.BMP);

	/**
	 * 上传文件
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月7日 下午3:17:11
	 * @param req
	 * @return
	 */
	@POST
	@Path("/uploadFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@UserLog(remark = "文件上传")
	public FileResp uploadFile(@BeanParam FileReq req, FormDataMultiPart multiPart) {
		FileResp resp = new FileResp();

		List<FormDataBodyPart> files = multiPart.getFields("file");
		if (null != files) {
			try {
				String subjectId = null;
				String attachTableAlias = multiPart.getField("attachTableAlias").getValue();;
				String subjectType = multiPart.getField("subjectType").getValue();
				FileMagicNumberProcessor.validateType(files.get(0).getValueAs(byte[].class), fileTypeList);

				InputStream fileInputStream = files.get(0).getValueAs(InputStream.class);
				if (!Enums.AttachType.CARD_PERSON.getValue().equals(subjectType)) {// 非名片
					subjectId = multiPart.getField("subjectId").getValue();
				}
				req.setSubjectId(subjectId);
				req.setSubjectType(subjectType);
				req.setAttachTableAlias(attachTableAlias);
				req.setFile(fileInputStream);
				LOGGER.debug("上传文件 请求参数【" + req + "】");
				String customerId = customerService.getCustomerId(req.getAccessToken());
				String fileName = req.getFileDisposition().getFileName();
				fileName = new String(fileName.getBytes(Enums.Charset.ISO88591.getValue()), Enums.Charset.UTF8.getValue());
				if (Enums.AttachType.CARD_PERSON.getValue().equals(req.getSubjectType())) {// 名片
					memberAttachmentService.saveNameCard(IOUtils.toByteArray(req.getFile()), fileName, customerId);
				} else if (Enums.AttachType.TRANSFER_PROTOCOL.getValue().equals(req.getSubjectType())) {// 债权转让
					if(AttachTableAlias.QUOTECONTRACTATTACHMENT.getValue().equals(req.getAttachTableAlias())) {
						quoteContractAttachmentService.saveQuoteContractAttachment(IOUtils.toByteArray(req.getFile()), fileName, customerId, Long.valueOf(req.getSubjectId()), req.getSubjectType());
					}else if(AttachTableAlias.ASSIGNEEATTACHMENT.getValue().equals(req.getAttachTableAlias())){
						assigneeAttachmentService.saveAssigneeAttachment(IOUtils.toByteArray(req.getFile()), fileName, customerId, Long.valueOf(req.getSubjectId()), req.getSubjectType());
					}
				} else if (Enums.AttachType.GUARANTEE_PROTOCOL.getValue().equals(req.getSubjectType())) { // 担保承诺函
					if(AttachTableAlias.QUOTECONTRACTATTACHMENT.getValue().equals(req.getAttachTableAlias())) {
						quoteContractAttachmentService.saveQuoteContractAttachment(IOUtils.toByteArray(req.getFile()), fileName, customerId, Long.valueOf(req.getSubjectId()), req.getSubjectType());
					}
				} else if (Enums.AttachType.CONFIRM_PROTOCOL.getValue().equals(req.getSubjectType())) { // 收款确认函
					if(AttachTableAlias.ASSIGNEEATTACHMENT.getValue().equals(req.getAttachTableAlias())){
						assigneeAttachmentService.saveAssigneeAttachment(IOUtils.toByteArray(req.getFile()), fileName, customerId, Long.valueOf(req.getSubjectId()), req.getSubjectType());
					}
				} else if (Enums.AttachType.PAY_PROTOCOL.getValue().equals(req.getSubjectType())) { // 支付凭证
					if(AttachTableAlias.ASSIGNEEATTACHMENT.getValue().equals(req.getAttachTableAlias())){
						assigneeAttachmentService.saveAssigneeAttachment(IOUtils.toByteArray(req.getFile()), fileName, customerId, Long.valueOf(req.getSubjectId()), req.getSubjectType());
					}
				} else if (Enums.AttachType.TRANSFER_NOTIFY_PROTOCOL.getValue().equals(req.getSubjectType())) { // 债权转让通知函
					if(AttachTableAlias.ASSIGNEEATTACHMENT.getValue().equals(req.getAttachTableAlias())){
						assigneeAttachmentService.saveAssigneeAttachment(IOUtils.toByteArray(req.getFile()), fileName, customerId, Long.valueOf(req.getSubjectId()), req.getSubjectType());
					}
				}
			} catch (Exception e) {
				LOGGER.error("上传文件异常", e);
				throw new PontusServiceException("上传文件异常", e);
			}
		}
		return resp;
	}

	@POST
	@Path("/downLoadFile")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "文件下载")
	public FileDownLoadResp downLoadFile(@BeanParam FileDownLoadReq req) {
		LOGGER.debug("下载文件请求参数【" + req + "】");
		FileDownLoadResp resp = new FileDownLoadResp();
		try {
			String customerId = customerService.getCustomerId(req.getAccessToken());
			if (Enums.AttachType.CARD_PERSON.getValue().equals(req.getSubjectType())) {// 名片
				resp.setBase64FileStrList(memberAttachmentService.getNameCard(Long.valueOf(customerId), Enums.AttachType.CARD_PERSON.getValue()));
			} else if (Enums.AttachType.GUARANTEE_PROTOCOL.getValue().equals(req.getSubjectType())) { // 担保承诺函
				if(AttachTableAlias.QUOTECONTRACTATTACHMENT.getValue().equals(req.getAttachTableAlias())) {
					resp.setBase64FileStrList(quoteContractAttachmentService.getQuoteContractAttachment(Long.valueOf(customerId), req.getSubjectId(), req.getSubjectType()));
				}
			} else if (Enums.AttachType.TRANSFER_PROTOCOL.getValue().equals(req.getSubjectType())) {// 债权转让
				if(AttachTableAlias.QUOTECONTRACTATTACHMENT.getValue().equals(req.getAttachTableAlias())) {
					resp.setBase64FileStrList(quoteContractAttachmentService.getQuoteContractAttachment(Long.valueOf(customerId), req.getSubjectId(), req.getSubjectType()));
				}else if(AttachTableAlias.ASSIGNEEATTACHMENT.getValue().equals(req.getAttachTableAlias())){
					resp.setBase64FileStrList(assigneeAttachmentService.getAssigneeAttachment(Long.valueOf(customerId), req.getSubjectId(), req.getSubjectType()));
				}
			} else if (Enums.AttachType.CONFIRM_PROTOCOL.getValue().equals(req.getSubjectType())) { // 收款确认函
				if(AttachTableAlias.ASSIGNEEATTACHMENT.getValue().equals(req.getAttachTableAlias())){
					resp.setBase64FileStrList(assigneeAttachmentService.getAssigneeAttachment(Long.valueOf(customerId), req.getSubjectId(), req.getSubjectType()));
				}
			} else if (Enums.AttachType.PAY_PROTOCOL.getValue().equals(req.getSubjectType())) { // 支付凭证
				if(AttachTableAlias.ASSIGNEEATTACHMENT.getValue().equals(req.getAttachTableAlias())){
					resp.setBase64FileStrList(assigneeAttachmentService.getAssigneeAttachment(Long.valueOf(customerId), req.getSubjectId(), req.getSubjectType()));
				}
			} else if (Enums.AttachType.TRANSFER_NOTIFY_PROTOCOL.getValue().equals(req.getSubjectType())) { // 债权转让通知函
				if(AttachTableAlias.ASSIGNEEATTACHMENT.getValue().equals(req.getAttachTableAlias())){
					resp.setBase64FileStrList(assigneeAttachmentService.getAssigneeAttachment(Long.valueOf(customerId), req.getSubjectId(), req.getSubjectType()));
				}
			}
		} catch (Exception e) {
			LOGGER.error("下载文件异常", e);
			throw new PontusServiceException("下载文件异常", e);
		}
		return resp;
	}

	/**
	 * 签署协议
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月7日 下午3:16:56
	 * @param req
	 * @return
	 */
	@POST
	@Path("/sign")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "签署协议")
	public FileSignResp sign(@BeanParam FileSignReq req) {
		LOGGER.debug("签署协议请求参数【" + req + "】");
		FileSignResp resp = new FileSignResp();
		try {
			Customer customer = customerRepository.findOne(Long.valueOf(customerService.getCustomerId(req.getAccessToken())));
			// 检验验证码
			customerService.validPhoneCode(customer.getPhone(), Scene.SIGN, System.APP, req.getValidateCode());
			// 开始签章
			if (Enums.AttachType.RENEWAL_PROTOCOL.getValue().equals(req.getSubjectType())) {// 展期协议
				renewalAgreementService.sign(req.getSubjectId());
			}

		} catch (PontusServiceException e1) {
			LOGGER.error("签署协议异常", e1);
			throw e1;
		} catch (Exception e) {
			LOGGER.error("签署协议异常", e);
			throw new PontusServiceException("签署协议异常", e);
		}
		return resp;
	}

	/**
	 * 预览附件
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月7日 下午3:17:02
	 * @param req
	 * @return
	 */
	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "文件预览")
	public FileGetResp get(@BeanParam FileGetReq req) {
		LOGGER.debug("预览附件请求参数【" + req + "】");
		FileGetResp resp = new FileGetResp();
		try {
			if (Enums.AttachType.RENEWAL_PROTOCOL.getValue().equals(req.getSubjectType())) {// 展期协议
				ExtFileImageVo imageVo = quoteContractAttachmentService.getProtocol(req.getSubjectId(), req.getCurrentPage());
				BeanUtils.copyProperties(imageVo, resp);
			}
		} catch (Exception e) {
			LOGGER.error("协议预览异常", e);
			throw new PontusServiceException("协议预览异常", e);
		}
		return resp;
	}

}
