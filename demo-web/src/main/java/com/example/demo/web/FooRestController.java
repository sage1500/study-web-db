package com.example.demo.web;

import java.io.Serializable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FooRestController {
	@GetMapping("/foo")
	public ResponseEntity<FooResource> foo() throws Exception {
		return ResponseEntity.ok().body(new FooResource("foo"));
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class FooResource implements Serializable {
		private String name;
	}
}
