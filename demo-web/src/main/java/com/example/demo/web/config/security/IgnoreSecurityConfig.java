package com.example.demo.web.config.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10 + 1)
@Slf4j
public class IgnoreSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().antMatchers("/css/**", "/js/**", "/img/**", "/management/health");
		http.authorizeRequests().anyRequest().permitAll();

		http.addFilterBefore((req, rsp, chain) -> {
			log.info("[WEB-Ignore] Req. {}", ((HttpServletRequest) req).getRequestURI());
			chain.doFilter(req, rsp);
			log.info("[WEB-Ignore] Rsp. {}", ((HttpServletRequest) req).getRequestURI());
		}, ExceptionTranslationFilter.class);
	}
}
