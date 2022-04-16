package com.example.demo.common.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 1)
@ConditionalOnProperty(prefix = "common.auth.form-login", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class FormLoginSecurityConfig extends WebSecurityConfigurerAdapter {
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
		http.formLogin(form -> {
			// loginProcessingUrl, loginPage, failureUrl を認証不要とする。
			// QueryParameter が付いた状態でのURLチェックとしており、
			// http.authorizeRequests() の方で設定する permitAll() よりも厳格。
			form.permitAll();

			// ログインフォームを用意している場合はデフォルトと同じURLの場合でも呼び出す必要がある
			form.loginPage(authProps.getFormLogin().getLoginPage());
			
			// 共通Login設定
			SecurityConfigHelper.setCommonLoginSettings(form, authProps);
		});

		// ログアウト
		//
		// - URLデフォルト値
		// -- logoutSuccessUrl : loginPage + "?logout"
		//
		http.logout(logout -> {
			logout.logoutSuccessUrl(authProps.getPostLogoutUrl());
		});

		http.exceptionHandling().accessDeniedHandler(SecurityConfigHelper.accessDeniedHandler(authProps));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
