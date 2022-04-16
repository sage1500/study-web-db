package com.example.demo.common.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties("common.auth")
@Data
public class AuthenticationProperties {
	private boolean enabled = false;
	private String[] ignorePaths = { "/css/**", "/js/**", "/img/**", "/webjars/**", "/management/health" };
	private String[] permitPaths = { "/" };
	private String postLogoutRedirectUri = "{baseUrl}/";
	private String postLogoutUrl = "/";
	private String failureUrl = "/error";
	private String successUrl;
	private OAuth2Login oauth2Login = new OAuth2Login();
	private FormLogin formLogin = new FormLogin();

	@Data
	public static class OAuth2Login {
		private boolean enabled = false;
		private String baseUri;
		private String logoutUri;
		private String clientId;
		private String clientSecret;
	}
	
	@Data
	public static class FormLogin {
		private boolean enabled = false;
		private String loginPage = "/login";
	}
}
