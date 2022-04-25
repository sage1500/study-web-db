package com.example.demo.api2;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HelloController {

	@GetMapping("/hello")
	public String hello(@AuthenticationPrincipal Jwt jwt) {
		log.info("[HELLO] hello: {}", jwt);
		if (jwt != null) {
			log.info("  user: {}", jwt.getClaimAsString("preferred_username"));
		}
		return "hello";
	}

	@GetMapping("hello2")
	public String hello2(@AuthenticationPrincipal Jwt jwt) {
		log.info("[HELLO] hello2: {}", jwt);
		if (jwt != null) {
			log.info("  user: {}", jwt.getClaimAsString("preferred_username"));
		}
		return "hello2";
	}

}
