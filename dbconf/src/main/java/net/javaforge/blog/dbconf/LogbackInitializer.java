package net.javaforge.blog.dbconf;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.ByteArrayInputStream;

public class LogbackInitializer implements ServletContextListener {

    private static final Logger logger = LoggerFactory
            .getLogger(LogbackInitializer.class);

    private static final String logbackConfigSysProp = "app.logback.config";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        SLF4JBridgeHandler.install();
        String logbackConfig = System.getProperty(logbackConfigSysProp);

        if (logbackConfig == null || logbackConfig.isEmpty()) {
            sce.getServletContext().log(
                    "WARNING! No logback configuration found! Please configure '"
                            + logbackConfigSysProp + "' system property!");
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
                logger.info("Logback logger configured successfully...");

            } catch (JoranException e) {
                sce.getServletContext().log("Error configuring logback!", e);
            }

        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        logger.info("Destroying logback logger context...");

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.stop();

    }

}
