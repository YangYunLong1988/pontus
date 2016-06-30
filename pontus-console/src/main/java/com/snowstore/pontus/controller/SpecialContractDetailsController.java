package com.snowstore.pontus.controller;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.SpecialContract;
import com.snowstore.pontus.domain.SpecialDetail;
import com.snowstore.pontus.service.AssigneeService;
import com.snowstore.pontus.service.SpecialContractDetailsService;
import com.snowstore.pontus.service.SpecialContractService;
import com.snowstore.pontus.service.vo.DataTableRequest;
import com.snowstore.pontus.service.vo.DataTables;
import com.snowstore.pontus.service.vo.Result;

@Controller
@RequestMapping("/specialContractDetails")
public class SpecialContractDetailsController {

	@Autowired
	private AssigneeService assigneeService;
	@Autowired
	private SpecialContractDetailsService specialContractDetailsService;

	@Autowired
	private SpecialContractService specialContractService;

	/**
	 * 显示特殊兑付明细页面
	 * 
	 * @date 2016年6月17日
	 * @param id
	 * @return
	 */
	@RequestMapping("/page/{id}")
	@UserLog(remark = "显示明细或者新建明细")
	public String showSpecialContractDetails(@PathVariable("id") Long id, Model model) {
		String action = "create";
		OriginalContract contract = new OriginalContract();
		Long specialId = id;
		if (null != id && -1 != id) {// 查看详情
			action = "detail";
			SpecialContract sc = specialContractService.findOne(id);
			if (sc != null) {
				contract = sc.getOriginalContract();
			}
		}
		model.addAttribute("spcialId", specialId);
		model.addAttribute("action", action);// 标识创建还是显示明细
		model.addAttribute("contract", contract);
		return "special_contract_details";
	}

	/**
	 * 显示明细列表
	 * 
	 * @date 2016年6月17日
	 * @param dataTableRequest
	 * @return
	 */
	@RequestMapping("/list")
	@UserLog(remark = "显示特殊兑付明细列表")
	@ResponseBody
	public DataTables<SpecialDetail> list(DataTableRequest dataTableRequest, Long spcialId) {
		Pageable pageable = new PageRequest(dataTableRequest.getStart() == 0 ? 0 : dataTableRequest.getStart() / dataTableRequest.getLength(), dataTableRequest.getLength(), new Sort(new Order(Direction.DESC, "createdDate")));
		Page<SpecialDetail> page = specialContractDetailsService.findBySpecialContract(spcialId, pageable);
		DataTables<SpecialDetail> result = new DataTables<SpecialDetail>(page);
		result.setDraw(dataTableRequest.getDraw());
		return result;
	}

	@RequestMapping(value = "/addDetail", method = RequestMethod.POST)
	@ResponseBody
	@UserLog(remark = "特殊兑付明细-添加明细")
	public Result<String> addDetail(Long originalContractId, String listDetail) {
		Map<String, String> emailMap = new HashMap<String, String>();
		Result<String> result = specialContractDetailsService.addSpecialDetailsService(originalContractId, listDetail, emailMap);
		if (!emailMap.isEmpty()) {
			for (String key : emailMap.keySet()) {
				assigneeService.assigneeSms(emailMap.get(key),key,"special_contract_srf",null);
			}
		}
		return result;
	}

}
