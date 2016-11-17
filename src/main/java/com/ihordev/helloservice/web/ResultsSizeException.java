package com.ihordev.helloservice.web;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 
 * This is exception for request that has too large response size.
 *
 */
public class ResultsSizeException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	private static final String ERROR_CODE = "ResultsSizeError";
	
	private int resultsSize;
	
	public ResultsSizeException(int resultsSize)
	{
		this.resultsSize = resultsSize;
	}

	@Override
	public String getMessage()
	{
		Locale locale = new Locale.Builder().setLanguage("en").setRegion("EN").build();
		return ResourceBundle.getBundle("properties.i18n.messages.messages", locale).getString(ERROR_CODE) + ": > " + resultsSize;
	}
	
	/**
	 * Returns message details in localized form according to current locale.
	 */
	@Override
	public String getLocalizedMessage()
	{
		Locale locale = LocaleContextHolder.getLocale();
		return ResourceBundle.getBundle("properties.i18n.messages.messages", locale).getString(ERROR_CODE) + ": > " + resultsSize;
	}
}
