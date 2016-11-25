package com.ihordev.helloservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ihordev.helloservice.dao.ContactDAO;
import com.ihordev.helloservice.domain.Contact;
import com.ihordev.helloservice.model.ResourcePage;
import com.ihordev.helloservice.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService
{

    @Autowired
    private ContactDAO contactDAO;

    @Override
    @Transactional(readOnly = true)
    public ResourcePage<Contact> findContactsUsingFilterPaginated(String nameFilter, ResourcePage<Contact> resourcePage)
    {
        return contactDAO.findContactsUsingFilterPaginated(nameFilter, resourcePage);
    }

}
