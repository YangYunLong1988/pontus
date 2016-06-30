package com.snowstore.pontus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.Assignee;
import com.snowstore.pontus.service.AssigneeService;
import com.snowstore.pontus.service.vo.AssigneeQueryForm;
import com.snowstore.pontus.service.vo.AssigneeVo;
import com.snowstore.pontus.service.vo.DataTableRequest;
import com.snowstore.pontus.service.vo.DataTables;
import com.snowstore.pontus.service.vo.Result;

@Controller
@RequestMapping("/assignee")
public class AssigneeController {

	@Autowired
	private AssigneeService assigneeService;

	@RequestMapping("/list")
	@ResponseBody
	@UserLog(remark = "受让预约列表")
	public DataTables<AssigneeVo> getListAssignee(DataTableRequest dataTableRequest, AssigneeQueryForm assigneeQueryForm) {
		Pageable pageable = new PageRequest(dataTableRequest.getStart() == 0 ? 0 : dataTableRequest.getStart() / dataTableRequest.getLength(), dataTableRequest.getLength(), new Sort(new Order(Direction.ASC, "createdDate")));
		Page<Assignee> page = assigneeService.findAll(assigneeService.buildSpecification(assigneeQueryForm), pageable);
		List<AssigneeVo> list = new ArrayList<AssigneeVo>();
		for (Assignee as : page.getContent()) {
			AssigneeVo vo = new AssigneeVo();
			vo.setCode(as.getCode());
			vo.setAssigneePrice(as.getAssigneePrice());
			vo.setAttachFlow(as.getAttachFlow());
			vo.setCreatedDate(as.getCreatedDate());
			vo.setId(as.getId());
			vo.setIdCardName(as.getCustomer().getIdCardName());
			vo.setPhone(as.getCustomer().getPhone());
			vo.setWorkFlow(as.getWorkFlow());
			if (null != as.getTransfer().getTradedAssignee()) {
				vo.setAgreeAssigneeId(as.getTransfer().getTradedAssignee().getId());
			}
			list.add(vo);
		}
		DataTables<AssigneeVo> result = new DataTables<AssigneeVo>();
		result.setData(list);
		result.setRecordsFiltered(page.getTotalElements());
		result.setRecordsTotal(page.getTotalElements());
		result.setDraw(dataTableRequest.getDraw());
		return result;
	}

	/**
	 * 修改受让状态
	 * 
	 * @date 2016年5月16日
	 * @param id
	 * @param workFlow
	 * @return
	 */
	@RequestMapping("/changeWorkFlow")
	@ResponseBody
	@UserLog(remark = "修改受让状态")
	public Result<String> changeWorkFlow(Long id, String workFlow) {
		String action = assigneeService.changeWorkFlow(id, workFlow);
		if ("true".equals(action)) {
			return new Result<String>(Result.Type.SUCCESS, "操作成功");
		} else if ("false".equals(action)) {
			return new Result<String>(Result.Type.FAILURE, "操作失败");
		} else {
			return new Result<String>(Result.Type.WARNING, action);
		}
	}

	@RequestMapping("/audit/action")
	@ResponseBody
	@UserLog(remark = "交易管理：附件审核")
	public Result<String> doTransferAudit(Long assigneeId, Boolean opinion, String reason) {
		return assigneeService.assigneeAttachment(assigneeId, opinion, reason);
	}

	/**
	 * 用来测试短信发送
	 * 
	 * @date 2016年5月26日
	 * @return
	 */
	@RequestMapping("/testmsm/{phone}/{context}")
	@ResponseBody
	public String testMSM(@PathVariable String phone, @PathVariable String context) {
		assigneeService.assigneeSms(phone, context, "test_sms", null);
		return "已发送，请查收";
	}

}
