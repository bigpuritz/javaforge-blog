package net.javaforge.blog.liquibase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestWithLiquibase {

	private static Connection conn;

	private static Liquibase liquibase;

	@BeforeClass
	public static void createTestData() throws SQLException,
			ClassNotFoundException, LiquibaseException {
		Class.forName("org.h2.Driver");
		conn = DriverManager.getConnection("jdbc:h2:liquibase-sample", "sa",
				"sa");

		Database database = DatabaseFactory.getInstance()
				.findCorrectDatabaseImplementation(new JdbcConnection(conn));

		liquibase = new Liquibase("db/testdata/db.testdata.xml",
				new FileSystemResourceAccessor(), database);
		liquibase.update(null);

	}

	@AfterClass
	public static void removeTestData() throws LiquibaseException, SQLException {
		liquibase.rollback(1000, null);
		conn.close();
	}

	@Test
	public void testUsers() throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			stmt = conn
					.prepareStatement("select count(*) as numberOfUsers from t_user");
			rs = stmt.executeQuery();
			rs.next();
			int numberOfUsers = rs.getInt("numberOfUsers");
			Assert.assertEquals(3, numberOfUsers);

		} finally {
			rs.close();
			stmt.close();
		}
	}

	@Test
	public void testRoles() throws SQLException {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			stmt = conn
					.prepareStatement("select count(*) as numberOfRoles from t_role");
			rs = stmt.executeQuery();
			rs.next();
			int numberOfUsers = rs.getInt("numberOfRoles");
			Assert.assertEquals(3, numberOfUsers);

		} finally {
			rs.close();
			stmt.close();
		}
	}

}
