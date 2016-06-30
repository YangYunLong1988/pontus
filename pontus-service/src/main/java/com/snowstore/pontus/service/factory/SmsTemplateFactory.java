package com.snowstore.pontus.service.factory;

/**
 * @author sm
 * 
 */
public interface SmsTemplateFactory {

	/**
	 * @param code
	 * @return
	 */
	public String createSmsTemplate(String code);

}
