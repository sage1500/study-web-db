package com.example.demo.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.web.DemoInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {
	private final DemoInterceptor demoInterceptor;
	private String[] ignorPaths = { "/css/**", "/js/**", "/images/**", "/webjars/**", "/manage/health" };

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(demoInterceptor).excludePathPatterns(ignorPaths);
	}

}
