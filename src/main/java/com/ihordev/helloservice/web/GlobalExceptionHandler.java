package com.ihordev.helloservice.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ihordev.helloservice.model.ResourcePage;

@ControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;
    
    private Locale systemLocale;
    
    public GlobalExceptionHandler()
    {
        
    }
    
    public GlobalExceptionHandler(@Value("${systemLocaleLanguage}") String systemLocaleLanguage, 
                                  @Value("${systemLocaleCountry}") String systemLocaleCountry)
    {
        super();
        this.systemLocale = new Locale(systemLocaleLanguage, systemLocaleCountry);
    }
    
    private String getDefaultMessage(String errorCode)
    {
        return messageSource.getMessage(errorCode, null, systemLocale);
    }
    
    private String getLocalizedMessage(String errorCode)
    {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorCode, null, locale);
    }
    
    
    
    
    
    private static final String WRONG_RESOURCE_PAGE_CODE = "WrongResourcePage";
    
    @ExceptionHandler(WrongResourcePageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(WrongResourcePageException ex)
    {
        int wrongPage = ex.getWrongPage();
        int totalPages = ex.getTotalPagesCount();
        String defaultMessage = createWrongPageMessage(getDefaultMessage(WRONG_RESOURCE_PAGE_CODE), wrongPage, totalPages);
        String localizedMessage = createWrongPageMessage(getLocalizedMessage(WRONG_RESOURCE_PAGE_CODE), wrongPage, totalPages);

        logger.error(defaultMessage, ex);
        
        Map<String, String> errors = new HashMap<>();
        errors.put(getPath(ex, ResourcePage.class, "page"), localizedMessage);
        
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors);
        
        return errorResponse;
    }
    
    private String createWrongPageMessage(String baseMessage, int wrongPage, int totalPages)
    {
        return baseMessage + ": page=" + wrongPage + ", totalPages=" + totalPages;
    }
    
    private String getPath(Exception ex, Class<?> cls, String property)
    {
        String throwPoint = ex.getStackTrace()[0].getMethodName();
        
        char chrs[] = cls.getSimpleName().toCharArray();
        chrs[0] = Character.toLowerCase(chrs[0]);
        String targetClass = new String(chrs);
        
        return throwPoint + "." + targetClass + "." + property;
    }
    
    
    
    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(ConstraintViolationException ex)
    {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        
        logger.error("Request parameter is invalid: ", new ConstraintViolationException(createLogConstraintErrorMessages(violations).toString(), violations));
               
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), createConstraintErrorMessages(violations));
        
        return errorResponse;
    }
    
    private Map<String, String> createConstraintErrorMessages(Set<ConstraintViolation<?>> violations)
    {
        Map<String, String> errors = new HashMap<>();
        
        for (ConstraintViolation<?> constraintViolation : violations)
        {
            errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        }
        
        return errors;
    }
    
    private List<String> createLogConstraintErrorMessages(Set<ConstraintViolation<?>> violations)
    {
        List<String> errors = new ArrayList<>();
        
        for (ConstraintViolation<?> constraintViolation : violations)
        {
            String logMessage = "Validation constraint violation: "
                    + "["
                    + "constraint = "    + constraintViolation.getConstraintDescriptor().getAnnotation().annotationType() + 
                    ", invalid value = " + constraintViolation.getInvalidValue().toString() + 
                    ", property = "      + constraintViolation.getPropertyPath().toString() + 
                    ", rootBeanClass = " + constraintViolation.getRootBeanClass()
                    + "]";
            errors.add(logMessage);
        }
        
        return errors;
    }
    
    
    
    
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(BindException exc)
    {
        logger.error("Request parameter is invalid: ", exc);
               
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), createBindingErrorMessages(exc));
        
        return errorResponse;
    }

    private Map<String, String> createBindingErrorMessages(BindException ex)
    {
        Map<String, String> errors = new HashMap<>();
        
        BindingResult results = ex.getBindingResult();
        
        for (FieldError error : results.getFieldErrors())
        {
            errors.put(error.getObjectName() + "." + error.getField(), messageSource.getMessage(error, LocaleContextHolder.getLocale()));
        }
        
        return errors;
    }
    
    
    
    
    private static final String MISSING_REQUEST_PARAMETER_CODE = "MissingRequestParameterError";
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(MissingServletRequestParameterException ex)
    {
        logger.error("Required request parameter is missing:", ex);
        
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getParameterName(), getLocalizedMessage(MISSING_REQUEST_PARAMETER_CODE));
        
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors);

        return errorResponse;
    }   

    
    
    
    private static final String UNHANDLED_ERROR_CODE = "ServerInternalError";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception ex)
    {
        logger.error("Unhandled exception: ", ex);

        Map<String, String> errors = new HashMap<>();
        errors.put(UNHANDLED_ERROR_CODE, getLocalizedMessage(UNHANDLED_ERROR_CODE));
        
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors);

        return errorResponse;
    }
    
}
