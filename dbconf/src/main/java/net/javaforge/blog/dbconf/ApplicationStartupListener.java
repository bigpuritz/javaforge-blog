package net.javaforge.blog.dbconf;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationStartupListener implements ServletContextListener {

    private static final String APP_DATASOURCE_SYSPROP = "dbconf.app.ds";

    private static final String APP_DATASOURCE_FALLBACK_VALUE = "jdbc/dbconf-app-ds";

    private static final String APP_PROPS_QUERY = "select key, value from t_dbconf_app_props";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        final ServletContext ctx = sce.getServletContext();

        ctx.log("*************************************************************");
        ctx.log("**                   dbconf app startup                    **");
        ctx.log("*************************************************************");

        String dataSourceName = System.getProperty(APP_DATASOURCE_SYSPROP,
                APP_DATASOURCE_FALLBACK_VALUE);

        System.setProperty(APP_DATASOURCE_SYSPROP, dataSourceName);

        ctx.log("Loading application properties from datasource: "
                + dataSourceName);

        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        DataSource ds = lookup.getDataSource(dataSourceName);

        new JdbcTemplate(ds).query(APP_PROPS_QUERY, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                System.setProperty(rs.getString("key"), rs.getString("value"));
            }
        });

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
