package com.ihordev.helloservice.service;

import com.ihordev.helloservice.domain.Contact;
import com.ihordev.helloservice.model.ResourcePage;

/**
 * This is a Repository service interface for {@link Contact} entities.
 */
public interface ContactService
{

    /**
     * Retrieves a {@link ResourcePage} collection of {@link Contact} objects 
     * using regular expression filter. Resource page passed as parameter holds
     * paging information and returned page is a new instance with result data. 
     * 
     * @param   nameFilter      a string containing regular expression.
     * @param   resourcePage    a page that describes how many results must be retrieved, from which 
     *                          element starting, sort order and sort attribute.
     * @return                  a new instance that holds all information from passed parameter page and result data.
     */
    ResourcePage<Contact> findContactsUsingFilterPaginated(String nameFilter, ResourcePage<Contact> resourcePage);

}
