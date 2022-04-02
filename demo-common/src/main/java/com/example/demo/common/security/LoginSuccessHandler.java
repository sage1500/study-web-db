package com.example.demo.common.security;

import javax.servlet.http.HttpServletRequest;

public interface LoginSuccessHandler {
	void loginSucceeded(HttpServletRequest request);
}
