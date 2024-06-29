package com.example.models.users;

import com.example.config.DBConnection;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());


    public void addUser(UserModel user) throws SQLException {
        String query = "INSERT INTO users (username, phone_number, password, address) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setInt(2, user.getPhoneNumber());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getAddress().toString());
            stmt.executeUpdate();
        }
    }

    public UserModel getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        LOGGER.info("Preparing statement: " + query + " with username: " + username);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set the username parameter in the prepared statement
            stmt.setString(1, username);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            // Check if a result is returned
            if (rs.next()) {
                LOGGER.info("User found: " + rs.getString("username"));
                // Create and return the UserModel object from the result set
                return new UserModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("phone_number"),
                        rs.getString("password"),
                        new JSONObject(rs.getString("address"))
                );
            } else {
                LOGGER.info("No user found with username: " + username);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception", e);
            throw e;
        }
        // Return null if no user is found
        return null;
    }

    public UserModel getUserByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        LOGGER.info("Preparing statement: " + sql + " with username: " + username + " and password: " + password);
        UserModel user = null;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                LOGGER.info("User found: " + resultSet.getString("username"));
                user = new UserModel(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getInt("phone_number"),
                        resultSet.getString("password"),
                        new JSONObject(resultSet.getString("address"))
                );
            } else {
                LOGGER.info("No user found with username: " + username + " and password: " + password);
            }
        }

        return user;
    }


    public UserModel getUserById(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("phone_number"),
                        rs.getString("password"),
                        new JSONObject(rs.getString("address"))
                );
            }
        }
        return null;
    }

    public List<UserModel> getAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        List<UserModel> users = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new UserModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("phone_number"),
                        rs.getString("password"),
                        new JSONObject(rs.getString("address"))
                ));
            }
        }
        return users;
    }

    public void updateUser(UserModel user) throws SQLException {
        String query = "UPDATE users SET username = ?, phone_number = ?, password = ?, address = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setInt(2, user.getPhoneNumber());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getAddress().toString());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

