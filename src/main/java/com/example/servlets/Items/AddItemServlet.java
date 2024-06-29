package com.example.servlets.Items;

import com.example.models.category.CategoryModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/AddItemServlet")
@MultipartConfig
public class AddItemServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AddItemServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch categories and forward to AddItem.jsp
        fetchCategories(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userIdString = (String) session.getAttribute("userId");
        Integer userId;

        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid user ID format", e);
            response.sendRedirect("Login.jsp");
            return;
        }
        String title = request.getParameter("title");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String description = request.getParameter("description");
        String condition = request.getParameter("condition");

        // Handle file uploads
        List<String> photoBase64List = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getContentType() != null && part.getContentType().startsWith("image/")) {
                byte[] photoBytes = part.getInputStream().readAllBytes();
                String photoBase64 = Base64.getEncoder().encodeToString(photoBytes);
                // Adding the prefix for Base64 data URL
                String photoBase64WithPrefix = "data:" + part.getContentType() + ";base64," + photoBase64;
                photoBase64List.add(photoBase64WithPrefix);
            }
        }

        // Construct API URL
        String apiUrl = constructApiUrl(request);

        // Create JSON object for item data
        JSONObject itemData = createItemData(userId, categoryId, title, description, condition, photoBase64List);

        try {
            HttpURLConnection conn = sendAddItemRequest(apiUrl, itemData);
            handleApiResponse(conn, request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during add item process", e);
            request.setAttribute("errorMessage", "An error occurred while adding the item. Please try again later.");
            fetchCategories(request, response); // Re-fetch categories and forward to AddItem.jsp
        }
    }

    private String constructApiUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/api/items/add";
    }

    private JSONObject createItemData(int userId, int categoryId, String description, String title, String condition, List<String> photoBase64List) {
        JSONObject itemData = new JSONObject();
        itemData.put("userId", userId);
        itemData.put("title", title);
        itemData.put("categoryId", categoryId);
        itemData.put("description", description);
        itemData.put("condition", condition);
        itemData.put("photos", new JSONArray(photoBase64List));
        return itemData;
    }

    private HttpURLConnection sendAddItemRequest(String apiUrl, JSONObject itemData) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = itemData.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return conn;
    }

    private void handleApiResponse(HttpURLConnection conn, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            // Item added successfully
            response.sendRedirect("Dashboard.jsp");
        } else {
            // Error adding item
            request.setAttribute("errorMessage", "Failed to add item. Please try again.");
            fetchCategories(request, response); // Re-fetch categories and forward to AddItem.jsp
        }
    }

    private void fetchCategories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apiUrl = constructCategoriesApiUrl(request);
        try {
            HttpURLConnection conn = sendGetRequest(apiUrl);
            String responseBody = readResponseBody(conn);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();
                List<CategoryModel> categories = mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, CategoryModel.class));

                // Set categories as a request attribute
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("AddItem.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Failed to fetch categories.");
                request.getRequestDispatcher("AddItem.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories", e);
            request.setAttribute("errorMessage", "An error occurred while fetching categories. Please try again later.");
            request.getRequestDispatcher("AddItem.jsp").forward(request, response);
        }
    }

    private String constructCategoriesApiUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/api/categories";
    }

    private HttpURLConnection sendGetRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        return conn;
    }

    private String readResponseBody(HttpURLConnection conn) throws IOException {
        InputStream is = (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) ? conn.getInputStream() : conn.getErrorStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }
}
