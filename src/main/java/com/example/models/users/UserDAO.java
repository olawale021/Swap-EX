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
            stmt.setString(2, user.getPhoneNumber());  // Store phone number as string
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getAddress().toString());
            stmt.executeUpdate();
        }
    }

    public UserModel getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("phone_number"),  // Retrieve phone number as string
                        rs.getString("password"),
                        new JSONObject(rs.getString("address"))
                );
            }
        }
        return null;
    }

    public UserModel getUserByUsernameAndPassword(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("phone_number"),  // Retrieve phone number as string
                        rs.getString("password"),
                        new JSONObject(rs.getString("address"))
                );
            }
        }
        return null;
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
                        rs.getString("phone_number"),  // Retrieve phone number as string
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
                        rs.getString("phone_number"),  // Retrieve phone number as string
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
            stmt.setString(2, user.getPhoneNumber());  // Store phone number as string
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
