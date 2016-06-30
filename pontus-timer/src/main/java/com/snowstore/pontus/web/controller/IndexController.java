package com.snowstore.pontus.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snowstore.pontus.timer.TimerWrapper;

@Controller
public class IndexController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private TimerWrapper timerWrapper;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@ResponseBody
	@RequestMapping("/exec")
	public WebResult execTimer(String t) {
		Class<? extends TimerWrapper> clazz = TimerWrapper.class;
		Method m1;
		try {
			m1 = clazz.getDeclaredMethod(t);
			m1.invoke(timerWrapper);
			return WebResult.newSuccessWebResult();
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.error("执行Timer异常", e);
			return WebResult.newExceptionWebResult();
		}
	}
}
