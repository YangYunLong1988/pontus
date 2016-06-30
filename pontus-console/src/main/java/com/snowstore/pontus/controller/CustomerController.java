package com.snowstore.pontus.controller;

import java.util.HashSet;
import java.util.Set;

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
import com.snowstore.pontus.domain.AdminUser;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.service.AdminUserService;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.MemberAttachmentService;
import com.snowstore.pontus.service.vo.CustomerQueryForm;
import com.snowstore.pontus.service.vo.DataTableRequest;
import com.snowstore.pontus.service.vo.DataTables;
import com.snowstore.pontus.service.vo.Result;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private MemberAttachmentService memberAttachmentService;
	
	@Autowired
	private AdminUserService adminUserService;

	@RequestMapping("/page")
	@UserLog(remark="会员管理：返回会员列表页")
	public String page() {
		return "customer_list";
	}

	@RequestMapping("/list")
	@ResponseBody
	@UserLog(remark="会员列表")
	public DataTables<Customer> list(CustomerQueryForm from, String status, DataTableRequest dataTableRequest) {
		Pageable pageable = new PageRequest(dataTableRequest.getStart() == 0 ? 0 : dataTableRequest.getStart() / dataTableRequest.getLength(), dataTableRequest.getLength(),new Sort(new Order(Direction.DESC,"createdDate")));
		Page<Customer> page = customerService.findAll(customerService.buildSpecification(from), pageable, from);
		DataTables<Customer> result = new DataTables<Customer>(page);
		result.setDraw(dataTableRequest.getDraw());
		return result;
	}

	@RequestMapping("/details/{id}")
	@UserLog(remark="会员管理-操作-查看：会员详情")
	public String details(@PathVariable("id") Long id, Model model) {
		Customer customer = customerService.get(id);
		if(!AdminUser.Role.ADMIN.name().equals(adminUserService.getUserDetails().getRole())){
			//TODO 求集合交集
			Set<QuoteContract> set = customer.getQuoteContractSet();
			Set<String> platforms = new HashSet<String>();
			for (QuoteContract quoteContract : set) {
				platforms.add(quoteContract.getPlatform());
			}
			if(!adminUserService.checkPlatforms(platforms)){
				return "500_ERROR";
			}
		}
		model.addAttribute("customer", customer);
		model.addAttribute("attachments", memberAttachmentService.loadMemberAttachment(customer));
		return "customer_details";
	}

	@RequestMapping("/changeStatus")
	@ResponseBody
	@UserLog(remark="会员管理-操作-冻结/恢复：修改会员状态")
	public Result<String> changeStatus(Long id, String status) {
		boolean action = customerService.changeStatus(id, status);
		if (action) {
			return new Result<String>(Result.Type.SUCCESS, "操作成功");
		} else {
			return new Result<String>(Result.Type.FAILURE, "操作失败");
		}
	}
}
