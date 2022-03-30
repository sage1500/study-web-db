package com.example.demo.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
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

	@GetMapping("/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("[HELLO] hello");

		Order order = new Order().withStatusCode(1);
		orderRepository.insert(order);

		log.info("[HELLO] insert after id={}", order.getId());

		var secctx = SecurityContextHolder.getContext();
		if (secctx == null) {
			log.info("SecurityContext is null");
		} else {
			log.info("SecurityContext is not null: auth={}", secctx.getAuthentication());
		}
		
//		if (true) {
//			throw new RuntimeException("test");
//		}

		return "hello";
		// return "redirect:/hello2";
		// return "forward:/hello2";
		// request.getRequestDispatcher("/hello2").forward(request, response);
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

	@GetMapping("/basic/hello")
	public String hello3() {
		log.info("[HELLO] hello3");
		return "hello";
	}

}
