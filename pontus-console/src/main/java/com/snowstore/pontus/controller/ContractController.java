package com.snowstore.pontus.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;
import com.snowstore.pontus.service.AdminUserService;
import com.snowstore.pontus.service.ContractService;
import com.snowstore.pontus.service.GridfsService;
import com.snowstore.pontus.service.OriginContractAttachmentService;
import com.snowstore.pontus.service.QuoteContractAttachmentService;
import com.snowstore.pontus.service.QuoteContractService;
import com.snowstore.pontus.service.vo.ContractQueryForm;
import com.snowstore.pontus.service.vo.DataTableRequest;
import com.snowstore.pontus.service.vo.DataTables;
import com.snowstore.pontus.service.vo.Result;

@Controller
@RequestMapping("/contract")
public class ContractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContractController.class);
	@Autowired
	private ContractService contractService;

	@Autowired
	private QuoteContractService quoteContractService;

	@Autowired
	private QuoteContractAttachmentService quoteContractAttachmentService;

	@Autowired
	private OriginContractAttachmentService originContractAttachmentService;

	@Autowired
	private GridfsService gridfsService;

	@Autowired
	private AdminUserService adminUserService;

	/**
	 * 原始合同页面
	 * 
	 * @return
	 */
	@RequestMapping("/page")
	@UserLog(remark = "业务管理/原始合同管理：返回原始合同页面")
	public String page(Model model) {
		model.addAttribute("platforms", adminUserService.getUserPlatforms());
		return "contract_list";
	}

	/**
	 * 原始合同数据
	 * 
	 * @param contractQueryForm
	 * @param dataTableRequest
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	@UserLog(remark = "原始合同数据列表")
	public DataTables<OriginalContract> list(ContractQueryForm contractQueryForm, DataTableRequest dataTableRequest) {
		Pageable pageable = new PageRequest(dataTableRequest.getStart() == 0 ? 0 : dataTableRequest.getStart() / dataTableRequest.getLength(), dataTableRequest.getLength(), new Sort(new Order(Direction.DESC, "id")));
		Page<OriginalContract> page = contractService.findAll(contractService.buildSpecification(contractQueryForm), pageable, contractQueryForm);
		DataTables<OriginalContract> result = new DataTables<OriginalContract>(page);
		result.setDraw(dataTableRequest.getDraw());
		return result;
	}

	/**
	 * 原始合同详情
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/details/{id}")
	@UserLog(remark = "原始合同管理-操作-查看：原始合同详情")
	public String detail(@PathVariable("id") Long id, Model model) {
		OriginalContract originalContract = contractService.get(id);
		if (!adminUserService.getUserPlatforms().contains(originalContract.getPlatform())) {
			return "500_ERROR";
		}
		model.addAttribute("contract", originalContract);
		model.addAttribute("attachments", originContractAttachmentService.loadQuoteContractAttachment(originalContract));
		return "contract_details";
	}

	/**
	 * 挂牌合同
	 * 
	 * @return
	 */
	@RequestMapping("/reservation")
	@UserLog(remark = "挂牌审核管理：返回挂牌合同审核列表页面")
	public String reservation(Model model) {
		model.addAttribute("flows", QuoteContractWorkFlow.values());
		return "reservation_list";
	}

	/**
	 * 挂牌合同数据
	 * 
	 * @param contractQueryForm
	 * @param dataTableRequest
	 * @return
	 */
	@RequestMapping("/reservation/list")
	@ResponseBody
	@UserLog(remark = "挂牌合同审核列表")
	public DataTables<QuoteContract> reservationList(ContractQueryForm contractQueryForm, DataTableRequest dataTableRequest) {
		Pageable pageable = new PageRequest(dataTableRequest.getStart() == 0 ? 0 : dataTableRequest.getStart() / dataTableRequest.getLength(), dataTableRequest.getLength(), new Sort(new Order(Direction.DESC, "createdDate")));
		Page<QuoteContract> page = quoteContractService.findAll(quoteContractService.buildSpecification(contractQueryForm), pageable);
		DataTables<QuoteContract> result = new DataTables<QuoteContract>(page);
		result.setDraw(dataTableRequest.getDraw());
		return result;
	}

	/**
	 * 挂牌合同详情
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/reservation/details/{id}")
	@UserLog(remark = "挂牌审核管理-操作-审核：挂牌合同详情")
	public String reservationDetails(@PathVariable("id") Long id, Model model) {
		QuoteContract quoteContract = quoteContractService.get(id);
		model.addAttribute("quoteContract", quoteContract);
		OriginalContract contract = contractService.findByContractCodeAndPlatformAndCertiNo(quoteContract.getContractCode(), quoteContract.getPlatform(), quoteContract.getCustomer().getIdCardAccount());
		if (null == contract) {
			contract = new OriginalContract();
		}
		if (!adminUserService.getUserPlatforms().contains(quoteContract.getPlatform())) {
			return "500_ERROR";
		}
		model.addAttribute("contract", contract);
		model.addAttribute("attachments", quoteContractAttachmentService.loadQuoteContractAttachment(quoteContract));
		return "reservation_details";
	}

	/**
	 * 挂牌合同审核
	 * 
	 * @param auditRefuseReason
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping("/reservation/audit")
	@ResponseBody
	@UserLog(remark = "挂牌审核管理-操作-审核：同意/拒绝")
	public Result<String> reservationDetails(String auditRefuseReason, Long id, String status) {
		return quoteContractService.audit(auditRefuseReason, id, status);
	}

	@RequestMapping("/reservation/audit/check")
	@ResponseBody
	@UserLog(remark = "挂牌审核管理-操作-审核：同意/拒绝-check")
	public Result<String> reservationBindCheck(Long id) {
		Result<String> result = new Result<String>(Result.Type.SUCCESS);
		QuoteContract quoteContract = quoteContractService.get(id);
		OriginalContract contract = contractService.findByContractCodeAndPlatformAndCertiNo(quoteContract.getContractCode(), quoteContract.getPlatform(), quoteContract.getCustomer().getIdCardAccount());
		if (Enums.QuoteContractWorkFlow.REJECT.getValue().equals(quoteContract.getWorkFlow())) {
			result.addMessage("未匹配到相关原始资产....");
			result.setType(Result.Type.FAILURE);
		} else if (null == contract) {
			quoteContract.setAuditRefuseReason("未匹配到相关原始资产");
			quoteContract.setWorkFlow(Enums.QuoteContractWorkFlow.REJECT.getValue());
			quoteContractService.saveOrUpdate(quoteContract);
			result.addMessage("未匹配到相关原始资产....");
			result.setType(Result.Type.FAILURE);
		} else if (Enums.OriginalContractState.INVALID.getValue().equals(contract.getStatus())) {
			result.addMessage("未匹配到相关原始资产....");
			result.setType(Result.Type.FAILURE);
		}
		return result;
	}

	@RequestMapping("/claim")
	@UserLog(remark = "展期管理：返回展期列表页面")
	public String claim() {
		return "claim_list";
	}

	@RequestMapping("/claim/list")
	@ResponseBody
	@UserLog(remark = "展期列表")
	public DataTables<QuoteContract> claimList(ContractQueryForm contractQueryForm, DataTableRequest dataTableRequest) {
		Pageable pageable = new PageRequest(dataTableRequest.getStart() == 0 ? 0 : dataTableRequest.getStart() / dataTableRequest.getLength(), dataTableRequest.getLength(), new Sort(new Order(Direction.DESC, "createdDate")));
		contractQueryForm.setWorkFlow(Enums.QuoteContractWorkFlow.RENEWED.getValue() + "," + Enums.QuoteContractWorkFlow.PASSED.getValue());
		Page<QuoteContract> page = quoteContractService.findAll(quoteContractService.buildSpecification(contractQueryForm), pageable);
		DataTables<QuoteContract> result = new DataTables<QuoteContract>(page);
		result.setDraw(dataTableRequest.getDraw());
		return result;
	}

	/**
	 * 预览附件
	 * 
	 * @date 2016年5月9日
	 * @param String
	 * @return
	 */
	@RequestMapping("/claim/agreementDetailsImages/{objectId}")
	@UserLog(remark = "展期列表-查看协议：预览附件")
	public void agreementDetailsImages(@PathVariable("objectId") String objectId, HttpServletResponse response) {
		try {
			response.setContentType("application/pdf");
			byte[] content = gridfsService.getByteContent(objectId);
			if (content != null) {
				response.getOutputStream().write(content);
			}
		} catch (Exception e) {
			LOGGER.error("预览附件异常", e);
		}
	}

	@RequestMapping("/add")
	@ResponseBody
	public Result<OriginalContract> add(OriginalContract originalContract){
		if(null != contractService.findByContractCode(originalContract.getContractCode())){
			return new Result<OriginalContract>(Result.Type.FAILURE, "合同已存在，请核对确认", null);
		}
		originalContract = contractService.saveOrUpdate(originalContract);
		return new Result<OriginalContract>(Result.Type.SUCCESS, "添加成功", originalContract);
	}
	
	@RequestMapping("/existCheck")
	@ResponseBody
	public boolean existCheck(String contractCode){
		if(null == contractService.findByContractCode(contractCode)){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 根据合同编号返回原始合同(特殊兑付)
	 * 
	 * @date 2016年6月20日
	 * @param id
	 * @return
	 */
	@RequestMapping("/query/contract/{contractCode}")
	@ResponseBody
	@UserLog(remark = "特殊兑付根据合同编号返回原始合同")
	public Result<OriginalContract> getQuoteContract(@PathVariable("contractCode") String contractCode) {
		return contractService.findByContractCodeAndStatusNot(contractCode, Enums.OriginalContractState.VALID.getValue());
	}

}
