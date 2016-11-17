package com.ihordev.helloservice.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ihordev.helloservice.service.ContactService;

@Component
@Import(WebConfig.class)
public class TestWebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public ContactService mockContactService()
	{
		return Mockito.mock(ContactService.class);
	}
	
}
