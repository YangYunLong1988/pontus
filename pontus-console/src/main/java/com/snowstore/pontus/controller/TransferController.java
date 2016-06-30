package com.snowstore.pontus.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.snowstore.pontus.domain.AssigneeAttachment;
import com.snowstore.pontus.domain.Transfer;
import com.snowstore.pontus.enums.Enums.AssigneeAttachmentState;
import com.snowstore.pontus.service.AdminUserService;
import com.snowstore.pontus.service.AssigneeAttachmentService;
import com.snowstore.pontus.service.TransferService;
import com.snowstore.pontus.service.vo.DataTableRequest;
import com.snowstore.pontus.service.vo.DataTables;
import com.snowstore.pontus.service.vo.Result;
import com.snowstore.pontus.service.vo.TransferQueryForm;
import com.snowstore.pontus.service.vo.TransferVo;

@Controller
@RequestMapping("/transfer")
public class TransferController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

	@Autowired
	private TransferService transferService;

	@Autowired
	private AssigneeAttachmentService assigneeAttachmentService;
	
	@Autowired
	private AdminUserService adminUserService;

	@RequestMapping("/page")
	@UserLog(remark = "交易管理：返回交易管理页")
	public String getPage() {
		return "transfer_list";
	}

	/**
	 * 分页查询
	 * 
	 * @date 2016年5月13日
	 * @param dataTableRequest
	 * @param transferQueryForm
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	@UserLog(remark = "获取交易请求List")
	public DataTables<TransferVo> getListTransfer(DataTableRequest dataTableRequest, TransferQueryForm transferQueryForm) {
		Pageable pageable = new PageRequest(dataTableRequest.getStart() == 0 ? 0 : dataTableRequest.getStart() / dataTableRequest.getLength(), dataTableRequest.getLength(), new Sort(new Order(Direction.DESC, "createdDate")));
		transferQueryForm.setState("有效");
		Page<Transfer> page = transferService.findAll(transferService.buildSpecification(transferQueryForm), pageable);
		List<TransferVo> list = new ArrayList<TransferVo>();
		for (Transfer tf : page.getContent()) {
			try {
				TransferVo vo = new TransferVo();
				vo.setCode(tf.getCode());
				vo.setContractCode(tf.getQuoteContract().getOriginalContract().getContractCode());
				vo.setDiscountRate(tf.getDiscountRate());
				vo.setEndDate(tf.getEndDate());
				vo.setId(tf.getId());
				vo.setOriginalContractId(tf.getQuoteContract().getOriginalContract().getId());
				vo.setIdCardName(tf.getQuoteContract().getCustomer().getIdCardName());
				vo.setPhone(tf.getQuoteContract().getCustomer().getPhone());
				vo.setPrincipal(tf.getQuoteContract().getPrincipal());
				vo.setTransferedValue(tf.getTransferedValue());
				vo.setWorkFlow(tf.getWorkFlow());
				vo.setCreatedDate(tf.getCreatedDate());
				vo.setAssigneeCount(tf.getTradedAssigneeSet().size());
				list.add(vo);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		DataTables<TransferVo> result = new DataTables<TransferVo>();
		result.setData(list);
		result.setRecordsFiltered(page.getTotalElements());
		result.setRecordsTotal(page.getTotalElements());
		result.setDraw(dataTableRequest.getDraw());
		return result;
	}

	/**
	 * 取消转让状态无效
	 * 
	 * @date 2016年5月16日
	 * @param id
	 * @return
	 */
	@RequestMapping("/changeStatus")
	@ResponseBody
	@UserLog(remark = "交易管理-操作-回访取消：修改转让状态")
	public Result<String> changeStatus(Long id) {
		String action = transferService.changeStatus(id);
		if ("true".equals(action)) {
			return new Result<String>(Result.Type.SUCCESS, "操作成功");
		} else if ("false".equals(action)) {
			return new Result<String>(Result.Type.FAILURE, "操作失败");
		} else {
			return new Result<String>(Result.Type.WARNING, "状态为 " + action + " 操作失败");
		}
	}

	/**
	 * 显示转让单详情
	 * 
	 * @date 2016年5月16日
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/details/{id}")
	@UserLog(remark = "交易管理-操作-已登记人数：显示转让单详情")
	public String details(@PathVariable("id") Long id, Model model) {
		Transfer transfer = transferService.findOne(id);
		if(!adminUserService.getUserPlatforms().contains(transfer.getQuoteContract().getPlatform())){
			return "500_ERROR";
		}
		model.addAttribute("transfer", transfer);
		return "transfer_details";
	}

	@RequestMapping("/audit/{id}")
	@UserLog(remark = "交易管理：交易附件详情")
	public String transferAudit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("transfer", transferService.findOne(id));
		return "transfer_audit";
	}

	@RequestMapping("/protocol")
	@UserLog(remark = "交易管理：获取附件")
	@ResponseBody
	public Result<ArrayList<AssigneeAttachment>> loadAssigneeAttachment(Long assigneeId, String type) {
		Result<ArrayList<AssigneeAttachment>> result = new Result<ArrayList<AssigneeAttachment>>(Result.Type.SUCCESS);
		List<AssigneeAttachment> attachments = assigneeAttachmentService.getByAssigneeAndAttachType(assigneeId, type,AssigneeAttachmentState.VALID.getValue());
		if (null != attachments && attachments.size()>0) {
			result.setData((ArrayList<AssigneeAttachment>) attachments);
		} else {
			result.setType(Result.Type.FAILURE);
		}
		return result;
	}
}
