package com.ihordev.helloservice.web;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ihordev.helloservice.config.TestWebConfig;
import com.ihordev.helloservice.domain.Contact;
import com.ihordev.helloservice.service.ContactService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestWebConfig.class)
@WebAppConfiguration
@ActiveProfiles("web-test")
public class ContactControllerTest
{
	
	private MockMvc mockMvc;
	
	@Autowired
	private ContactService mockContactService;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	
	@Before
	public void setUp()
	{
		Mockito.reset(mockContactService);
		
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void shouldReturnContactsThatFilteredByNameFilter() throws Exception
	{
		Contact contact1 = new Contact();
		contact1.setId(1L);
		contact1.setName("Contact one");
		
		Contact contact2 = new Contact();
		contact2.setId(2L);
		contact2.setName("Contact two");
		
		Contact contact3 = new Contact();
		contact3.setId(3L);
		contact3.setName("A contact three");
		
		String nameFilter = "^A.*$";
		
		when(mockContactService.findContactsUsingFilter(eq(nameFilter))).thenReturn(Arrays.asList(contact1, contact2));
		
		mockMvc.perform(get("/hello/contacts")
				.param("nameFilter", nameFilter))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", equalTo(1)))
			.andExpect(jsonPath("$[0].name", equalTo("Contact one")))
			.andExpect(jsonPath("$[1].id", equalTo(2)))
			.andExpect(jsonPath("$[1].name", equalTo("Contact two")));
		
		verify(mockContactService, atLeastOnce()).findContactsUsingFilter(eq(nameFilter));
	}
	
	@Test
	public void shouldReturnBadRequestResponse_missingParameter() throws Exception 
	{
		mockMvc.perform(get("/hello/contacts"))
			.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
	}
	
	@Test
	public void shouldReturnBadRequestResponse_invalidParameter() throws Exception 
	{
		String nameFilter = "[";

		mockMvc.perform(get("/hello/contacts")
				.param("nameFilter", nameFilter))
		.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
	}
	
}
