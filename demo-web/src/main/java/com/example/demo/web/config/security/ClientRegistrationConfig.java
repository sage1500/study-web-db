package com.example.demo.web.config.security;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class ClientRegistrationConfig {
	@Value("${services.auth.logout-uri}")
	private String logoutUri;

	@Bean
	@ConditionalOnMissingBean({ ClientRegistrationRepository.class })
	InMemoryClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties) {
		List<ClientRegistration> registrations = OAuth2ClientPropertiesRegistrationAdapter
				.getClientRegistrations(properties).values().stream()
				.map(o -> ClientRegistration.withClientRegistration(o)
						.providerConfigurationMetadata(Map.of("end_session_endpoint", logoutUri)).build())
				.collect(Collectors.toList());

		return new InMemoryClientRegistrationRepository(registrations);
	}
}