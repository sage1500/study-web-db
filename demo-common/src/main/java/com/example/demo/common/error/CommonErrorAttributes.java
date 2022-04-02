package com.example.demo.common.error;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommonErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		log.info("getErrorAttributes");
		var attrs = super.getErrorAttributes(webRequest, options);
		return attrs;
	}

	@Override
	public Throwable getError(WebRequest webRequest) {
		log.info("getError");
		var exception = super.getError(webRequest);
		if (exception == null) {
			exception = getAttribute(webRequest, WebAttributes.AUTHENTICATION_EXCEPTION);
		}
		return exception;
	}

	@SuppressWarnings("unchecked")
	private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
		T value = (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
		if (value == null) {
			value = (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_SESSION);
		}
		return value;
	}
}
