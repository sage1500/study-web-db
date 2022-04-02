package com.example.demo.common.security;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CommonAuthenticationFilter extends OncePerRequestFilter {

	private static String MDC_KEY_USERNAME = "username";

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
			}

			filterChain.doFilter(request, response);
		} finally {
			MDC.remove(MDC_KEY_USERNAME);
		}
	}
}
