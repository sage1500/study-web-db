package com.example.demo.web.config.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10 + 2)
@Slf4j
public class HttpBasicSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().antMatchers("/basic/**");
		http.authorizeRequests().anyRequest().authenticated();
		http.httpBasic().realmName("Your secret area.");

		http.authenticationManager(auth -> {
			log.info("[WEB-Basic] Authenticate: user={} password={}", auth.getName(), auth.getCredentials());
			return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(),
					auth.getAuthorities());
		});

		http.addFilterBefore((req, rsp, chain) -> {
			log.info("[WEB-Basic] Req. {}", ((HttpServletRequest) req).getRequestURI());
			chain.doFilter(req, rsp);
			log.info("[WEB-Basic] Rsp. {}", ((HttpServletRequest) req).getRequestURI());
		}, ExceptionTranslationFilter.class);
	}
}