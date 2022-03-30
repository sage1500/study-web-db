package com.example.demo.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.domain.simple.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = HelloController.class)
@AutoConfigureMockMvc(addFilters = false)
@Slf4j
public class HogeTest {
	@Autowired
	MockMvc mvc;

	@MockBean
	OrderRepository orderRepository;

	@Test
	public void test_hoge() throws Exception {
		log.info("[!] test_hoge");
		mvc.perform(get("/hello")).andDo(print()).andExpect(status().isOk());
	}
}
