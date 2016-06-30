package com.snowstore.pontus.config;

import java.math.BigDecimal;
import java.util.HashMap;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import java.util.Map;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author sm
 * 
 */
@Component
public class JerseyConfig extends ResourceConfig {

	@Autowired
	public JerseyConfig(@Value("${token.timeout:30}") long timeout, @Value("${cors.domain:}") String corsDomain, @Value("${sms.limit:20}") long smsLimit, @Value("${file.maxsize:5}") BigDecimal fileMaxsize) {
		packages("com.snowstore.pontus.endpoint;com.snowstore.pontus.provider");
		Map<String, Object> map = new HashMap<String, Object>();
		register(MultiPartFeature.class);
		map.put("timeout", timeout);
		String domain = StringUtils.isEmpty(corsDomain) ? "*" : corsDomain;
		map.put("domain", domain);
		map.put("smsLimit", smsLimit);
		map.put("fileMaxsize", fileMaxsize);
		map.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		addProperties(map);
	}
}
