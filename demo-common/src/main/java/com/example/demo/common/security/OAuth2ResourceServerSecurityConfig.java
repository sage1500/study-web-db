package com.example.demo.common.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 1)
@ConditionalOnProperty(prefix = "common.auth.oauth2-resource-server", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class OAuth2ResourceServerSecurityConfig extends WebSecurityConfigurerAdapter {
	private final AuthenticationProperties authProps;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**");

		// 認可設定
		if (authProps.isEnabled()) {
			http.authorizeRequests() //
					.antMatchers(authProps.getFailureUrl()).permitAll() //
					.antMatchers(authProps.getPermitPaths()).permitAll() //
					.anyRequest().authenticated();
		} else {
			http.authorizeRequests().anyRequest().permitAll();
		}

		// OAuth2 ログイン
		http.oauth2ResourceServer(oauth2 -> {
			// oauth2.accessDeniedHandler(null);
			oauth2.jwt();
		});
	}

}
