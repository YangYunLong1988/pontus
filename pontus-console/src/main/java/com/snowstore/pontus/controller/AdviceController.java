package com.snowstore.pontus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.snowstore.log.annotation.UserLog;

@ControllerAdvice
public class AdviceController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@UserLog(remark="统一异常处理")
	public String handleIOException(Exception ex) {
		LOGGER.error("统一异常处理", ex);
		return "500_ERROR";
	}
}