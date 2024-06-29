package com.example.servlets.Items;

import com.example.models.category.CategoryModel;
import com.example.models.items.ItemModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/UserItemsServlet")
public class UserItemsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UserItemsServlet.class.getName());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userIdString = (String) session.getAttribute("userId");

        if (userIdString == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid user ID", e);
            response.sendRedirect("Login.jsp");
            return;
        }

        try {
            List<ItemModel> userItems = fetchUserItems(request, userId);
            List<CategoryModel> categories = fetchCategories(request);

            request.setAttribute("userItems", userItems);
            request.setAttribute("categories", categories);

            String success = request.getParameter("success");
            String error = request.getParameter("error");

            if ("true".equals(success)) {
                request.setAttribute("message", "Item updated successfully!");
            } else if (error != null) {
                request.setAttribute("errorMessage", "An error occurred while updating the item.");
            }

            request.getRequestDispatcher("UserItems.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching data", e);
            request.setAttribute("errorMessage", "An error occurred while fetching data. Please try again later.");
            request.getRequestDispatcher("Error.jsp").forward(request, response);
        }
    }

    private List<ItemModel> fetchUserItems
            (HttpServletRequest request, int userId) throws IOException {
        String apiUrl = constructApiUrl(request, "/api/items/user/" + userId);
        return fetchData(apiUrl, new TypeReference<List<ItemModel>>() {});
    }

    private List<CategoryModel> fetchCategories(HttpServletRequest request) throws IOException {
        String apiUrl = constructApiUrl(request, "/api/categories");
        return fetchData(apiUrl, new TypeReference<List<CategoryModel>>() {});
    }

    private <T> T fetchData(String apiUrl, TypeReference<T> typeReference) throws IOException {
        HttpURLConnection conn = sendGetRequest(apiUrl);
        String responseBody = handleApiResponse(conn);
        return OBJECT_MAPPER.readValue(responseBody, typeReference);
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

    private String handleApiResponse(HttpURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();
        LOGGER.info("Response code: " + responseCode);
        LOGGER.info("Response message: " + conn.getResponseMessage());

        String responseBody = readResponseBody(conn);
        LOGGER.info("Response body: " + responseBody);

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("API request failed: " + responseBody);
        }

        return responseBody;
    }

    private String readResponseBody(HttpURLConnection conn) throws IOException {
        try (InputStream is = (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) ? conn.getInputStream() : conn.getErrorStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }
}
