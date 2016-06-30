package com.snowstore.pontus.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.snowstore.pontus.service.userDetails.UserDetailsImpl;

public class SecurityHandlerInterceptorAdapter extends HandlerInterceptorAdapter {
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Authentication authen = SecurityContextHolder.getContext().getAuthentication();
		if (null != modelAndView && !"anonymousUser".equals(authen.getPrincipal())) {
			UserDetails userDetails =  (UserDetailsImpl) authen.getPrincipal();
			modelAndView.addObject("current", userDetails);
		} 
	}
}
