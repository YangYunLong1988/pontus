package com.snowstore.pontus.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesService {

	@Value("${snowstore.esb.system.self.code:}")
	private String selfSystemCode;

	@Value("${snowstore.esb.system.tpp.code:}")
	private String tppSystemCode;

	@Value("${snowstore.esb.system.tpp.username:}")
	private String tppSystemName;

	@Value("${snowstore.esb.system.tpp.password:}")
	private String tppSystemPassword;

	@Value("${snowstore.esb.system.mars.code:}")
	private String marsSystemCode;

	@Value("${snowstore.esb.system.mars.username:}")
	private String marsSystemName;

	@Value("${snowstore.esb.system.mars.password:}")
	private String marsSystemPassword;

	@Value("${snowstore.esb.system.apollo.code:}")
	private String apolloSystemCode;

	@Value("${snowstore.esb.system.apollo.username:}")
	private String apolloSystemName;

	@Value("${snowstore.esb.system.apollo.password:}")
	private String apolloSystemPassword;

	/**
	 * 获取tpp的esb报文头相关配置
	 * 
	 * @return
	 */
	public Map<String, String> getTppSystemConfiguration() {
		Map<String, String> tppConfig = new HashMap<>();
		tppConfig.put("tppSystemCode", tppSystemCode);
		tppConfig.put("selfSystemCode", selfSystemCode);
		tppConfig.put("tppSystemName", tppSystemName);
		tppConfig.put("tppSystemPassword", tppSystemPassword);
		return tppConfig;
	}

	/**
	 * 获取mars的esb报文头相关配置
	 * 
	 * @return
	 */
	public Map<String, String> getMarsSystemConfiguration() {
		Map<String, String> tppConfig = new HashMap<>();
		tppConfig.put("marsSystemCode", marsSystemCode);
		tppConfig.put("selfSystemCode", selfSystemCode);
		tppConfig.put("marsSystemName", marsSystemName);
		tppConfig.put("marsSystemPassword", marsSystemPassword);
		return tppConfig;
	}

	/**
	 * 获取mars的esb报文头相关配置
	 * 
	 * @return
	 */
	public Map<String, String> getApolloSystemConfiguration() {
		Map<String, String> tppConfig = new HashMap<>();
		tppConfig.put("apolloSystemCode", apolloSystemCode);
		tppConfig.put("selfSystemCode", selfSystemCode);
		tppConfig.put("apolloSystemName", apolloSystemName);
		tppConfig.put("apolloSystemPassword", apolloSystemPassword);
		return tppConfig;
	}
}
