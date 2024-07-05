package com.example.models.messages;

import com.example.config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageDAO {
    private Connection connection;

    public MessageDAO(Connection connection) {
        this.connection = connection;
    }

    private static final Logger LOGGER = Logger.getLogger(MessageDAO.class.getName());

    public void createMessage(MessageModel message) throws SQLException {
        String query = "INSERT INTO messages (exchange_id, sender_id, receiver_id, content, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, message.getExchangeId());
            stmt.setInt(2, message.getSenderId());
            stmt.setInt(3, message.getReceiverId());
            stmt.setString(4, message.getContent());
            stmt.setTimestamp(5, message.getCreatedAt());
            stmt.executeUpdate();
            LOGGER.info("Message created: " + message);
        }
    }

    public List<MessageModel> getMessagesByExchangeId(int exchangeId) throws SQLException {
        String query = "SELECT * FROM messages WHERE exchange_id = ?";
        List<MessageModel> messages = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, exchangeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new MessageModel(
                        rs.getInt("id"),
                        rs.getInt("exchange_id"),
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at")
                ));
            }
            LOGGER.info("Fetched messages for exchangeId " + exchangeId + ": " + messages.size());
        }
        return messages;
    }

    public List<MessageModel> getMessagesByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM messages WHERE sender_id = ? OR receiver_id = ?";
        List<MessageModel> messages = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new MessageModel(
                        rs.getInt("id"),
                        rs.getInt("exchange_id"),
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at")
                ));
            }
            LOGGER.info("Fetched messages for userId " + userId + ": " + messages.size());
        }
        return messages;
    }

    public MessageModel getMessageById(int id) throws SQLException {
        String query = "SELECT * FROM messages WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                MessageModel message = new MessageModel(
                        rs.getInt("id"),
                        rs.getInt("exchange_id"),
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at")
                );
                LOGGER.info("Fetched message by id: " + message);
                return message;
            }
        }
        return null;
    }

    public List<MessageModel> getAllMessages() throws SQLException {
        String query = "SELECT * FROM messages";
        List<MessageModel> messages = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                messages.add(new MessageModel(
                        rs.getInt("id"),
                        rs.getInt("exchange_id"),
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at")
                ));
            }
            LOGGER.info("Fetched all messages: " + messages.size());
        }
        return messages;
    }

    public void updateMessage(MessageModel message) throws SQLException {
        String query = "UPDATE messages SET exchange_id = ?, sender_id = ?, receiver_id = ?, content = ?, created_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, message.getExchangeId());
            stmt.setInt(2, message.getSenderId());
            stmt.setInt(3, message.getReceiverId());
            stmt.setString(4, message.getContent());
            stmt.setInt(5, message.getId());
            stmt.executeUpdate();
            LOGGER.info("Message updated: " + message);
        }
    }

    public void deleteMessage(int id) throws SQLException {
        String query = "DELETE FROM messages WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            LOGGER.info("Message deleted with id: " + id);
        }
    }
}
