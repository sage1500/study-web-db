package com.example.demo.api2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.example.demo.common.DemoCommonConfiguration;

@SpringBootApplication
@Import({ DemoCommonConfiguration.class })
public class Api2WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(Api2WebApplication.class, args);
	}

}
