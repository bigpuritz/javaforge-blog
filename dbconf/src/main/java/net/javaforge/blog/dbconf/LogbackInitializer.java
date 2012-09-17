package net.javaforge.blog.dbconf;

import java.io.ByteArrayInputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LogbackInitializer implements ServletContextListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(LogbackInitializer.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		SLF4JBridgeHandler.install();

		String logbackConfig = ApplicationProperties.get()
				.getLogbackConfiguration();

		if (logbackConfig == null || logbackConfig.isEmpty()) {
			sce.getServletContext()
					.log("WARNING! No logback configuration found! "
							+ "Please configure 'app.logback.config' configuration property!");
		} else {

			LoggerContext context = (LoggerContext) LoggerFactory
					.getILoggerFactory();
			try {

				JoranConfigurator configurator = new JoranConfigurator();
				configurator.setContext(context);
				// Call context.reset() to clear any previous configuration,
				// e.g. default configuration. For multi-step configuration,
				// omit calling context.reset().
				context.reset();
				configurator.doConfigure(new ByteArrayInputStream(logbackConfig
						.getBytes()));

				StatusPrinter.printInCaseOfErrorsOrWarnings(context);
				LOG.info("Logback logger configured successfully...");

			} catch (JoranException e) {
				sce.getServletContext().log("Error configuring logback!", e);
			}

		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

		LOG.info("Destroying logback logger context...");

		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		lc.stop();

	}

}
