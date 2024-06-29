package com.example.models.offers;


import com.example.config.DBConnection;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfferDAO {
    public void addOffer(OfferModel offer) throws SQLException {
        String query = "INSERT INTO offers (item_id, user_id, offer_details) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, offer.getItemId());
            stmt.setInt(2, offer.getUserId());
            stmt.setString(3, offer.getOfferDetails().toString());
            stmt.executeUpdate();
        }
    }

    public OfferModel getOfferById(int id) throws SQLException {
        String query = "SELECT * FROM offers WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new OfferModel(
                        rs.getInt("id"),
                        rs.getInt("item_id"),
                        rs.getInt("user_id"),
                        new JSONObject(rs.getString("offer_details")),
                        rs.getTimestamp("created_at")
                );
            }
        }
        return null;
    }

    public List<OfferModel> getAllOffers() throws SQLException {
        String query = "SELECT * FROM offers";
        List<OfferModel> offers = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                offers.add(new OfferModel(
                        rs.getInt("id"),
                        rs.getInt("item_id"),
                        rs.getInt("user_id"),
                        new JSONObject(rs.getString("offer_details")),
                        rs.getTimestamp("created_at")
                ));
            }
        }
        return offers;
    }

    public void updateOffer(OfferModel offer) throws SQLException {
        String query = "UPDATE offers SET item_id = ?, user_id = ?, offer_details = ?, created_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, offer.getItemId());
            stmt.setInt(2, offer.getUserId());
            stmt.setString(3, offer.getOfferDetails().toString());
            stmt.setInt(4, offer.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteOffer(int id) throws SQLException {
        String query = "DELETE FROM offers WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
