package com.example.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.json.JSONObject;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phoneNumber = request.getParameter("phoneNumber");
        String street = request.getParameter("street");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zip = request.getParameter("zip");

        // Check for null values
        if (username == null || password == null || phoneNumber == null ||
                street == null || city == null || state == null || zip == null) {
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("Register.jsp").forward(request, response);
            return;
        }

        // Construct API URL
        String apiUrl = constructApiUrl(request);
        LOGGER.info("API URL: " + apiUrl);

        // Create JSON object for registration data
        JSONObject registrationData = createRegistrationData(username, password, phoneNumber, street, city, state, zip);
        LOGGER.info("Request body: " + registrationData.toString());

        // Send registration request to API
        try {
            HttpURLConnection conn = sendRegistrationRequest(apiUrl, registrationData);
            handleApiResponse(conn, request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during registration process", e);
            request.setAttribute("errorMessage", "An error occurred during registration. Please try again later.");
            request.getRequestDispatcher("Register.jsp").forward(request, response);
        }
    }

    private String constructApiUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/api/users/register";
    }

    private JSONObject createRegistrationData(String username, String password, String phoneNumber,
                                              String street, String city, String state, String zip) {
        JSONObject registrationData = new JSONObject();
        registrationData.put("username", username);
        registrationData.put("password", password);
        registrationData.put("phoneNumber", phoneNumber);

        JSONObject addressJson = new JSONObject();
        addressJson.put("street", street);
        addressJson.put("city", city);
        addressJson.put("state", state);
        addressJson.put("zip", zip);

        registrationData.put("address", addressJson);
        return registrationData;
    }

    private HttpURLConnection sendRegistrationRequest(String apiUrl, JSONObject registrationData) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = registrationData.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return conn;
    }

    private void handleApiResponse(HttpURLConnection conn, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int responseCode = conn.getResponseCode();
        LOGGER.info("Response code: " + responseCode);
        LOGGER.info("Response message: " + conn.getResponseMessage());
        logResponseHeaders(conn);

        String responseBody = readResponseBody(conn);
        LOGGER.info("Response body: " + responseBody);

        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            LOGGER.info("User registered successfully");
            response.sendRedirect("Login.jsp");
        } else {
            request.setAttribute("errorMessage", "Registration failed: " + responseBody);
            request.getRequestDispatcher("Register.jsp").forward(request, response);
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