package com.ihordev.helloservice.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ihordev.helloservice.web.WebMarker;

@Component
@EnableWebMvc
@ComponentScan(basePackageClasses = WebMarker.class)
public class WebConfig extends WebMvcConfigurerAdapter {

    
}
