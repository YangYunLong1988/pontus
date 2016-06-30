package com.snowstore.pontus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.snowstore.pontus.service.userDetails.UserDetailsImpl;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
	 @Bean
	  public AuditorAware<String> auditorProvider() {
		 return new AuditorAware<String>() {

			@Override
			public String getCurrentAuditor() {
				Authentication authen = SecurityContextHolder.getContext().getAuthentication();
				return authen == null||"anonymousUser".equals(authen.getPrincipal()) ?"anonymousUser":((UserDetailsImpl)authen.getPrincipal()).getUsername();
			}
		};
	 }
}
