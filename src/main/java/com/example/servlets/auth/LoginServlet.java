package com.example.servlets.auth;

import com.example.models.users.UserDAO;
import com.example.models.users.UserModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();

        try {
            UserModel user = userDAO.getUserByUsernameAndPassword(username, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", String.valueOf(user.getId()));
                session.setAttribute("user", user);

                LOGGER.info("User logged in: " + username);
                response.sendRedirect("Dashboard.jsp");
            } else {
                request.setAttribute("errorMessage", "Invalid username or password. Please try again.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during login process", e);
            request.setAttribute("errorMessage", "An error occurred during login. Please try again later.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }
}
