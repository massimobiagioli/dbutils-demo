package it.demo.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import it.demo.dbutils.model.Person;

public class App {
	
	private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";
	
	public static void main(String[] args) {
		try {
			insertWithPreparedStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void insertWithPreparedStatement() throws SQLException {
		Connection connection = getDBConnection();
		PreparedStatement createPreparedStatement = null;
		PreparedStatement insertPreparedStatement = null;
		PreparedStatement selectPreparedStatement = null;

		String CreateQuery = "CREATE TABLE PERSON(id int primary key, name varchar(255))";
		String InsertQuery = "INSERT INTO PERSON" + "(id, name) values" + "(?,?)";
		String SelectQuery = "select * from PERSON";

		try {
			connection.setAutoCommit(false);
			
			// Esempio dal sito: http://www.javatips.net/blog/h2-in-memory-database-example
			
			createPreparedStatement = connection.prepareStatement(CreateQuery);
			createPreparedStatement.executeUpdate();
			createPreparedStatement.close();

			insertPreparedStatement = connection.prepareStatement(InsertQuery);
			insertPreparedStatement.setInt(1, 1);
			insertPreparedStatement.setString(2, "Jose");
			insertPreparedStatement.executeUpdate();
			insertPreparedStatement.close();

			selectPreparedStatement = connection.prepareStatement(SelectQuery);
			ResultSet rs = selectPreparedStatement.executeQuery();
			System.out.println("H2 In-Memory Database inserted through PreparedStatement");
			while (rs.next()) {
				System.out.println("Id " + rs.getInt("id") + " Name " + rs.getString("name"));
			}
			selectPreparedStatement.close();

			connection.commit();
			
			// Lettura attraverso libreria Apache DbUtils
			System.out.println("*** Accesso ai dati attraverso libreria DbUtils ***");
			
			DbUtils.loadDriver(DB_DRIVER);
			QueryRunner runner = new QueryRunner();
			List<Person> personList = (List<Person>) runner.query(connection, SelectQuery, new BeanListHandler<>(Person.class));
			
			for (Person person : personList) {
				System.out.println(person);
			}
			
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}
}
