package com.example.demo.common.security;

import java.util.HashMap;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 1)
@ConditionalOnProperty(prefix = "common.auth.oauth2-login", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {
	private final AuthenticationProperties authProps;
	private final ClientRegistrationRepository clientRegistrationRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**");

		// ????????????
		if (authProps.isEnabled() || authProps.isUseDummyUser()) {
			http.authorizeRequests() //
					.antMatchers(authProps.getFailureUrl()).permitAll() //
					.antMatchers(authProps.getPermitPaths()).permitAll() //
					.anyRequest().authenticated();
		} else {
			http.authorizeRequests().anyRequest().permitAll();
		}

		// OAuth2 ????????????
		http.oauth2Login(oauth2 -> {
			oauth2.authorizationEndpoint(authorization -> {
				DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
						clientRegistrationRepository,
						OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
				authorizationRequestResolver
						.setAuthorizationRequestCustomizer(customizer -> customizer.additionalParameters(params -> {
							var locale = LocaleContextHolder.getLocale();
							log.debug("locale {}", locale.getLanguage());
							params.put("ui_locales", locale.getLanguage());
						}));
				authorization.authorizationRequestResolver(authorizationRequestResolver);
			});
			oauth2.failureHandler(failureHandler());

			// ??????Login??????
			SecurityConfigHelper.setCommonLoginSettings(oauth2, authProps);
		});

		// ???????????????
		http.logout(logout -> {
			OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(
					this.clientRegistrationRepository);
			logoutSuccessHandler.setPostLogoutRedirectUri(authProps.getPostLogoutRedirectUri());
			logout.logoutSuccessHandler(logoutSuccessHandler);
		});

		// ??????????????????????????????????????????????????????????????????
		// ????????????????????????????????????????????????????????????????????????
		// ?????????????????????????????????????????????????????????????????????????????????????????????
		// http.sessionManagement().invalidSessionUrl("xxx");

		http.exceptionHandling().accessDeniedHandler(SecurityConfigHelper.accessDeniedHandler(authProps));

		// ??????????????????????????????????????????
		// http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
		if (authProps.isUseDummyUser()) {
			http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
				//
				// ?????????????????????????????????????????????????????????
				//

				// ??????????????????
				var claims = new HashMap<String, Object>();
				claims.put(IdTokenClaimNames.SUB, "123");
				claims.put(StandardClaimNames.PREFERRED_USERNAME, "user123");
				var idToken = new OidcIdToken("xxxx", null, null, claims);
				var authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
				var principal = new DefaultOidcUser(authorities, idToken, StandardClaimNames.PREFERRED_USERNAME);
				OAuth2AuthenticationToken auth = new OAuth2AuthenticationToken(principal, authorities, "myapp");

				// ???????????????????????????
				var context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(auth);
				SecurityContextHolder.setContext(context);

				// AuthenticationSuccessHandler?????????
				new SavedRequestAwareAuthenticationSuccessHandler().onAuthenticationSuccess(request, response, auth);
			});
		}
	}

	private AuthenticationFailureHandler failureHandler() {
		// OAuth2Login ????????????????????????????????????????????????????????????
		var handler = new SimpleUrlAuthenticationFailureHandler(authProps.getFailureUrl());
		return (request, response, exception) -> {
			log.error("AuthenticationFailure: {}", exception.getMessage());
			handler.onAuthenticationFailure(request, response, exception);
		};
	}

}
