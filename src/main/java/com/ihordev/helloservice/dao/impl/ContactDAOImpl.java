package com.ihordev.helloservice.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.ihordev.helloservice.dao.ContactDAO;
import com.ihordev.helloservice.domain.Contact;
import com.ihordev.helloservice.web.ResultsSizeException;

@Repository
public class ContactDAOImpl extends GenericDAOImpl<Contact, Long> implements ContactDAO {

	@Value("${ContactDAO.maxQueryResults}")
	private int maxQueryResults;

	@Value("${ContactDAO.fetchSize}")
	private int fetchSize;

	public ContactDAOImpl()
	{
		super(Contact.class);
	}
	

	@Override
	public List<Contact> findContactsUsingFilter(final String nameFilter)
	{
		final List<Contact> filteredContacts = new ArrayList<>();
		final Session session = getEntityManager().unwrap(Session.class);
		
		
		session.doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException
			{
				StatelessSession statelessSession = session.getSessionFactory().openStatelessSession(connection);
				   
				ScrollableResults contacts = statelessSession.createQuery("from Contact", Contact.class)
						.setReadOnly(true).setFetchSize(fetchSize).scroll(ScrollMode.FORWARD_ONLY);
				
				Pattern p = Pattern.compile(nameFilter);
				
				while (contacts.next()) {
				    Contact contact = (Contact) contacts.get(0);
				    Matcher m = p.matcher(contact.getName());
					if (m.matches() == false)
					{
						filteredContacts.add(contact);
						if (filteredContacts.size() > maxQueryResults) {
							throw new ResultsSizeException(filteredContacts.size());
						}
					}
				}
				 
				statelessSession.close();
			}
			
		});
		
		return filteredContacts;
	}
	
}
