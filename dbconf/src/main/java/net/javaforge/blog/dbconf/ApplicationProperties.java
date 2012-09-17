package net.javaforge.blog.dbconf;

import java.util.Properties;

/**
 * Class holding application properties.
 */
public final class ApplicationProperties extends Properties {

	private static final long serialVersionUID = 1L;

	private static ApplicationProperties instance;

	private ApplicationProperties() {
	}

	public String getLogbackConfiguration() {
		return this.getProperty("app.logback.config");
	}

	public static final ApplicationProperties get() {
		if (instance == null)
			instance = new ApplicationProperties();

		return instance;
	}

}
