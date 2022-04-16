package com.example.demo.common.security;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.DelegatingAccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 1)
@ConditionalOnProperty(prefix = "common.auth.form-login", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class FormLoginSecurityConfig extends WebSecurityConfigurerAdapter {
	private final AuthenticationProperties authProps;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**");

		if (false) {
			http.addFilterBefore((req, rsp, chain) -> {
				log.info("[WEB-Form] {}", ((HttpServletRequest) req).getRequestURI());
				chain.doFilter(req, rsp);
			}, ExceptionTranslationFilter.class);
		}

		// 認可設定
		if (authProps.isEnabled()) {
			http.authorizeRequests() //
					.antMatchers(authProps.getFailureUrl()).permitAll() //
					.antMatchers(authProps.getPermitPaths()).permitAll() //
					.anyRequest().authenticated();
		} else {
			http.authorizeRequests().anyRequest().permitAll();
		}

		// Form ログイン
		//
		// - URL設定
		// -- POST /login : loginProcessingUrl() - 認証処理(Spring Securityで処理)
		// -- GET /login : loginPage() - ログインフォーム(FormLoginPageControllerで処理)
		// -- GET /login?error : failureUrl() - 認証失敗(FormLoginPageControllerで処理)
		// -- GET /hello2 : defaultSuccessUrl() - ログイン成功後のデフォルトの遷移先
		//
		// - URLデフォルト値
		// -- loginProcessingUrl : loginPage
		// -- failureUrl : loginPage + "?error"
		//
		http.formLogin() //
				// loginProcessingUrl, loginPage, failureUrl を認証不要とする。
				// QueryParameter が付いた状態でのURLチェックとしており、
				// http.authorizeRequests() の方で設定する permitAll() よりも厳格。
				.permitAll() //
				.loginPage("/login") // ログインフォームを用意している場合はデフォルトと同じURLの場合でも呼び出す必要がある
				.defaultSuccessUrl("/hello", true) // ログイン後に遷移する画面を指定する場合
		;

		// ログアウト
		//
		// - URLデフォルト値
		// -- logoutSuccessUrl : loginPage + "?logout"
		//
		http.logout(logout -> {
			logout.logoutSuccessUrl(authProps.getPostLogoutUrl());
		});

		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
	}

	private AccessDeniedHandler accessDeniedHandler() {
		//
		// 例外により挙動変更
		// - MissingCsrfTokenException: セッション切れ判定
		// - Spring Session が有効になっていないと、この挙動にならない（？）
		// - 上記以外: デフォルト実装(403エラー)
		//

		var handlers = new LinkedHashMap<Class<? extends AccessDeniedException>, AccessDeniedHandler>();

		handlers.put(MissingCsrfTokenException.class, (request, response, e) -> {
			new DefaultRedirectStrategy().sendRedirect(request, response, "/error?type=invalidSession");
		});

		return new DelegatingAccessDeniedHandler(handlers, new AccessDeniedHandlerImpl());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
