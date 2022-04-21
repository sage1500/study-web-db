package com.example.demo.common.security;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@ConditionalOnClass(ClientRegistrationRepository.class)
@RequiredArgsConstructor
public class ClientRegistrationConfig {
	private final AuthenticationProperties authProps;

	@Bean
	@ConditionalOnMissingBean({ ClientRegistrationRepository.class })
	InMemoryClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties) {
		List<ClientRegistration> registrations = OAuth2ClientPropertiesRegistrationAdapter
				.getClientRegistrations(properties).values().stream()
				.map(o -> ClientRegistration.withClientRegistration(o)
						.providerConfigurationMetadata(
								Map.of("end_session_endpoint", authProps.getOauth2Login().getLogoutUri()))
						.build())
				.collect(Collectors.toList());

		return new InMemoryClientRegistrationRepository(registrations);
	}
}