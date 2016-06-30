package com.snowstore.pontus.web.interceptor;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.snowstore.pontus.service.constants.PontusConstant;
import com.snowstore.pontus.web.annotation.AvoidDuplicateSubmission;
import com.snowstore.pontus.web.annotation.TokenAction;

public class AvoidDuplicateSubmissionInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			AvoidDuplicateSubmission annotation = method.getAnnotation(AvoidDuplicateSubmission.class);
			if (annotation != null) {
				TokenAction tokenAction = annotation.tokenAction();
				if (tokenAction == TokenAction.PREPARATION) {
					request.getSession().setAttribute("token", UUID.randomUUID().toString());
				} else if (tokenAction == TokenAction.SUBMIT) {
					String token = (String) request.getSession().getAttribute("token");
					request.getSession().setAttribute("token", UUID.randomUUID().toString());
					if (!(token != null && token.equals(request.getParameter("token")))) {
						request.setAttribute(PontusConstant.SUBMIT_REPEATEDLY, Boolean.TRUE);
						if (!"".equals(annotation.dupUrl())) {
							response.sendRedirect(request.getContextPath() + annotation.dupUrl());
							return false;
						}
					}
				}
			}
			return true;
		} else {
			return super.preHandle(request, response, handler);
		}
	}
}