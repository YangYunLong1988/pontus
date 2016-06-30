package com.snowstore.pontus.service.config;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
	@Bean
	public FastDateFormat fastDateFormat() {
		return FastDateFormat.getInstance("yyyy-MM-dd");
	}
}
