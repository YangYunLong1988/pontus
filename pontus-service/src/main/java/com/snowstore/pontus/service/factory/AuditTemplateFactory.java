package com.snowstore.pontus.service.factory;

import org.springframework.stereotype.Component;

@Component
public class AuditTemplateFactory implements SmsTemplateFactory {

	@Override
	public String createSmsTemplate(String code) {
		return "<快鹿集团>，" + code + "，客服电话400-0033-699";
	}

}
