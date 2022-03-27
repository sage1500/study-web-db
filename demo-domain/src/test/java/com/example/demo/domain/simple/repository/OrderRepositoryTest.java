package com.example.demo.domain.simple.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class OrderRepositoryTest {

	@Autowired
	OrderRepository orderRepos;

	@Value("${spring.datasource.platform}")
	String platform;

	@Test
	public void test_hoge1() {
		log.info("[!] platform: {}", platform);
	}

	@Test
	public void test_hoge2() {
		log.info("[!] repos: {}", orderRepos);
	}
}
