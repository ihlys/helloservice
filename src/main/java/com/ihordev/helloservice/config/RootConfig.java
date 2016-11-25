package com.ihordev.helloservice.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@Import(DataConfig.class)
@PropertySource("classpath:properties/app-config.properties")
public class RootConfig
{

    @Bean
    public MessageSource messageSource()
    {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/properties/i18n/messages/messages", "/org/hibernate/validator/ValidationMessages.properties");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(LocalValidatorFactoryBean localValidatorFactoryBean) {
         MethodValidationPostProcessor mvppr = new MethodValidationPostProcessor();
         mvppr.setValidator(localValidatorFactoryBean.getValidator());
         
         return mvppr;
    }
    
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource)
    {
        LocalValidatorFactoryBean lvfb = new LocalValidatorFactoryBean();
        lvfb.setValidationMessageSource(messageSource);
        return lvfb;
    }
    
}
