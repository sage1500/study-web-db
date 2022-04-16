package com.example.demo.common.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Order(SecurityProperties.IGNORED_ORDER)
@EnableConfigurationProperties(AuthenticationProperties.class)
@RequiredArgsConstructor
public class IgnoreSecurityConfig extends WebSecurityConfigurerAdapter {
	private final AuthenticationProperties authProps;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().antMatchers(authProps.getIgnorePaths());
		http.authorizeRequests().anyRequest().permitAll();
	}
}
