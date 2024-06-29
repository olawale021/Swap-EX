package com.example.servlets.exchange;

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

@WebServlet("/fetchCategories")
public class FetchCategoriesServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(FetchCategoriesServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Construct API URL
        String apiUrl = constructApiUrl(request);
        LOGGER.info("API URL: " + apiUrl);

        // Send GET request to API
        try {
            HttpURLConnection conn = sendGetRequest(apiUrl);
            handleApiResponse(conn, request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories", e);
            request.setAttribute("errorMessage", "An error occurred while fetching categories. Please try again later.");
            request.getRequestDispatcher("AddItem.jsp").forward(request, response);
        }
    }

    private String constructApiUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/api/categories";
    }

    private HttpURLConnection sendGetRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        return conn;
    }

    private void handleApiResponse(HttpURLConnection conn, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int responseCode = conn.getResponseCode();
        LOGGER.info("Response code: " + responseCode);
        LOGGER.info("Response message: " + conn.getResponseMessage());
        logResponseHeaders(conn);

        String responseBody = readResponseBody(conn);
        LOGGER.info("Response body: " + responseBody);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<CategoryModel> categories = mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, CategoryModel.class));

                // Log the fetched categories
                LOGGER.info("Fetched categories: " + categories.toString());

                // Set categories as a request attribute
                request.setAttribute("categories", categories);

                // Forward to JSP
                request.getRequestDispatcher("AddItem.jsp").forward(request, response);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error parsing categories response", e);
                request.setAttribute("errorMessage", "An error occurred while parsing categories. Please try again.");
                request.getRequestDispatcher("AddItem.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Failed to fetch categories: " + responseBody);
            request.getRequestDispatcher("AddItem.jsp").forward(request, response);
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
