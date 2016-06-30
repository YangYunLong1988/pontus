package com.snowstore.pontus.service.factory;

import org.springframework.stereotype.Component;

@Component
public class SignSmsTemplateFactory implements SmsTemplateFactory {

	@Override
	public String createSmsTemplate(String code) {
		return "您的验证码为" + code + "，请注意保密哦！如有疑问请联系400-0033-699";
	}

}
