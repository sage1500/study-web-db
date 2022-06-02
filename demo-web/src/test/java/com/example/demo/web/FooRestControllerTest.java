package com.example.demo.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = FooRestController.class)
@AutoConfigureWebMvc
@AutoConfigureMockMvc(addFilters = false)
class FooRestControllerTest {
	@Autowired
	MockMvc mvc;

	ObjectMapper mapper = new ObjectMapper();

	@Test
	void test() throws Exception {
		var body = mvc.perform(get("/foo")) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();
		var resource = mapper.readValue(body, FooRestController.FooResource.class);
		assertThat(resource.getName()).isEqualTo("foo");
	}
}
