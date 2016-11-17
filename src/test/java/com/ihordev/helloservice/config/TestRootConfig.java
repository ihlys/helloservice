package com.ihordev.helloservice.config;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@Import(RootConfig.class)
public class TestRootConfig
{

	@Autowired
	private Environment env;
	
	@Bean
	@Profile("db-test")
	public DataSource testDataSource()
	{
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL(env.getProperty("url"));
		ds.setUser(env.getProperty("user"));
		ds.setPassword(env.getProperty("password"));
		
        return ds;
	}
	
}
