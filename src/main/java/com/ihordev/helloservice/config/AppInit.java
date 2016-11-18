package com.ihordev.helloservice.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer
{

    @Override
    protected Class<?>[] getRootConfigClasses()
    {
        return new Class<?>[] { WebConfig.class, RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses()
    {
        return new Class<?>[] {};
    }

    @Override
    protected String[] getServletMappings()
    {
        return new String[] { "/" };
    }

}
