package com.example.demo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.example.demo.common.DemoCommonConfiguration;
import com.example.demo.domain.DemoDomainConfiguration;

@SpringBootApplication
@Import({ DemoDomainConfiguration.class, DemoCommonConfiguration.class })
public class DemoWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoWebApplication.class, args);
	}

}
