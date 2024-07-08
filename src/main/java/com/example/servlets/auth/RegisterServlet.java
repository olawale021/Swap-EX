package com.example.servlets.auth;

import com.example.models.users.UserDAO;
import com.example.models.users.UserModel;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        // Create the user object
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);  // Store phone number as string

        JSONObject address = new JSONObject();
        address.put("street", street);
        address.put("city", city);
        address.put("state", state);
        address.put("zip", zip);
        user.setAddress(address);

        // Save the user to the database
        UserDAO userDAO = new UserDAO();
        try {
            userDAO.addUser(user);
            LOGGER.info("User registered successfully");
            response.sendRedirect("Login.jsp");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during registration process", e);
            request.setAttribute("errorMessage", "An error occurred during registration. Please try again later.");
            request.getRequestDispatcher("Register.jsp").forward(request, response);
        }
    }
}
