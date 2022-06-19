package com.example.demo.autoconfigure;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@Slf4j
public class DemoAutoconfigureConfiguration {
	@Bean
	public CommandLineRunner myCommandLineRunner() {
		return args -> {
			log.info("★MyCommandLineRunner executed.");
		};
	}
}
