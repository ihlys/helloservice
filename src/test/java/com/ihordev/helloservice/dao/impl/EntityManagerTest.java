package com.ihordev.helloservice.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.ihordev.helloservice.config.TestRootConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestRootConfig.class, loader = AnnotationConfigContextLoader.class)
@TestPropertySource("classpath:properties/test-dbconfig.properties")
@ActiveProfiles("db-test")
public class EntityManagerTest
{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	public void shouldProvideEntityManager()
	{
		Assert.assertNotNull(entityManager);
	}
}
