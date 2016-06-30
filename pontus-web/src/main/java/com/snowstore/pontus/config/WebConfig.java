package com.snowstore.pontus.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.snowstore.pontus.web.interceptor.AvoidDuplicateSubmissionInterceptor;
import com.snowstore.pontus.web.interceptor.BaseHandlerInterceptor;
import com.snowstore.terra.client.TerraPropertyPlaceholderConfigurer;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public BaseHandlerInterceptor baseHandlerInterceptor() {
		return new BaseHandlerInterceptor();
	}

	@Bean
	public TerraPropertyPlaceholderConfigurer terraPropertyPlaceholderConfigurer() {
		return new TerraPropertyPlaceholderConfigurer();
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new KaptchaServlet(), "/kaptcha");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(baseHandlerInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(new AvoidDuplicateSubmissionInterceptor()).addPathPatterns("/**");
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/not-found");
				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/not-found");
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/not-found");
				ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/not-found");
				container.addErrorPages(error400Page, error401Page, error404Page, error500Page);
			}
		};
	}

}
