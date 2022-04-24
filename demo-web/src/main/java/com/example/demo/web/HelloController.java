package com.example.demo.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.domain.entity.Order;
import com.example.demo.domain.entity.OrderExample;
import com.example.demo.domain.simple.repository.OrderRepository;
import com.example.demo.domain.simple.repository2.OrderRepository2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HelloController {
	private final OrderRepository orderRepository;
	private final OrderRepository2 orderRepository2;
	private final WebClient webClient;
	private final OAuth2AuthorizedClientRepository clientRespository;

	// @Secured("ROLE_ADMIN")
	@GetMapping("/hello")
	@Transactional
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

		if (false) {
			throw new RuntimeException("test");
		}

		return "hello";
		// return "redirect:/hello2";
		// return "forward:/hello2";
		// request.getRequestDispatcher("/hello2").forward(request, response);
	}

	@GetMapping("hello2")
	@Transactional
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

	@GetMapping("hello4")
	public String hello4(HttpServletRequest request) {
		webClient.get().uri("/hello").retrieve().bodyToMono(String.class).block();
		return "hello";
	}

	@GetMapping("hello5")
	public String hello5(HttpServletRequest request) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof OAuth2AuthenticationToken) {
			var regId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
			log.info("[!] regId: {}", regId);

			var authClient = clientRespository.loadAuthorizedClient(regId, authentication, request);
			log.info("[!] client: {}", authClient);
			log.info("[!] accessToken: {}", authClient.getAccessToken().getTokenValue());

			webClient //
					.get().uri("/hello2") //
					//.headers(headers -> headers.setBearerAuth(authClient.getAccessToken().getTokenValue())) //
					.retrieve().bodyToMono(String.class).block();
		}

		return "hello";
	}

	@PostMapping(path = "/hello", params = "post")
	public String helloPost() {
		log.info("[HELLO] helloPost");
		return "hello";
	}

	// memo: TransactionManager と
	// Mapperインタフェースの組み合わせを間違えても動くが、
	// その場合はトランザクションなし(auto-commit)としての動作になるため注意が必要。
	@GetMapping("/hello11")
	@Transactional
	public String hello11(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("[HELLO] hello11");
		Order order = new Order().withStatusCode(1);
		orderRepository.insert(order);
		log.info("[HELLO] insert after id={}", order.getId());
		return "hello";
	}

	@GetMapping("hello12")
	@Transactional
	public String hello12() {
		log.info("[HELLO] hello12");

		OrderExample orderExample = new OrderExample();
		orderExample.createCriteria().andStatusCodeIsNotNull();

		List<Order> results = orderRepository.selectByExample(orderExample);
		results.forEach(v -> log.info("[HELLO] result = {}", v));

		return "hello";
	}

	@GetMapping("/hello21")
	@Transactional("transactionManager2")
	public String hello21(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("[HELLO] hello21");
		Order order = new Order().withStatusCode(1);
		orderRepository2.insert(order);
		log.info("[HELLO] insert after id={}", order.getId());
		return "hello";
	}

	@GetMapping("hello22")
	@Transactional("transactionManager2")
	public String hello22() {
		log.info("[HELLO] hello22");

		OrderExample orderExample = new OrderExample();
		orderExample.createCriteria().andStatusCodeIsNotNull();

		List<Order> results = orderRepository2.selectByExample(orderExample);
		results.forEach(v -> log.info("[HELLO] result = {}", v));

		return "hello";
	}

}
