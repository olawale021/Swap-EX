package com.example.servlets.Items;

import com.example.models.items.ItemModel;
import com.example.models.category.CategoryModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/AllItemsServlet")
public class AllItemsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AllItemsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch items and categories
        try {
            List<ItemModel> items = fetchItems(request);
            List<CategoryModel> categories = fetchCategories(request);

            // Set items and categories as request attributes
            request.setAttribute("items", items);
            request.setAttribute("categories", categories);

            // Forward to JSP
            request.getRequestDispatcher("AllItems.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching items or categories", e);
            request.setAttribute("errorMessage", "An error occurred while fetching items or categories. Please try again later.");
            request.getRequestDispatcher("AllItems.jsp").forward(request, response);
        }
    }

    private List<ItemModel> fetchItems(HttpServletRequest request) throws IOException {
        String apiUrl = constructApiUrl(request, "/api/items");
        HttpURLConnection conn = sendGetRequest(apiUrl);
        return handleItemsApiResponse(conn);
    }

    private List<CategoryModel> fetchCategories(HttpServletRequest request) throws IOException {
        String apiUrl = constructApiUrl(request, "/api/categories");
        HttpURLConnection conn = sendGetRequest(apiUrl);
        return handleCategoriesApiResponse(conn);
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

    private List<ItemModel> handleItemsApiResponse(HttpURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();
        LOGGER.info("Items Response code: " + responseCode);
        LOGGER.info("Items Response message: " + conn.getResponseMessage());
        logResponseHeaders(conn);

        String responseBody = readResponseBody(conn);
        LOGGER.info("Items Response body: " + responseBody);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, ItemModel.class));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error parsing items response", e);
                throw new IOException("Error parsing items response", e);
            }
        } else {
            throw new IOException("Failed to fetch items: " + responseBody);
        }
    }

    private List<CategoryModel> handleCategoriesApiResponse(HttpURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();
        LOGGER.info("Categories Response code: " + responseCode);
        LOGGER.info("Categories Response message: " + conn.getResponseMessage());
        logResponseHeaders(conn);

        String responseBody = readResponseBody(conn);
        LOGGER.info("Categories Response body: " + responseBody);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, CategoryModel.class));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error parsing categories response", e);
                throw new IOException("Error parsing categories response", e);
            }
        } else {
            throw new IOException("Failed to fetch categories: " + responseBody);
        }
    }

    private void logResponseHeaders(HttpURLConnection conn) {
        conn.getHeaderFields().forEach((key, value) -> LOGGER.info(key + ": " + value));
    }

    private String readResponseBody(HttpURLConnection conn) throws IOException {
        InputStream is = (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) ? conn.getInputStream() : conn.getErrorStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }
}
