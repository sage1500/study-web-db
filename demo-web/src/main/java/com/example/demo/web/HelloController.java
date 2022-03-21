package com.example.demo.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.domain.entity.Order;
import com.example.demo.domain.entity.OrderExample;
import com.example.demo.domain.simple.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HelloController {
	private final OrderRepository orderRepository;

	@GetMapping("hello")
	public String hello() {
		log.info("[HELLO] hello");

		Order order = new Order().withStatusCode(1);
		orderRepository.insertSelective(order);
		
		log.info("[HELLO] insert after id={}", order.getId());

		return "hello";
	}

	@GetMapping("hello2")
	public String hello2() {
		log.info("[HELLO] hello2");

		OrderExample orderExample = new OrderExample();
		orderExample.createCriteria().andStatusCodeIsNotNull();

		List<Order> results = orderRepository.selectByExample(orderExample);
		results.forEach(v -> log.info("[HELLO] result = {}", v));

		return "hello";
	}

}
