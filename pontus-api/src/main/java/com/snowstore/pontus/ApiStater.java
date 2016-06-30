package com.snowstore.pontus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication(exclude = { WebMvcAutoConfiguration.class, DispatcherServletAutoConfiguration.class })
public class ApiStater extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApiStater.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApiStater.class, args);
	}
}
