package com.ihordev.helloservice.validation;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameFilterValidator implements ConstraintValidator<ValidNameFilter, String>
{

    @Override
    public void initialize(ValidNameFilter constraintAnnotation)
    {

    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext)
    {
        if (object == null)
        {
            return true;
        }

        try
        {
            Pattern.compile(object);
        } catch (PatternSyntaxException ex)
        {
            return false;
        }

        return true;
    }

}
