package com.ihordev.helloservice.web;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.MissingServletRequestParameterException;

/**
 * 
 * This is exception for requests with missing required parameters.
 * It is used to replace {@link org.springframework.web.bind.MissingServletRequestParameterException}
 * for convenience to return localized messages.
 * 
 */
public class MissingRequestParameterException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	private static final String ERROR_CODE = "MissingRequestParameterError";

	private String parameterName;
	
	public MissingRequestParameterException(MissingServletRequestParameterException e)
	{
		this(e.getParameterName());
	}
	
	public MissingRequestParameterException(String parameterName)
	{
		this.parameterName = parameterName;
	}

	@Override
	public String getMessage() 
	{
		Locale locale = new Locale.Builder().setLanguage("en").setRegion("EN").build();
		return ResourceBundle.getBundle("properties.i18n.messages.messages", locale).getString(ERROR_CODE) + ": " + parameterName;
	}
	
	/**
	 * Returns message details in localized form according to current locale.
	 */
	@Override
	public String getLocalizedMessage()
	{
		Locale locale = LocaleContextHolder.getLocale();
		return ResourceBundle.getBundle("properties.i18n.messages.messages", locale).getString(ERROR_CODE) + ": " + parameterName;
	}
}
