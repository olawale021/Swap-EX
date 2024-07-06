package com.example.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ExportToArff {
    public static void main(String[] args) {
        String query = "SELECT c.name AS item_category, i.description AS item_name, i.features AS item_features " +
                "FROM items i JOIN categories c ON i.category_id = c.id";
        String arffFilePath = "/Users/olawale/Desktop/javacs/MangoEX/src/main/resources/items.arff";  // Update this with the desired output path

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             FileWriter writer = new FileWriter(arffFilePath)) {

            // Collect all unique categories
            Set<String> categories = new HashSet<>();
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    categories.add(rs.getString("item_category"));
                }
            }

            // Re-query to reset the ResultSet
            try (ResultSet rs = stmt.executeQuery(query)) {
                // Write ARFF header
                writer.write("@relation items\n\n");
                writer.write("@attribute item_category {" + String.join(", ", categories) + "}\n");
                writer.write("@attribute item_name string\n");
                writer.write("@attribute item_features string\n\n");
                writer.write("@data\n");

                // Write data
                while (rs.next()) {
                    String itemCategory = rs.getString("item_category");
                    String itemName = rs.getString("item_name");
                    String itemFeatures = rs.getString("item_features");

                    writer.write(String.format("%s, '%s', '%s'\n", itemCategory, itemName, itemFeatures));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


