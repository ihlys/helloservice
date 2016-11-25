package com.ihordev.helloservice.validation;

import java.lang.reflect.Field;
import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ihordev.helloservice.domain.SortAttribute;
import com.ihordev.helloservice.model.ResourcePage;

public class ResourcePageValidator implements ConstraintValidator<ValidResourcePage, ResourcePage<?>>
{
    private int maxPage;
    private int maxLimit;
    private ResourcePage.Order[] orders;
    private Class<?> checkGroupByWithEntity;
    
    @Override
    public void initialize(ValidResourcePage constraintAnnotation)
    {
        this.maxPage = constraintAnnotation.maxPage();
        this.maxLimit = constraintAnnotation.maxLimit();
        this.orders = constraintAnnotation.orders();
        this.checkGroupByWithEntity = constraintAnnotation.checkOrderByWithEntity();
    }

    @Override
    public boolean isValid(ResourcePage<?> value, ConstraintValidatorContext context)
    {
        if (value == null)
        {
            return true;
        } 
        
        boolean isValid = true;
        
        if (checkPageMin(value) == false)
        {
            isValid = false;
            addContraintViolation(context, "{ResourcePage.Page.Min}", "page");            
        } else if (checkPageMax(value) == false)
        {
            isValid = false;
            addContraintViolation(context, "{ResourcePage.Page.Max}", "page");
        }
        
        if (checkLimitMin(value) == false)
        {
            isValid = false;
            addContraintViolation(context, "{ResourcePage.Limit.Min}", "limit");
        } else if (checkLimitMax(value) == false)
        {
            isValid = false;
            addContraintViolation(context, "{ResourcePage.Limit.Max}", "limit");            
        }
        
        if (checkOrders(value) == false)
        {
            isValid = false;
            addContraintViolation(context, "{ResourcePage.Order}", "order");
        }
        
        if (checkIfAttributeIsSortable(value) == false)
        {
            isValid = false;
            addContraintViolation(context, "{ResourcePage.OrderBy}", "orderBy");            
        }
        
        return isValid;
        
    }

    private boolean checkPageMin(ResourcePage<?> value)
    {
        return value.getPage() > 0;    
    }
    
    private boolean checkPageMax(ResourcePage<?> value)
    {
        return value.getPage() <= maxPage;    
    }
    
    private boolean checkLimitMin(ResourcePage<?> value)
    {
        return value.getLimit() > 0;
    }
    
    private boolean checkLimitMax(ResourcePage<?> value)
    {
        return value.getLimit() <= maxLimit;
    }
    
    private boolean checkOrders(ResourcePage<?> value)
    {
        return value.getOrder() == null || Arrays.asList(orders).contains(value.getOrder());
    }
    
    private boolean checkIfAttributeIsSortable(ResourcePage<?> value)
    {
        if (value.getOrderBy() == null || checkGroupByWithEntity.equals(ValidResourcePage.DEFAULT.class))
        {
            return true;
        }
        
        for (Field field : checkGroupByWithEntity.getDeclaredFields())
        {
            if (field.isAnnotationPresent(SortAttribute.class))
            {
                if (field.getName().equals(value.getOrderBy()))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void addContraintViolation(ConstraintValidatorContext context, 
                                       String contraintTemplate, 
                                       String propertyNode)
    {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(contraintTemplate)
            .addPropertyNode(propertyNode)
            .addConstraintViolation();
    }
}
