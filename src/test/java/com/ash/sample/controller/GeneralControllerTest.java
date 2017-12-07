package com.ash.sample.controller;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.ash.sample.service.DateService;

/**
 * @author Ash Izadi
 * <p>This test case is to make sure Controller works fine. At this level, we mock under layer services as <br/>
 * we don't care. We are only interested on control level
 * </p>
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = GeneralController.class)
public class GeneralControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private DateService dateService;
	
	private Optional<String> mockResult = Optional.of("28 10 1981 , 20 11 1990 , 9 year and 1 months and 8 days");
	
	
	@Test
	public void givenDateText_whenTheyArenotInOrder_thenRetunnInOrderAndDifference() throws Exception{
		
		
		given(dateService.processInput(Mockito.anyString())).willReturn(mockResult);
		
		mockMvc.perform(get("/input?input=20%2011%201990,%2028%2010%201981")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(content().string("28 10 1981 , 20 11 1990 , 9 year and 1 months and 8 days"));

		/**
		 * 		System.out.println(result.getResponse());
		 * MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		 * JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
		 */		
	      
	}
	@Test
	public void givenFile_whenTheyArenotInOrder_thenRetunnInOrderAndDifference() throws Exception{
		
		given(dateService.proccessFile(Mockito.anyString())).willReturn(mockResult);
		
		mockMvc.perform(get("/file/path?path=K:%5CAsh%5CProjects%5Cresources%5Csample.txt")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(content().string("28 10 1981 , 20 11 1990 , 9 year and 1 months and 8 days"));
	}
}
