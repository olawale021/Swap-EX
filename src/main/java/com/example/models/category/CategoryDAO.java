package com.example.models.category;

import com.example.config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    public void addCategory(CategoryModel category) throws SQLException {
        String query = "INSERT INTO categories (name) VALUES (?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            stmt.executeUpdate();
        }
    }

    public CategoryModel getCategoryById(int id) throws SQLException {
        String query = "SELECT * FROM categories WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new CategoryModel(
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }
        }
        return null;
    }

    public List<CategoryModel> getAllCategories() throws SQLException {
        String query = "SELECT * FROM categories";
        List<CategoryModel> categories = new ArrayList<>();
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

    public void updateCategory(CategoryModel category) throws SQLException {
        String query = "UPDATE categories SET name = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteCategory(int id) throws SQLException {
        String query = "DELETE FROM categories WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
