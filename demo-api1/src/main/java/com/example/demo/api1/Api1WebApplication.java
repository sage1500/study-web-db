package com.example.demo.api1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.example.demo.common.DemoCommonConfiguration;

@SpringBootApplication
@Import({ DemoCommonConfiguration.class })
public class Api1WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(Api1WebApplication.class, args);
	}

}
