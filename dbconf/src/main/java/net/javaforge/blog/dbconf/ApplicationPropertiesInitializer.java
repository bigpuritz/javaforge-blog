package net.javaforge.blog.dbconf;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public abstract class ApplicationPropertiesInitializer implements
		ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final ServletContext ctx = sce.getServletContext();
		ctx.log("*************************************************************");
		ctx.log("**                    app startup                          **");
		ctx.log("*************************************************************");

		ApplicationProperties.get().putAll(loadProperties());
	}

	/**
	 * Loads properties from the desired data source (e.g. database, LDAP,...)
	 * 
	 * @return initialised properties instance
	 */
	protected abstract Properties loadProperties();

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		final ServletContext ctx = sce.getServletContext();
		ctx.log("*************************************************************");
		ctx.log("**                    app shutdown                         **");
		ctx.log("*************************************************************");
	}

}
