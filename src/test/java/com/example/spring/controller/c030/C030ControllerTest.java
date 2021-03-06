package com.example.spring.controller.c030;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/spring-context.xml" })
public class C030ControllerTest {
	@Autowired
	private WebApplicationContext wac;
	@Autowired
	private MockHttpSession mockHttpSession;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = webAppContextSetup(wac).build();
	}

	@Test
	public void sessionStartのGET() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(get("/c030/sessionStart").session(mockHttpSession))
				.andExpect(status().isOk())
				.andExpect(view().name("c030/sessionScope"))
				.andExpect(model().attributeExists("c030Model")).andReturn();
		checkC030Model(mvcResult);

		mvcResult = mockMvc
				.perform(get("/c030/sessionScope").session(mockHttpSession))
				.andExpect(status().isOk())
				.andExpect(view().name("c030/sessionScope"))
				.andExpect(model().attributeExists("c030Model")).andReturn();
		checkC030Model(mvcResult);

		mvcResult = mockMvc
				.perform(get("/c030/sessionClear").session(mockHttpSession))
				.andExpect(status().isOk())
				.andExpect(view().name("c030/sessionScope"))
				.andExpect(model().attributeExists("c030Model")).andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		Object c030ModelObject = mav.getModel().get("c030Model");
		assertThat(c030ModelObject, is(notNullValue()));
		assertThat(c030ModelObject, is(instanceOf(C030Model.class)));

		C030Model c030Model = (C030Model) c030ModelObject;
		assertThat(c030Model.getName(), is(nullValue()));
		assertThat(c030Model.getPrice(), is(nullValue()));
	}

	private void checkC030Model(MvcResult mvcResult) {
		// モデルデータの確認
		ModelAndView mav = mvcResult.getModelAndView();
		Object c030ModelObject = mav.getModel().get("c030Model");
		assertThat(c030ModelObject, is(notNullValue()));
		assertThat(c030ModelObject, is(instanceOf(C030Model.class)));

		C030Model c030Model = (C030Model) c030ModelObject;
		assertThat(c030Model.getName(), is("よくわかるHttpSession"));
		assertThat(c030Model.getPrice(), is(980));
	}
}
