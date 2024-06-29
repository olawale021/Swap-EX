package com.example.servlets;

import com.example.models.items.ItemModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/ItemDetailsServlet")
public class ItemDetailsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ItemDetailsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemIdParam = request.getParameter("id");
        if (itemIdParam != null && !itemIdParam.isEmpty()) {
            try {
                int itemId = Integer.parseInt(itemIdParam);
                ItemModel item = fetchItemDetails(request, itemId);
                if (item != null) {
                    request.setAttribute("item", item);
                    request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found");
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "Invalid item ID format", e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID format");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID");
        }
    }

    private ItemModel fetchItemDetails(HttpServletRequest request, int itemId) throws IOException {
        String apiUrl = constructApiUrl(request, "/api/items/" + itemId);
        HttpURLConnection conn = sendGetRequest(apiUrl);

        int responseCode = conn.getResponseCode();
        LOGGER.info("Response code: " + responseCode);
        LOGGER.info("Response message: " + conn.getResponseMessage());
        logResponseHeaders(conn);

        String responseBody = readResponseBody(conn);
        LOGGER.info("Response body: " + responseBody);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(responseBody, ItemModel.class);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error parsing item details response", e);
                throw new IOException("Error parsing item details response", e);
            }
        } else {
            throw new IOException("Failed to fetch item details: " + responseBody);
        }
    }

    private String constructApiUrl(HttpServletRequest request, String path) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + path;
    }

    private HttpURLConnection sendGetRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        return conn;
    }

    private void logResponseHeaders(HttpURLConnection conn) {
        conn.getHeaderFields().forEach((key, value) -> LOGGER.info(key + ": " + value));
    }

    private String readResponseBody(HttpURLConnection conn) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST ? conn.getInputStream() : conn.getErrorStream(),
                StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }
}
