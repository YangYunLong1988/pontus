package com.snowstore.pontus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snowstore.pontus.domain.SpecialContract;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.service.SpecialContractService;
import com.snowstore.pontus.service.vo.DataTableRequest;
import com.snowstore.pontus.service.vo.DataTables;
import com.snowstore.pontus.service.vo.SpecialContractQueryForm;

@Controller
@RequestMapping("/specialContract")
public class SpecialContractController {
	
	@Autowired
	private SpecialContractService specialContractService;
	
	@RequestMapping("")
	public String page(Model model){
		model.addAttribute("platforms", Enums.Platform.values());
		return "special_contract_list";
	}
	@RequestMapping("/list")
	@ResponseBody
	public DataTables<SpecialContract> list(SpecialContractQueryForm specialContractQueryForm, DataTableRequest dataTableRequest){
		Pageable pageable = new PageRequest(dataTableRequest.getStart() == 0 ? 0 : dataTableRequest.getStart() / dataTableRequest.getLength(), dataTableRequest.getLength(), new Sort(new Order(Direction.DESC, "createdDate")));
		Page<SpecialContract> page = specialContractService.findAll(specialContractService.buildSpecification(specialContractQueryForm), pageable);
		DataTables<SpecialContract> result = new DataTables<SpecialContract>(page);
		result.setDraw(dataTableRequest.getDraw());
		return result;
	}
}
