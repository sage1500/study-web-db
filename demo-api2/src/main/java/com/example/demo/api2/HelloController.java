package com.example.demo.api2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HelloController {

	@GetMapping("/hello")
	public String hello() {
		log.info("[HELLO] hello");
		return "hello";
	}

	@GetMapping("hello2")
	public String hello2() {
		log.info("[HELLO] hello2");
		return "hello2";
	}

}
