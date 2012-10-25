1. Apply all database changes:

	mvn liquibase:update


2. Generate SQL script for applying all database changes

	mvn liquibase:updateSQL

	
3. Create testdata:

	mvn -Dliquibase.changeLogFile=db/testdata/db.testdata.xml liquibase:update

	
4. Remove testdata

	mvn -Dliquibase.changeLogFile=db/testdata/db.testdata.xml -Dliquibase.rollbackCount=1000 liquibase:rollback
	