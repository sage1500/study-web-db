package com.example.demo.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DemoInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		new Exception().fillInStackTrace().printStackTrace();
		
		var secctx = SecurityContextHolder.getContext();
		var auth = (secctx != null) ? secctx.getAuthentication() : null;
		var authenticated = (auth != null) ? auth.isAuthenticated() : false;
		log.info("auth={}", auth);
		log.info("authenticated={}", authenticated);
		
		final String key = "my-initialized";
		
		var initialized = request.getSession().getAttribute(key);;
		if (!"true".equals(initialized)) {
			request.getSession().setAttribute(key, "true");
			log.info("1st proc: [{}]", initialized);
		} else {
			log.info("2nd proc");
		}
		
		return true;
	}

}
