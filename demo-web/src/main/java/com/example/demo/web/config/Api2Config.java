package com.example.demo.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class Api2Config {
    @Bean
    public WebClient webClientForApi2(ClientRegistrationRepository clientRegistrations,
            OAuth2AuthorizedClientRepository authorizedClients) {
        var oauth = new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);

        oauth.setDefaultOAuth2AuthorizedClient(true);
        //oauth.setDefaultClientRegistrationId("publicapp");
        //oauth.setDefaultClientRegistrationId("demoapp");

        // @formatter:off
        return WebClient.builder()
                .baseUrl("http://localhost:8082/api2")
                .filter(oauth)
                .build();
        // @formatter:on
    }

}
