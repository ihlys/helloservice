package com.ihordev.helloservice.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ihordev.helloservice.dao.ContactDAO;
import com.ihordev.helloservice.domain.Contact;
import com.ihordev.helloservice.web.ResultsSizeException;

public class ContactDAOTest extends AbstractDAOTest
{

	@Autowired
	private ContactDAO contactDAO;
	
	@Override
	protected IDataSet getDataSet() throws Exception
	{
		return FlatXmlDataSetHelper.getDataSet("testdata/contactdaotest/data-source.xml", "testdata/contactdaotest/dataset.dtd");
	}
	
	
	@Test
	public void shouldReturnFilteredContacts() throws Exception
	{
		List<Contact> contacts = contactDAO.findContactsUsingFilter("^.*[aei].*$");
		
		assertEquals(2, contacts.size());
	}

	
	@Test(expected = ResultsSizeException.class)
	public void shouldThrowExceptionIfThereAreTooManyResults() throws Exception
	{
		contactDAO.findContactsUsingFilter("^A.*$");
	}
}
