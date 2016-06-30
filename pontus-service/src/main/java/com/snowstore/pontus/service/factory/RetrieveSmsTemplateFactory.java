package com.snowstore.pontus.service.factory;

import org.springframework.stereotype.Component;

@Component
public class RetrieveSmsTemplateFactory implements SmsTemplateFactory {

	@Override
	public String createSmsTemplate(String code) {
		return "您提交了找回密码的请求，验证码为" + code + "，如有疑问请联系400-0033-699";
	}

}
