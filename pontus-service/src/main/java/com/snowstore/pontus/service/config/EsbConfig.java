package com.snowstore.pontus.service.config;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zendaimoney.hera.connector.EnableEsb;

@Configuration
@EnableEsb
public class EsbConfig {

	@Bean
	public Mapper dozerBeanMapper() {
		return new DozerBeanMapper();
	}

}
