create table t_dbconf_app_props(key varchar2 (100) not null, value varchar2(1000) not null) ;

insert into t_dbconf_app_props ( key , value ) values ('app.name', 'dbconf');
insert into t_dbconf_app_props ( key , value ) values ('app.version', '0.0.1');
insert into t_dbconf_app_props ( key , value ) values ('app.logback.config', '<?xml version="1.0" encoding="UTF-8"?>
<configuration>
	<contextListener class="ch.qos.logback.classic.jul.LevelChangePropagator">
		<resetJUL>true</resetJUL>
	</contextListener>
	<appender name="console" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
		</encoder>
	</appender>
	<root level="info">
		<appender-ref ref="console" />
	</root>
</configuration>');