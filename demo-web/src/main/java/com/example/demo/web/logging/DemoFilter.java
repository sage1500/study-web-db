package com.example.demo.web.logging;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoFilter extends OncePerRequestFilter {

	private static String MDC_KEY_USERNAME = "username";
	private static String ATTR_NAME_AUTH_INITIALIZED = "AUTH_INITIALIZED";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			var authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = null;

			if (authentication instanceof AbstractAuthenticationToken
					&& !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
				// 認証済のルート

				// MDC設定
				username = authentication.getName();
				MDC.put(MDC_KEY_USERNAME, username);

				// 初期化判定
				var initialized = request.getSession().getAttribute(ATTR_NAME_AUTH_INITIALIZED);
				if (!"true".equals(initialized)) {
					request.getSession().setAttribute(ATTR_NAME_AUTH_INITIALIZED, "true");
					log.info("1st proc: [{}]", initialized);
				} else {
					log.info("2nd proc");
				}
			}

			filterChain.doFilter(request, response);
		} finally {
			MDC.remove(MDC_KEY_USERNAME);
		}
	}
}
