package com.snowstore.pontus.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.service.vo.DataTableRequest;
import com.snowstore.pontus.service.vo.DataTables;

@Controller
@RequestMapping("/content")
public class ContentController {
	
	@RequestMapping("/page")
	public String page(){
		return "content_list";
	}
	
	@RequestMapping("/list")
	@ResponseBody
	@UserLog(remark="TBD")
	public DataTables<Customer> list(Customer customer,DataTableRequest dataTableRequest){
		//Pageable pageable = new PageRequest(dataTableRequest.getStart() == 0 ? 0 : dataTableRequest.getStart() / dataTableRequest.getLength(), dataTableRequest.getLength());
		Page<Customer> page = null;//contractService.findAll(null, pageable, customer);
		DataTables<Customer> result = new DataTables<Customer>(page);
		result.setDraw(dataTableRequest.getDraw());
		return result;
	}
	
	@RequestMapping("/editor")
	@UserLog(remark="TBD")
	public String editor(){
		return "content_editor";
	}
}
