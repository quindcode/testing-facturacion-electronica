package co.com.fe.api.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class DatabaseConnector {
    private static Connection connection;
    private static final Dotenv dotenv = Dotenv.load();
    private static final String HOST = dotenv.get("POSTGRES_DB_HOST");
    private static final String DB_NAME = dotenv.get("POSTGRES_DB_NAME");
    private static final String USERNAME = dotenv.get("POSTGRES_DB_USERNAME");
    private static final String PASSWORD = dotenv.get("POSTGRES_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:postgresql://" + HOST + "/" + DB_NAME;
            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
            connection.setReadOnly(true);
            System.out.println("Conexión exitosa a Postgresql!");
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Conexión cerrada.");
        }
    }
}
