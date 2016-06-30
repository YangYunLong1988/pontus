package com.snowstore.pontus.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.constants.PontusConstant;
import com.snowstore.pontus.service.userDetails.WebCustomDetailsService;

public class BaseHandlerInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private WebCustomDetailsService webCustomDetailsService;
	@Autowired
	private CustomerService customerService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURI();
		if (PontusConstant.urls.contains(url)) { // 访问白名单
			return true;
		}

		Long customerId = webCustomDetailsService.getCustomerId();

		Customer customer = customerService.get(customerId);
		if (customer == null) {
			response.sendRedirect("/login");

		} else if (!Enums.CustomerWorkFlow.PASSED.getValue().equals(customer.getWorkFlow()) && !"/cust/identify-authentication".equals(url)) {
			response.sendRedirect("/cust/identify-authentication");
		}

		return true;
	}
}
