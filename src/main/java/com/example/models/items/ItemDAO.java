package com.example.models.items;

import com.example.config.DBConnection;
import com.example.models.category.CategoryModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.config.DBConnection.getConnection;

public class ItemDAO {
    private static final Logger LOGGER = Logger.getLogger(ItemDAO.class.getName());

    public int addItem(ItemModel item) throws SQLException {
        String sql = "INSERT INTO items (user_id, category_id, title, description, condition, features, photos, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, item.getUserId());
            pstmt.setInt(2, item.getCategoryId());
            pstmt.setString(3, item.getTitle());
            pstmt.setString(4, item.getDescription());
            pstmt.setString(5, item.getCondition());
            pstmt.setString(6, item.getFeatures());
            pstmt.setString(7, new JSONArray(item.getPhotos()).toString());
            pstmt.setTimestamp(8, item.getCreatedAt());
            pstmt.setTimestamp(9, item.getUpdatedAt());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating item failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating item failed, no ID obtained.");
                }
            }
        }
    }

    public ItemModel getItemById(int id) throws SQLException {
        String query = "SELECT items.*, categories.name as category_name FROM items JOIN categories ON items.category_id = categories.id WHERE items.id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                List<String> photosList = parsePhotos(rs.getString("photos"));
                String photosJson = convertPhotosListToJson(photosList);

                return new ItemModel(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("category_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getString("features"),
                        photosList,
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("category_name"),
                        photosJson
                );
            }
        }
        return null;
    }

    public List<ItemModel> getAllItems() throws SQLException {
        List<ItemModel> items = new ArrayList<>();
        String query = "SELECT items.*, categories.name as category_name FROM items JOIN categories ON items.category_id = categories.id";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                List<String> photosList = parsePhotos(rs.getString("photos"));
                String photosJson = convertPhotosListToJson(photosList);

                items.add(new ItemModel(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("category_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getString("features"),
                        photosList,
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("category_name"),
                        photosJson
                ));
            }
        }

        return items;
    }

    private List<String> parsePhotos(String photosString) {
        List<String> photosList = new ArrayList<>();
        try {
            JSONArray photosArray = new JSONArray(photosString);
            for (int i = 0; i < photosArray.length(); i++) {
                photosList.add(photosArray.getString(i));
            }
        } catch (JSONException e) {
            LOGGER.log(Level.SEVERE, "Invalid photos JSON format: " + photosString, e);
        }
        return photosList;
    }

    public boolean updateItem(ItemModel item) throws SQLException {
        String sql = "UPDATE items SET description = ?, condition = ?, category_id = ?, features = ?, photos = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getDescription());
            stmt.setString(2, item.getCondition());
            stmt.setInt(3, item.getCategoryId());
            stmt.setString(4, item.getFeatures());
            stmt.setString(5, convertPhotosListToJson(item.getPhotos()));
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(7, item.getId());

            LOGGER.info("Executing SQL: " + stmt.toString());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating item", e);
            throw e;
        }
    }

    public boolean deleteItem(int id) throws SQLException {
        String query = "DELETE FROM items WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // Return true if rows were affected (i.e., the item was deleted)
        }
    }


    public List<ItemModel> getItemsByUserId(int userId) throws SQLException {
        List<ItemModel> items = new ArrayList<>();
        String query = "SELECT items.*, categories.name as category_name FROM items JOIN categories ON items.category_id = categories.id WHERE items.user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Convert photos from JSONArray to List<String>
                String photosString = rs.getString("photos");

                List<String> photosList = new ArrayList<>();
                if (photosString != null && !photosString.isEmpty()) {
                    try {
                        JSONArray photosArray = new JSONArray(photosString);
                        for (int i = 0; i < photosArray.length(); i++) {
                            photosList.add(photosArray.getString(i));
                        }
                    } catch (JSONException e) {
                        LOGGER.log(Level.SEVERE, "Invalid JSON format in photos column for item ID: " + rs.getInt("id"), e);
                        // Handle the invalid JSON case, e.g., skip this entry or add default value
                    }
                }

                // Convert photosList to JSON string
                String photosJson = convertPhotosListToJson(photosList);

                items.add(new ItemModel(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("category_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getString("features"),
                        photosList,
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("category_name"),
                        photosJson
                ));
            }
        }
        return items;
    }

    public List<ItemModel> getAllItemsSortedByDate() throws SQLException {
        List<ItemModel> items = new ArrayList<>();
        String query = "SELECT items.*, categories.name as category_name FROM items JOIN categories ON items.category_id = categories.id ORDER BY items.created_at DESC";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                List<String> photosList = parsePhotos(rs.getString("photos"));
                String photosJson = convertPhotosListToJson(photosList);

                items.add(new ItemModel(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("category_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getString("features"),
                        photosList,
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("category_name"),
                        photosJson
                ));
            }
        }

        return items;
    }

    // Method to convert List<String> to JSON string
    private String convertPhotosListToJson(List<String> photosList) {
        try {
            return new ObjectMapper().writeValueAsString(photosList);
        } catch (JsonProcessingException e) {
            // Log the error and handle the exception as necessary
            e.printStackTrace();
            return "[]";
        }
    }
    public List<CategoryModel> getAllCategories() throws SQLException {
        List<CategoryModel> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";

        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                categories.add(new CategoryModel(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        }

        return categories;
    }

}
