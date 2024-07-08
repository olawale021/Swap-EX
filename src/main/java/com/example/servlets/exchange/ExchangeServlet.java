package com.example.servlets.exchange;

import com.example.config.DBConnection;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebServlet("/ExchangeServlet")
public class ExchangeServlet extends HttpServlet {

    // Logger instance for logging purposes
    private static final Logger LOGGER = Logger.getLogger(ExchangeServlet.class.getName());

    // Handles POST requests to initiate an exchange and create an associated message
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve parameters from the request
        String itemIdParam = request.getParameter("itemId");
        String interestedUserIdParam = request.getParameter("interestedUserId");
        String content = request.getParameter("content");

        LOGGER.info("Received itemId: " + itemIdParam);
        LOGGER.info("Received interestedUserId: " + interestedUserIdParam);
        LOGGER.info("Received content: " + content);

        // Validate the input parameters
        if (itemIdParam == null || itemIdParam.isEmpty() ||
                interestedUserIdParam == null || interestedUserIdParam.isEmpty() ||
                content == null || content.isEmpty()) {
            // If any parameter is invalid, set an error message and forward to ItemDetails.jsp
            request.setAttribute("errorMessage", "Invalid input parameters. Please try again.");
            request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
            return;
        }

        // Convert itemId and interestedUserId to integers
        int itemId = Integer.parseInt(itemIdParam);
        int interestedUserId = Integer.parseInt(interestedUserIdParam);

        try (Connection connection = DBConnection.getConnection()) {
            // Fetch ownerId and itemTitle from the items table
            JSONObject itemDetails = getItemDetails(connection, itemId);
            if (itemDetails == null) {
                // If the item is not found, set an error message and forward to ItemDetails.jsp
                request.setAttribute("errorMessage", "Item not found.");
                request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
                return;
            }

            int ownerId = itemDetails.getInt("user_id");
            String itemTitle = itemDetails.getString("title");

            // Fetch usernames for both the owner and the interested user
            String ownerUsername = getUsernameById(connection, ownerId);
            String interestedUserUsername = getUsernameById(connection, interestedUserId);

            // Create an exchange entry in the exchanges table
            int exchangeId = createExchange(connection, itemId, interestedUserId, ownerId, ownerUsername, interestedUserUsername, itemTitle);

            // Create a message entry in the messages table associated with the exchange
            createMessage(connection, exchangeId, interestedUserId, ownerId, content);

            // Redirect to the MessageServlet to display the updated messages for this exchange
            response.sendRedirect("MessageServlet?exchangeId=" + exchangeId);
        } catch (SQLException e) {
            // Log any database errors that occur
            LOGGER.log(Level.SEVERE, "Database error", e);
            // Set an error message and forward to ItemDetails.jsp
            request.setAttribute("errorMessage", "An error occurred while processing your request. Please try again later.");
            request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
        }
    }

    // Fetches item details (owner ID and title) from the database for the given item ID
    private JSONObject getItemDetails(Connection connection, int itemId) throws SQLException {
        String query = "SELECT user_id, title FROM items WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    JSONObject itemDetails = new JSONObject();
                    itemDetails.put("user_id", rs.getInt("user_id"));
                    itemDetails.put("title", rs.getString("title"));
                    return itemDetails;
                }
            }
        }
        return null;
    }

    // Fetches the username for the given user ID from the users table
    private String getUsernameById(Connection connection, int userId) throws SQLException {
        String query = "SELECT username FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        }
        return null;
    }

    // Creates a new exchange entry in the exchanges table and returns the generated exchange ID
    private int createExchange(Connection connection, int itemId, int interestedUserId, int ownerId, String ownerUsername, String interestedUserUsername, String itemTitle) throws SQLException {
        String query = "INSERT INTO exchanges (item_id, interested_user_id, owner_id, owner_username, interested_user_username, item_title, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, itemId);
            stmt.setInt(2, interestedUserId);
            stmt.setInt(3, ownerId);
            stmt.setString(4, ownerUsername);
            stmt.setString(5, interestedUserUsername);
            stmt.setString(6, itemTitle);
            stmt.setString(7, "pending");
            stmt.executeUpdate();

            // Retrieve the generated exchange ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating exchange failed, no ID obtained.");
                }
            }
        }
    }

    // Creates a new message entry in the messages table for the given exchange
    private void createMessage(Connection connection, int exchangeId, int senderId, int receiverId, String content) throws SQLException {
        String query = "INSERT INTO messages (exchange_id, sender_id, receiver_id, content) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, exchangeId);
            stmt.setInt(2, senderId);
            stmt.setInt(3, receiverId);
            stmt.setString(4, content);
            stmt.executeUpdate();
        }
    }
}