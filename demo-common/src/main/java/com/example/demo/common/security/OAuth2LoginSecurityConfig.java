package com.example.demo.common.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 1)
@ConditionalOnProperty(prefix = "common.auth.oauth2-login", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {
	private final AuthenticationProperties authProps;
	private final ClientRegistrationRepository clientRegistrationRepository;

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
		http.oauth2Login(oauth2 -> {
			oauth2.authorizationEndpoint(authorization -> {
				DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
						clientRegistrationRepository,
						OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
				authorizationRequestResolver
						.setAuthorizationRequestCustomizer(customizer -> customizer.additionalParameters(params -> {
							var locale = LocaleContextHolder.getLocale();
							log.debug("locale {}", locale.getLanguage());
							params.put("ui_locales", locale.getLanguage());
						}));
				authorization.authorizationRequestResolver(authorizationRequestResolver);
			});
			oauth2.failureHandler(failureHandler());

			// 共通Login設定
			SecurityConfigHelper.setCommonLoginSettings(oauth2, authProps);
		});

		// ログアウト
		http.logout(logout -> {
			OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(
					this.clientRegistrationRepository);
			logoutSuccessHandler.setPostLogoutRedirectUri(authProps.getPostLogoutRedirectUri());
			logout.logoutSuccessHandler(logoutSuccessHandler);
		});

		// セッションがないときにどうするか？という設定
		// セッションがない初回アクセスの場合にも呼ばれる。
		// セッションが無効になった場合にどうするか？ではないことに注意。
		// http.sessionManagement().invalidSessionUrl("xxx");

		http.exceptionHandling().accessDeniedHandler(SecurityConfigHelper.accessDeniedHandler(authProps));

		// ログイン画面に遷移させる処理
		// http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
	}

	private AuthenticationFailureHandler failureHandler() {
		// OAuth2Login において、認証エラーはシステムエラー扱い
		var handler = new SimpleUrlAuthenticationFailureHandler(authProps.getFailureUrl());
		return (request, response, exception) -> {
			log.error("AuthenticationFailure: {}", exception.getMessage());
			handler.onAuthenticationFailure(request, response, exception);
		};
	}

}
