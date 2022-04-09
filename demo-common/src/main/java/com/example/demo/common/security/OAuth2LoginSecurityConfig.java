package com.example.demo.common.security;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.DelegatingAccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 1)
@ConditionalOnProperty(prefix = "common.auth.oauth2login", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {
	private final AuthenticationProperties authProps;
	private final ClientRegistrationRepository clientRegistrationRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**");

		// http.addFilterBefore(new DemoFilter(), ExceptionTranslationFilter.class);
		http.addFilterBefore((req, rsp, chain) -> {
			log.info("[WEB-OAuth2] {}", ((HttpServletRequest) req).getRequestURI());
			chain.doFilter(req, rsp);
		}, ExceptionTranslationFilter.class);

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
		http.oauth2Login(oauth2 -> oauth2 //
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
				}).failureHandler(failureHandler()));

		// ログアウト
		http.logout(logout -> {
			OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(
					this.clientRegistrationRepository);
			logoutSuccessHandler.setPostLogoutRedirectUri(authProps.getPostLogoutRedirectUri());
			logout.logoutSuccessHandler(logoutSuccessHandler);
		});

		// セッションがないときにどうするか？という設定
		// 初回アクセスでセッションがないときも呼ばれることに注意。
		//http.sessionManagement().invalidSessionUrl("/error?type=invalidSession");

		//
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
	}

	private AuthenticationFailureHandler failureHandler() {
		// TODO ログを仕込むためだけに拡張するべきか
		var handler = new SimpleUrlAuthenticationFailureHandler(authProps.getFailureUrl());
		// handler.setUseForward(true);
		return handler;
	}

	private AccessDeniedHandler accessDeniedHandler() {
		//
		// 例外により挙動変更
		// - MissingCsrfTokenException: セッション切れ判定
		// - 上記以外: デフォルト実装(403エラー)
		//
		
		var handlers = new LinkedHashMap<Class<? extends AccessDeniedException>, AccessDeniedHandler>();

		handlers.put(MissingCsrfTokenException.class, (request, response, e) -> {
			new DefaultRedirectStrategy().sendRedirect(request, response, "/error?type=invalidSession");
		});
		
		var handler = new DelegatingAccessDeniedHandler(handlers, new AccessDeniedHandlerImpl());
		return handler;
	}
}
