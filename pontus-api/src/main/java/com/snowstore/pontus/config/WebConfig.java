package com.snowstore.pontus.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.snowstore.log.configure.LogStashConfigure;
import com.snowstore.mercury.configuration.EnableHealth;
import com.snowstore.pontus.audit.ApiSession;
import com.snowstore.pontus.provider.FloodFilter.CountPertime;
import com.snowstore.terra.client.TerraPropertyPlaceholderConfigurer;

/**
 * @author sm
 * 
 */
@Configuration
@EnableJpaAuditing
@EnableHealth
@Import(LogStashConfigure.class)
public class WebConfig {

	public static final String ACCESS_TOKEN = "accessToken";

	@Bean
	public TerraPropertyPlaceholderConfigurer terraPropertyPlaceholderConfigurer() {
		return new TerraPropertyPlaceholderConfigurer();
	}

	@Bean
	public PasswordEncoder bcryptEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuditorAware<String> apiAuditorAware() {
		return new AuditorAware<String>() {
			@Override
			public String getCurrentAuditor() {
				if (null == ApiSession.getCustomerId())
					return null;
				return ApiSession.getCustomerId();
			}
		};
	}

	@Bean
	public LoadingCache<String, CountPertime> loadingCache() {
		return CacheBuilder.newBuilder().maximumSize(10000).expireAfterWrite(1, TimeUnit.HOURS).build(new CacheLoader<String, CountPertime>() {
			public CountPertime load(String key) {
				return new CountPertime();
			}
		});
	}

}
