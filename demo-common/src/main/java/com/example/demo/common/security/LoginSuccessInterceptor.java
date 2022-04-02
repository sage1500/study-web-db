package com.example.demo.common.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginSuccessInterceptor implements HandlerInterceptor {
	private static String ATTR_NAME_AUTH_INITIALIZED = "AUTH_INITIALIZED";

	private final List<LoginSuccessHandler> loginSuccessHandlers;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AbstractAuthenticationToken
				&& !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
			// 認証済のルート

			var initialized = request.getSession().getAttribute(ATTR_NAME_AUTH_INITIALIZED);
			if (!"true".equals(initialized)) {
				request.getSession().setAttribute(ATTR_NAME_AUTH_INITIALIZED, "true");

				for (var h : loginSuccessHandlers) {
					h.loginSucceeded(request);
				}
			}
		}
		return true;
	}

}
