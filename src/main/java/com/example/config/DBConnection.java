package com.example.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final Dotenv dotenv = Dotenv.configure().load();

    private static final String jdbcUrl = dotenv.get("DB_URL");

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            LOGGER.info("SQLite JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "SQLite JDBC driver not found", e);
            throw new ExceptionInInitializerError("SQLite JDBC driver not found");
        }
    }

    public static Connection getConnection() throws SQLException {
        if (jdbcUrl == null) {
            throw new SQLException("Database connection details are not set. Please check your .env file.");
        }
        LOGGER.info("JDBC URL: " + jdbcUrl);
        return DriverManager.getConnection(jdbcUrl);
    }

    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            LOGGER.info("Connected to the database!");
            connection.close();  // Close the connection after use
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to the database.", e);
        }
    }
}

