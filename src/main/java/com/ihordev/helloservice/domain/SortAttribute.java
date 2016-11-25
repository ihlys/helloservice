package com.ihordev.helloservice.domain;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks class field as sortable which means it can be used for sorting during data retrieval.
 * <p>
 * This annotation is used by {@link com.ihordev.helloservice.validation.ResourcePageValidator.class}
 */
@Target({ FIELD })
@Retention(RUNTIME)
@Documented
public @interface SortAttribute
{
        
    
}