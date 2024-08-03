package com.example.servlets.exchange;


import com.example.config.DBConnection;
import com.example.models.exchanges.ExchangeDAO;
import com.example.models.exchanges.ExchangeModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/UserExchangesServlet")
public class UserExchangesServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UserExchangesServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object userIdAttr = session.getAttribute("userId");

        if (userIdAttr == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        int userId = Integer.parseInt(userIdAttr.toString());

        try (Connection connection = DBConnection.getConnection()) {
            ExchangeDAO exchangeDAO = new ExchangeDAO(connection);

            // Retrieve all exchanges related to the logged-in user
            List<ExchangeModel> userExchanges = exchangeDAO.getExchangesByUserId(userId);
            request.setAttribute("userExchanges", userExchanges);
            LOGGER.info("Fetched user exchanges: " + userExchanges.size());

            request.getRequestDispatcher("user_exchanges.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error fetching user exchanges", e);
            request.setAttribute("errorMessage", "An error occurred while fetching your exchanges. Please try again later.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
