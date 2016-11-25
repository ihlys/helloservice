package com.ihordev.helloservice.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.ihordev.helloservice.model.ResourcePage;

/**
 * Validate that the annotated {@link ResourcePage} has correct properties.
 * For properties {@link ResourcePage#getPage()} and {@link ResourcePage#getLimit()}
 * only max values can be specified because minimal values are always equals to one.
 * <p>
 * This annotation is used by {@link ResourcePageValidator}.
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = ResourcePageValidator.class)
@Documented
public @interface ValidResourcePage
{
    String message() default "{javax.validation.constraints.Page.message}";
    
    int maxPage() default Integer.MAX_VALUE;
    
    int maxLimit() default Integer.MAX_VALUE;

    ResourcePage.Order[] orders() default {ResourcePage.Order.ASC, ResourcePage.Order.DESC};
    
    /**
     * Specifies entity that resource page will contain for specification of attributes 
     * that are valid for sorting. It is used together with 
     * {@link com.ihordev.helloservice.domain.SortAttribute} annotation on entity 
     * attributes that specifies if they are valid for sorting.
     * 
     * @return    entity    persistence entity for resource page that contains sortable attributes.
     */
    Class<?> checkOrderByWithEntity() default DEFAULT.class;

    static final class DEFAULT {} 
    
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}