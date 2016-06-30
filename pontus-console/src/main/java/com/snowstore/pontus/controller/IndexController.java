package com.snowstore.pontus.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.service.GridfsService;

@Controller
public class IndexController {
	
	@Autowired
	private GridfsService gridfsService;

	@RequestMapping("login")
	@UserLog(remark="登录页面")
	public String login() {
		return "login";
	}

	@RequestMapping("/")
	@UserLog(remark="返回会员列表页")
	public String index() {
		return "customer_list";
	}

	@RequestMapping("/pic/{objectId}")
	@UserLog(remark="获取图片")
	public void getPic(@PathVariable("objectId") String objectId,HttpServletResponse response){
		try {
			response.setContentType("image/jpeg");
			byte[] content = gridfsService.getByteContent(objectId);
			if (content != null) {
				response.getOutputStream().write(content);
			}
		} catch (Exception e) {
		
		}
	}
	@RequestMapping("/pageError")
	@UserLog(remark="pageError")
	public String errorPage(){
		return "500_ERROR";
	}
}
