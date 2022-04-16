package com.example.demo.common.security;

import java.util.LinkedHashMap;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.DelegatingAccessDeniedHandler;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

public class SecurityConfigHelper {
	public static AccessDeniedHandler accessDeniedHandler(AuthenticationProperties authProps) {
		//
		// 例外により挙動変更
		// - MissingCsrfTokenException: セッション切れ判定
		// - Spring Session が有効になっていないと、この挙動にならない（？）
		// - 上記以外: デフォルト実装(403エラー)
		//

		var handlers = new LinkedHashMap<Class<? extends AccessDeniedException>, AccessDeniedHandler>();

		handlers.put(MissingCsrfTokenException.class, (request, response, e) -> {
			new DefaultRedirectStrategy().sendRedirect(request, response,
					authProps.getFailureUrl() + "?type=invalidSession");
		});

		return new DelegatingAccessDeniedHandler(handlers, new AccessDeniedHandlerImpl());
	}
	
	public static void setCommonLoginSettings(AbstractAuthenticationFilterConfigurer<?,?,?> auth, AuthenticationProperties authProps) {
		// ログイン後に遷移する画面を強制する
		String successUrl = authProps.getSuccessUrl();
		if (successUrl != null) {
			auth.defaultSuccessUrl(successUrl, true);
		}
	}
}
