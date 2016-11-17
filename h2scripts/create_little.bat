
java -cp ..\h2-1.3.176.jar org.h2.tools.RunScript -user sa -url jdbc:h2:tcp://localhost/~/test -script create_schema.sql
java -cp ..\h2-1.3.176.jar org.h2.tools.RunScript -user sa -url jdbc:h2:tcp://localhost/~/test -script pop_schema.sql

PAUSE