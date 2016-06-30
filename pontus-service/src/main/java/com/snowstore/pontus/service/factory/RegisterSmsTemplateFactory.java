package com.snowstore.pontus.service.factory;

import org.springframework.stereotype.Component;

@Component
public class RegisterSmsTemplateFactory implements SmsTemplateFactory {

	@Override
	public String createSmsTemplate(String code) {
		return "您的注册验证码为" + code + "，感谢您注册快鹿投资者服务中心账户，如有疑问请联系400-0033-699";
	}

}
