package net.javaforge.blog.dbconf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

/**
 * Loads application configuration from the database
 */
public class DatabaseAwareApplicationPropertiesInitializer extends
		ApplicationPropertiesInitializer {

	private static final String DATASOURCE_KEY = "dbconf.app.ds";

	private static final String DATASOURCE_FALLBACK_VALUE = "jdbc/dbconf-app-ds";

	private static final String LOAD_CONFIG_QUERY = "select key, value from t_dbconf_app_props";

	@Override
	protected Properties loadProperties() {
		return loadProperties(resolveDataSourceName());
	}

	/**
	 * Default implementation resolves the name of the data source from the
	 * system property. You can override this method and resolve the name from
	 * other sources (e.g. webapp context parameter, property file, environment,
	 * ...)
	 * 
	 * @return name of the data source to use
	 */
	protected String resolveDataSourceName() {
		return System.getProperty(DATASOURCE_KEY, DATASOURCE_FALLBACK_VALUE);
	}

	/**
	 * Initialises {@link Properties} instance from the given data source.
	 * 
	 * @param dataSourceName
	 *            is a name of the data source to use
	 * @return initialised properties
	 */
	private Properties loadProperties(String dataSourceName) {

		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		DataSource ds = lookup.getDataSource(dataSourceName);

		final Properties props = new Properties();
		new JdbcTemplate(ds).query(LOAD_CONFIG_QUERY, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				props.setProperty(rs.getString("key"), rs.getString("value"));
			}
		});

		// additionally we have to add data source name itself to the properties
		props.setProperty(DATASOURCE_KEY, dataSourceName);
		return props;
	}

}
