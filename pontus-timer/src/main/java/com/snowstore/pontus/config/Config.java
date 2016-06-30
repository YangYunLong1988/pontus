package com.snowstore.pontus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snowstore.terra.client.TerraPropertyPlaceholderConfigurer;


@Configuration
public class Config {
	/**配置中心
	 * @return
	 */
	@Bean
	public TerraPropertyPlaceholderConfigurer terraPropertyPlaceholderConfigurer() {
		return new TerraPropertyPlaceholderConfigurer();
	}
}
