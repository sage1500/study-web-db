package com.example.demo.common.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Order(SecurityProperties.IGNORED_ORDER)
@EnableConfigurationProperties(AuthenticationProperties.class)
@RequiredArgsConstructor
@Slf4j
public class IgnoreSecurityConfig extends WebSecurityConfigurerAdapter {
	private final AuthenticationProperties authProps;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().antMatchers(authProps.getIgnorePaths());
		http.authorizeRequests().anyRequest().permitAll();

		http.addFilterBefore((req, rsp, chain) -> {
			log.info("[WEB-Ignore] {}", ((HttpServletRequest) req).getRequestURI());
			chain.doFilter(req, rsp);
		}, ExceptionTranslationFilter.class);
	}
}
