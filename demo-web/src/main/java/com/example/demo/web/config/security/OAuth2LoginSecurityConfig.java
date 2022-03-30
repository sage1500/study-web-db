package com.example.demo.web.config.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import com.example.demo.web.logging.DemoFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10 + 3)
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {
	private final ClientRegistrationRepository clientRegistrationRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**");

		http.addFilterBefore(new DemoFilter(), ExceptionTranslationFilter.class);

		// 認可設定
		// @formatter:off
        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .anyRequest().authenticated()
        	//.anyRequest().permitAll()
        	;
        // @formatter:on

		// OAuth2 ログイン
		// @formatter:off
        http.oauth2Login(oauth2 -> oauth2
            .authorizationEndpoint(authorization -> {
        		DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
        				clientRegistrationRepository, "/oauth2/authorization");
        		authorizationRequestResolver
        				.setAuthorizationRequestCustomizer(customizer -> customizer.additionalParameters(params -> {
        					var locale = LocaleContextHolder.getLocale();
        					log.debug("locale {}", locale.getLanguage());
        					params.put("ui_locales", locale.getLanguage());
        				}));
            	authorization.authorizationRequestResolver(authorizationRequestResolver);
            }));
        // @formatter:on

		// ログアウト
		http.logout(logout -> {
			OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(
					this.clientRegistrationRepository);
			logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/");
			logout.logoutSuccessHandler(logoutSuccessHandler);
		});
	}
}
