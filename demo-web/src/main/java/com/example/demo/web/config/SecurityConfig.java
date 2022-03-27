package com.example.demo.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final ClientRegistrationRepository clientRegistrationRepository;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/management/health");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 認可設定
		// @formatter:off
        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/manage/**").permitAll()
            //.anyRequest().authenticated()
        	.anyRequest().permitAll()
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

		// @formatter:off
		http.sessionManagement()
			//.invalidSessionStrategy((reqest, response) -> {})
			//.sessionAuthenticationFailureHandler((reqest, response, e) -> {})
			//.sessionAuthenticationStrategy((auth, request, response) -> {})
			.maximumSessions(10000);
		http.exceptionHandling()
			//.authenticationEntryPoint(((reqest, response, e) -> {})
			//.accessDeniedHandler((reqest, response, e) -> {})
			;
        // @formatter:on

	}

}
