package com.example.servlets;

import com.example.config.DBConnection;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.stream.Collectors;

@WebServlet("/ExchangeServlet")
public class ExchangeServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ExchangeServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemIdParam = request.getParameter("itemId");
        String interestedUserIdParam = request.getParameter("interestedUserId");
        String content = request.getParameter("content");

        LOGGER.info("Received itemId: " + itemIdParam);
        LOGGER.info("Received interestedUserId: " + interestedUserIdParam);
        LOGGER.info("Received content: " + content);

        if (itemIdParam == null || itemIdParam.isEmpty() ||
                interestedUserIdParam == null || interestedUserIdParam.isEmpty() ||
                content == null || content.isEmpty()) {
            request.setAttribute("errorMessage", "Invalid input parameters. Please try again.");
            request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
            return;
        }

        int itemId = Integer.parseInt(itemIdParam);
        int interestedUserId = Integer.parseInt(interestedUserIdParam);

        try {
            // Fetch ownerId and itemTitle from the items table
            JSONObject itemDetails = getItemDetails(itemId);
            if (itemDetails == null) {
                request.setAttribute("errorMessage", "Item not found.");
                request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
                return;
            }

            int ownerId = itemDetails.getInt("user_id");
            String itemTitle = itemDetails.getString("title");

            // Fetch usernames
            String ownerUsername = getUsernameById(ownerId);
            String interestedUserUsername = getUsernameById(interestedUserId);

            // Construct API URL
            String exchangeApiUrl = constructApiUrl(request, "/api/exchanges");
            String messageApiUrl = constructApiUrl(request, "/api/messages");
            LOGGER.info("Exchange API URL: " + exchangeApiUrl);
            LOGGER.info("Message API URL: " + messageApiUrl);

            // Create JSON object for exchange data
            JSONObject exchangeData = createExchangeData(itemId, interestedUserId, ownerId, ownerUsername, interestedUserUsername, itemTitle, "pending");
            LOGGER.info("Exchange request body: " + exchangeData.toString());

            // Send exchange request to API
            try {
                HttpURLConnection exchangeConn = sendApiRequest(exchangeApiUrl, exchangeData);
                int exchangeId = handleExchangeResponse(exchangeConn, request, response);

                // Create JSON object for message data
                JSONObject messageData = createMessageData(exchangeId, interestedUserId, ownerId, content);
                LOGGER.info("Message request body: " + messageData.toString());

                HttpURLConnection messageConn = sendApiRequest(messageApiUrl, messageData);
                handleMessageResponse(messageConn, request, response, exchangeId);

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error during exchange process", e);
                request.setAttribute("errorMessage", "An error occurred during the exchange process. Please try again later.");
                request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            request.setAttribute("errorMessage", "An error occurred while processing your request. Please try again later.");
            request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
        }
    }

    private JSONObject getItemDetails(int itemId) throws SQLException {
        String query = "SELECT user_id, title FROM items WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
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

    private String getUsernameById(int userId) throws SQLException {
        String query = "SELECT username FROM users WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        }
        return null;
    }

    private String constructApiUrl(HttpServletRequest request, String path) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + path;
    }

    private JSONObject createExchangeData(int itemId, int interestedUserId, int ownerId, String ownerUsername, String interestedUserUsername, String itemTitle, String status) {
        JSONObject exchangeData = new JSONObject();
        exchangeData.put("itemId", itemId);
        exchangeData.put("interestedUserId", interestedUserId);
        exchangeData.put("ownerId", ownerId);
        exchangeData.put("ownerUsername", ownerUsername);
        exchangeData.put("interestedUserUsername", interestedUserUsername);
        exchangeData.put("itemTitle", itemTitle);
        exchangeData.put("status", status);
        return exchangeData;
    }

    private JSONObject createMessageData(int exchangeId, int senderId, int receiverId, String content) {
        JSONObject messageData = new JSONObject();
        messageData.put("exchangeId", exchangeId);
        messageData.put("senderId", senderId);
        messageData.put("receiverId", receiverId);
        messageData.put("content", content);
        return messageData;
    }

    private HttpURLConnection sendApiRequest(String apiUrl, JSONObject requestData) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestData.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return conn;
    }

    private int handleExchangeResponse(HttpURLConnection conn, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int responseCode = conn.getResponseCode();
        LOGGER.info("Exchange response code: " + responseCode);
        LOGGER.info("Exchange response message: " + conn.getResponseMessage());
        logResponseHeaders(conn);

        String responseBody = readResponseBody(conn);
        LOGGER.info("Exchange response body: " + responseBody);

        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getInt("exchangeId");
        } else {
            request.setAttribute("errorMessage", "Exchange creation failed: " + responseBody);
            request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
            throw new ServletException("Exchange creation failed");
        }
    }

    private void handleMessageResponse(HttpURLConnection conn, HttpServletRequest request, HttpServletResponse response, int exchangeId) throws IOException, ServletException {
        int responseCode = conn.getResponseCode();
        LOGGER.info("Message response code: " + responseCode);
        LOGGER.info("Message response message: " + conn.getResponseMessage());
        logResponseHeaders(conn);

        String responseBody = readResponseBody(conn);
        LOGGER.info("Message response body: " + responseBody);

        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            // Redirect to MessageServlet to display the updated messages
            response.sendRedirect("MessageServlet?exchangeId=" + exchangeId);
        } else {
            request.setAttribute("errorMessage", "Message creation failed: " + responseBody);
            request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
        }
    }

    private void logResponseHeaders(HttpURLConnection conn) {
        Map<String, List<String>> headers = conn.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            LOGGER.info(entry.getKey() + ": " + entry.getValue());
        }
    }

    private String readResponseBody(HttpURLConnection conn) throws IOException {
        InputStream is = (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) ? conn.getInputStream() : conn.getErrorStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }
}
