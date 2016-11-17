package com.ihordev.helloservice.dao;

import java.util.List;

import com.ihordev.helloservice.domain.Contact;

/**
 * 
 * Data access object (DAO) interface for {@link com.ihordev.helloservice.domain.Contact} entities.
 *
 */
public interface ContactDAO extends GenericDAO<Contact, Long> {

	/**
	 * Retrieves {@link com.ihordev.helloservice.domain.Contact} list using regex expression filter.
	 * 
	 * @param nameFilter a string containing regex expression.
	 * @return a list of contacts
	 */
	List<Contact> findContactsUsingFilter(String nameFilter);
	
}
