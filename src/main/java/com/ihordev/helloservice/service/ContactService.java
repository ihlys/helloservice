package com.ihordev.helloservice.service;

import java.util.List;

import com.ihordev.helloservice.domain.Contact;

/**
 * 
 * Repository service interface for {@link com.ihordev.helloservice.domain.Contact} entities.
 *
 */
public interface ContactService {

	
	/**
	 * Retrieves {@link com.ihordev.helloservice.domain.Contact} list using regex expression filter.
	 * 
	 * @param nameFilter a string containing regex expression.
	 * @return a list of contacts
	 */
	List<Contact> findContactsUsingFilter(String nameFilter);
	
}
