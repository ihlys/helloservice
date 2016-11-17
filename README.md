[![CircleCI](https://circleci.com/gh/ihlys/helloservice.svg?style=svg)](https://circleci.com/gh/ihlys/helloservice)

The application works in Tomcat environment http://tomcat.apache.org/ and uses JNDI lookup for java.sql.DataSource.
The server must provide initial context with following resource: "jdbc/H2Database".

Example Tomcat configuration in %CATALINA_HOME%\conf\server.xml:
```xml
  <Resource name="jdbc/H2Database" auth="Container"
      global="jdbc/H2Database"
      factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
      type="javax.sql.DataSource"
      username="sa"
      password=""
      driverClassName="org.h2.Driver"
      description="Local H2 data base"
      url="jdbc:h2:tcp://localhost/~/test"
      maxTotal="10"
      maxIdle="10"
      maxWaitMillis="10000"
      removeAbandonedTimeout="300"           
      defaultAutoCommit="true" />
```
      
DataSource presented by H2 lightweight database: http://www.h2database.com. The class 'org.h2.Driver' which is located in h2.jar library must be presented on Tomcat classpath. 
To generate example schema and data you can use data population sql scripts generation utility in h2script folder.
