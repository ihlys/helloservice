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

/**
 * Validate that the annotated string is a correct regular expression.
 * <p>
 * This annotation is used by {@link NameFilterValidator}.
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = NameFilterValidator.class)
@Documented
public @interface ValidNameFilter
{
    String message() default "{javax.validation.constraints.NameFilter.message}";
    
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    
}
