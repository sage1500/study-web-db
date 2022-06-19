package com.example.demo.profile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class DemoProfileApplication implements ApplicationRunner {

	@Value("!${demo.profile.type:none}!")
	private String type;

	@Value("!${demo.profile.type2:none}!")
	private String type2;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DemoProfileApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("type={}", type);
		log.info("type2={}", type2);
	}
}

/*
 * 検証結果:
 * 
 * - p1,p2,p3 のように指定した場合:
 *   - p1, p2, p3 の順に適用される。 
 * - spring.profiles.group で p1, p2, p3 の順に指定した場合
 *   - group名, p1, p2, p3 の順に適用される。
 */
