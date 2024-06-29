package com.example.servlets.auth;

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

import com.example.models.users.UserModel;
import org.json.JSONObject;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Construct API URL
        String apiUrl = constructApiUrl(request);
        LOGGER.info("API URL: " + apiUrl);

        // Create JSON object for login data
        JSONObject loginData = createLoginData(username, password);
        LOGGER.info("Request body: " + loginData.toString());

        // Send login request to API
        try {
            HttpURLConnection conn = sendLoginRequest(apiUrl, loginData);
            handleApiResponse(conn, request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during login process", e);
            request.setAttribute("errorMessage", "An error occurred during login. Please try again later.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }

    private String constructApiUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/api/users/login";
    }

    private JSONObject createLoginData(String username, String password) {
        JSONObject loginData = new JSONObject();
        loginData.put("username", username);
        loginData.put("password", password);
        return loginData;
    }

    private HttpURLConnection sendLoginRequest(String apiUrl, JSONObject loginData) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = loginData.toString().getBytes(StandardCharsets.UTF_8);
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
        HttpSession session = request.getSession();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try {
                JSONObject jsonResponse = new JSONObject(responseBody);

                String userId = String.valueOf(jsonResponse.getInt("id"));
                String username = jsonResponse.getString("username");

                // Create a User object
                UserModel user = new UserModel();
                user.setId(Integer.parseInt(userId));
                user.setUsername(username);

                // Set attributes in the session
                session.setAttribute("userId", userId);
                session.setAttribute("user", user);

                LOGGER.info("User logged in: " + username);
                response.sendRedirect("Dashboard.jsp");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error parsing login response", e);
                request.setAttribute("errorMessage", "An error occurred during login. Please try again.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Login failed: " + responseBody);
            request.getRequestDispatcher("Login.jsp").forward(request, response);
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
