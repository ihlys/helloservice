package com.ihordev.helloservice.web;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler
{	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(MissingServletRequestParameterException ex) {
    	MissingRequestParameterException exc = new MissingRequestParameterException(ex);
    	
    	logger.error("Required request parameter is missing:", exc);
    	
    	ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getLocalizedMessage());
    	
        return errorResponse;
    }
    
    @ExceptionHandler(InvalidRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(InvalidRequestParameterException ex) {
    	logger.error("Request parameter is invalid: ", ex);
    	
    	ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage());
    	
        return errorResponse;
    }
	
    @ExceptionHandler(ResultsSizeException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ResponseBody
    public ErrorResponse handleException(ResultsSizeException ex) {
    	logger.error("Request result size is too large: ", ex);
    	
    	ErrorResponse errorResponse = new ErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE.value(), ex.getLocalizedMessage());
    	
        return errorResponse;
    }
    
	private static final String UNHANDLED_ERROR_CODE = "ServerInternalError";
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception ex) 
    {
    	logger.error("Unhandled exception: ", ex);
    	
    	Locale locale = LocaleContextHolder.getLocale();
    	String messageLocalized = ResourceBundle.getBundle("properties.i18n.messages.messages", locale).getString(UNHANDLED_ERROR_CODE);
    	
    	ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), messageLocalized);
    	
        return errorResponse;
    }

}
