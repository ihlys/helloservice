package com.ihordev.helloservice.dao.impl;

import static org.junit.Assert.assertEquals;

import org.dbunit.dataset.IDataSet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ihordev.helloservice.dao.ContactDAO;
import com.ihordev.helloservice.domain.Contact;
import com.ihordev.helloservice.model.ResourcePage;

public class ContactDAOTest extends AbstractDAOTest
{

    @Autowired
    private ContactDAO contactDAO;

    @Override
    protected IDataSet getDataSet() throws Exception
    {
        return FlatXmlDataSetHelper.getDataSet("testdata/contactdaotest/data-source.xml",
                                               "testdata/contactdaotest/dataset.dtd");
    }

    @Test
    public void shouldReturnAllFilteredContacts() throws Exception
    {
        ResourcePage<Contact> contacts = contactDAO.findContactsUsingFilterPaginated("^.*[aei].*$", new ResourcePage<Contact>(1, 10));

        assertEquals(2, contacts.getTotalRecordsCount());
    }
    
    @Test
    public void shouldReturnPageOfFilteredContacts1() throws Exception
    {
        ResourcePage<Contact> contacts = contactDAO.findContactsUsingFilterPaginated("^A.*$", new ResourcePage<Contact>(1, 3));

        assertEquals(9, contacts.getTotalRecordsCount());
        assertEquals(3, contacts.getTotalPagesCount());
        assertEquals(3, contacts.getRecords().size());
    }
    
    @Test
    public void shouldReturnPageOfFilteredContacts2() throws Exception
    {
        ResourcePage<Contact> contacts = contactDAO.findContactsUsingFilterPaginated("^A.*$", new ResourcePage<Contact>(2, 5));

        assertEquals(9, contacts.getTotalRecordsCount());
        assertEquals(2, contacts.getTotalPagesCount());
        assertEquals(4, contacts.getRecords().size());
    }
    
    @Test
    public void shouldReturnPageOfFilteredContacts3() throws Exception
    {
        ResourcePage<Contact> contacts = contactDAO.findContactsUsingFilterPaginated("^A.*$", new ResourcePage<Contact>(5, 2));

        assertEquals(9, contacts.getTotalRecordsCount());
        assertEquals(5, contacts.getTotalPagesCount());
        assertEquals(1, contacts.getRecords().size());
    }
    
    
}
