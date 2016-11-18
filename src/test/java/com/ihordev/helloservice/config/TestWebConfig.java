package com.ihordev.helloservice.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import com.ihordev.helloservice.service.ContactService;

@Component
@Import(WebConfig.class)
public class TestWebConfig
{

    @Bean
    public ContactService mockContactService()
    {
        return Mockito.mock(ContactService.class);
    }

}
