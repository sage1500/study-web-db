package com.example.demo.common.security;

import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(WebAttributes.AUTHENTICATION_EXCEPTION) // セッションに保存されている例外をView側で参照できるように
public class FormLoginPageController {
	@GetMapping("${common.auth.form-login.login-page:/login}")
	public String login() {
		return "login/login";
	}
}
