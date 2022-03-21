package com.example.demo.domain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// TODO
@Configuration
@ComponentScan
@MapperScan("com.example.demo.domain.**.repository")
public class DemoDomainConfiguration {
}
