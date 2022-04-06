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
	private String failureUrl = "/error";
	private OAuth2login oauth2login = new OAuth2login();

	@Data
	public static class OAuth2login {
		private boolean enabled = false;
		private String baseUri;
		private String logoutUri;
		private String clientId;
		private String clientSecret;
	}
}
