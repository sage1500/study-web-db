package com.example.demo.web.logging;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.util.NestedServletException;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(Integer.MIN_VALUE)
@Slf4j
public class AccessLoggingFilter implements Filter {

	private String[] ignorPaths = { "/css/**", "/js/**", "/images/**", "/manage/health" };

	private RequestMatcher ignoreRequestMatcher;

	@PostConstruct
	public void init() {
		RequestMatcher[] pathMatchers = new RequestMatcher[ignorPaths.length];
		for (int i = 0; i < pathMatchers.length; i++) {
			pathMatchers[i] = new AntPathRequestMatcher(ignorPaths[i]);
		}

		ignoreRequestMatcher = new OrRequestMatcher(pathMatchers);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (ignoreRequestMatcher.matches(httpRequest)) {
			chain.doFilter(request, response);
			return;
		}

		try {
			log.info("[ACCESS] Req. {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());

			chain.doFilter(request, response);

			log.info("[ACCESS] Rsp. status={} location={}", httpResponse.getStatus(),
					httpResponse.getHeader("location"));
		} catch (IOException | ServletException e) {
			logException(httpResponse, e);
			throw e;
		} catch (RuntimeException e) {
			logException(httpResponse, e);
			throw e;
		}
	}

	private void logException(HttpServletResponse httpResponse, Exception e) {
		Throwable target = e;
		if (e instanceof NestedServletException) {
			target = e.getCause();
		}
		log.info("[ACCESS] Rsp. status={} exception={}", httpResponse.getStatus(), target.getClass().getSimpleName());
	}
}
