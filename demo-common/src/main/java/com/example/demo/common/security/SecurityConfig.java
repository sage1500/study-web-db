package com.example.demo.common.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
@EnableConfigurationProperties(AuthenticationProperties.class)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {

}
