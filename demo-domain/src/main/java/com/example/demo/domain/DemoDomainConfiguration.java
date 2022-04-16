package com.example.demo.domain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// TODO
@Configuration
@ComponentScan
@MapperScan("com.example.demo.domain.**.repository")
@MapperScan(basePackages = "com.example.demo.domain.**.repository2", sqlSessionFactoryRef = "sqlSessionFactory2")
public class DemoDomainConfiguration {
}
