package com.ihordev.helloservice.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ihordev.helloservice.dao.impl.DAOmarker;
import com.ihordev.helloservice.service.impl.ServiceMarker;

@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:properties/app-dbconfig.properties", 
				 "classpath:properties/app-datalayer-config.properties"})
@ComponentScan(basePackageClasses = {DAOmarker.class, ServiceMarker.class})
public class RootConfig
{

	@Autowired
	private Environment env;
	
	@Bean
	@Profile("production")
	public DataSource dataSource()
	{
		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		lookup.setResourceRef(true);
		DataSource dataSource = lookup.getDataSource("jdbc/H2Database");

		return dataSource;
	}

	@Bean
	@Profile({"production", "db-test"})
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource)
	{
		LocalContainerEntityManagerFactoryBean lcemf = new LocalContainerEntityManagerFactoryBean();
		lcemf.setDataSource(dataSource);
		
		Properties props = new Properties();
		props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		props.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		props.setProperty("hibernate.default_schema", env.getProperty("hibernate.default_schema"));
		props.setProperty("hibernate.connection.isolation", env.getProperty("hibernate.connection.isolation"));
		
		lcemf.setJpaProperties(props);
		
		return lcemf;
	} 
	
	@Bean
	@Profile({"production", "db-test"})
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
	    JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
	    return transactionManager;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer(){
		return new PropertySourcesPlaceholderConfigurer();
	}
	
}
