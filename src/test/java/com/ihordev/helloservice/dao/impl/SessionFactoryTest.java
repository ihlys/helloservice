package com.ihordev.helloservice.dao.impl;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ihordev.helloservice.config.TestDataConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataConfig.class)
@TestPropertySource("classpath:properties/test-dbconfig.properties")
@ActiveProfiles("db-test")
public class SessionFactoryTest
{
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void shouldLoadSessionFactory()
    {
        Assert.assertNotNull(sessionFactory);
    }
}
