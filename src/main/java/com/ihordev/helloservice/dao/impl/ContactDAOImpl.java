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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.ihordev.helloservice.dao.ContactDAO;
import com.ihordev.helloservice.domain.Contact;
import com.ihordev.helloservice.model.ResourcePage;

@Repository
public class ContactDAOImpl extends GenericDAOImpl<Contact, Long> implements ContactDAO
{
    private static final Logger logger = LoggerFactory.getLogger(ContactDAOImpl.class);
    

    @Value("${ContactDAO.fetchSize}")
    private int fetchSize;

    public ContactDAOImpl()
    {
        super(Contact.class);
    }

    
    @Override
    public ResourcePage<Contact> findContactsUsingFilterPaginated(final String nameFilter, ResourcePage<Contact> resourcePage)
    {
        logger.debug("nameFilter param: " + nameFilter);
        
        final List<Contact> filteredContacts = new ArrayList<>();
        final Session session = getCurrentSession();
        
        final int[] totalResults = new int[1];
        final ResourcePage<Contact> resultPage = new ResourcePage<>(resourcePage);

        session.doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException
            {
                StatelessSession statelessSession = session.getSessionFactory().openStatelessSession(connection);

                String defaultOrderBy = "name";
                String defaultOrder = "ASC";
                String orderBy = (resultPage.getOrderBy() != null) ? resultPage.getOrderBy() : defaultOrderBy;
                String order = (resultPage.getOrder() != null) ? resultPage.getOrder().toString() : defaultOrder;
                
                String query = "from Contact contact order by contact." + orderBy + " " + order;
                
                ScrollableResults contacts = statelessSession.createQuery(query, Contact.class)
                        .setReadOnly(true).setFetchSize(fetchSize).scroll(ScrollMode.FORWARD_ONLY);

                Pattern p = Pattern.compile(nameFilter);
                int skippedResults = 0;
                
                while (contacts.next())
                {
                    Contact contact = (Contact) contacts.get(0);
                    Matcher m = p.matcher(contact.getName());
                    if (m.matches() == false)
                    {
                        totalResults[0]++;
                        if (skippedResults++ >= (resultPage.getPage() - 1) * resultPage.getLimit() 
                                && filteredContacts.size() < resultPage.getLimit())
                        {
                            filteredContacts.add(contact);
                        }
                    }
                }

                statelessSession.close();
            }

        });

        resultPage.setRecords(filteredContacts);
        resultPage.setTotalRecords(totalResults[0]);
        
        return resultPage;
    }

}
