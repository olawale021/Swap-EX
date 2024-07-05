package com.example.models.exchanges;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ExchangeDAO {
    private static final Logger LOGGER = Logger.getLogger(ExchangeDAO.class.getName());
    private Connection connection;

    public ExchangeDAO(Connection connection) {
        this.connection = connection;
    }

    public void createExchange(ExchangeModel exchange) throws SQLException {
        String query = "INSERT INTO exchanges (item_id, owner_id, interested_user_id, status, created_at, updated_at, owner_username, interested_user_username, item_title) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, exchange.getItemId());
            stmt.setInt(2, exchange.getOwnerId());
            stmt.setInt(3, exchange.getInterestedUserId());
            stmt.setString(4, exchange.getStatus());
            stmt.setTimestamp(5, exchange.getCreatedAt());
            stmt.setTimestamp(6, exchange.getUpdatedAt());
            stmt.setString(7, exchange.getOwnerUsername());
            stmt.setString(8, exchange.getInterestedUserUsername());
            stmt.setString(9, exchange.getItemTitle());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    exchange.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<ExchangeModel> getExchangesByUserId(int userId) throws SQLException {
        String query = "SELECT e.*, o.username AS owner_username, iu.username AS interested_user_username, i.title AS item_title " +
                "FROM exchanges e " +
                "JOIN users o ON e.owner_id = o.id " +
                "JOIN users iu ON e.interested_user_id = iu.id " +
                "JOIN items i ON e.item_id = i.id " +
                "WHERE e.owner_id = ? OR e.interested_user_id = ?";
        List<ExchangeModel> exchanges = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            LOGGER.info("Executing query to get exchanges by user ID: " + userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ExchangeModel exchange = new ExchangeModel(
                        rs.getInt("id"),
                        rs.getInt("item_id"),
                        rs.getInt("owner_id"),
                        rs.getInt("interested_user_id"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("owner_username"),
                        rs.getString("interested_user_username"),
                        rs.getString("item_title")
                );
                LOGGER.info("Fetched exchange: " + exchange);
                exchanges.add(exchange);
            }
        }
        LOGGER.info("Fetched exchanges: " + exchanges.size());
        return exchanges;
    }

    public ExchangeModel getExchangeById(int id) throws SQLException {
        String query = "SELECT e.*, o.username AS owner_username, iu.username AS interested_user_username, i.title AS item_title " +
                "FROM exchanges e " +
                "JOIN users o ON e.owner_id = o.id " +
                "JOIN users iu ON e.interested_user_id = iu.id " +
                "JOIN items i ON e.item_id = i.id " +
                "WHERE e.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            LOGGER.info("Executing query to get exchange by ID: " + id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ExchangeModel exchange = new ExchangeModel(
                        rs.getInt("id"),
                        rs.getInt("item_id"),
                        rs.getInt("owner_id"),
                        rs.getInt("interested_user_id"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("owner_username"),
                        rs.getString("interested_user_username"),
                        rs.getString("item_title")
                );
                LOGGER.info("Fetched exchange: " + exchange);
                return exchange;
            } else {
                LOGGER.warning("No exchange found with ID: " + id);
                return null;
            }
        }
    }

    public List<ExchangeModel> getAllExchanges() throws SQLException {
        String query = "SELECT e.*, o.username AS owner_username, iu.username AS interested_user_username, i.title AS item_title " +
                "FROM exchanges e " +
                "JOIN users o ON e.owner_id = o.id " +
                "JOIN users iu ON e.interested_user_id = iu.id " +
                "JOIN items i ON e.item_id = i.id";
        List<ExchangeModel> exchanges = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            LOGGER.info("Executing query to get all exchanges");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ExchangeModel exchange = new ExchangeModel(
                        rs.getInt("id"),
                        rs.getInt("item_id"),
                        rs.getInt("owner_id"),
                        rs.getInt("interested_user_id"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("owner_username"),
                        rs.getString("interested_user_username"),
                        rs.getString("item_title")
                );
                LOGGER.info("Fetched exchange: " + exchange);
                exchanges.add(exchange);
            }
        }
        LOGGER.info("Fetched all exchanges: " + exchanges.size());
        return exchanges;
    }
}
