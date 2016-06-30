package com.snowstore.pontus.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.snowstore.pontus.service.constants.PontusConstant;
import com.snowstore.pontus.service.userDetails.WebCustomDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private WebCustomDetailsService webCustomDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// @Override
	// public void configure(WebSecurity web) throws Exception {
	// web.ignoring().antMatchers("/static/**"); // #3
	// }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 设置拦截规则
		http.authorizeRequests().antMatchers(PontusConstant.urls.toArray(new String[PontusConstant.urls.size()])).permitAll().anyRequest().authenticated();
		// 自定义登录
		http.formLogin().usernameParameter("phoneNumber").passwordParameter("password").loginPage("/login").failureUrl("/login?error").successHandler(new SimpleUrlAuthenticationSuccessHandler() {
			/**
			 * 记住帐号
			 */
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
				if (StringUtils.isNotEmpty(request.getParameter("rememberPhoneNumber"))) {
					Cookie username = new Cookie("username", request.getParameter("phoneNumber"));
					username.setMaxAge(60 * 60 * 24 * 30);
					response.addCookie(username);
				}
				super.onAuthenticationSuccess(request, response, authentication);
			}
		}).permitAll();
		// 自定义注销
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/").logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler() {

			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
				Cookie username = new Cookie("username", "");
				username.setMaxAge(0);
				response.addCookie(username);
				super.onLogoutSuccess(request, response, authentication);
			}
		});
		// 禁用 csrf
		http.csrf().disable();

		// http.logout().logoutRequestMatcher(new
		// AntPathRequestMatcher("/logout"));
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(webCustomDetailsService).passwordEncoder(passwordEncoder);
	}
}