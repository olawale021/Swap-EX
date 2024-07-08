package com.example.servlets.contracts;

import com.example.config.DBConnection;
import com.example.models.contracts.ContractDAO;
import com.example.models.contracts.ContractModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

// The servlet is mapped to the URL pattern "/createContract"
@WebServlet("/createContract")
public class CreateContractServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CreateContractServlet.class.getName());

    // Handles POST requests to create a new contract
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the user ID from the session
        HttpSession session = request.getSession();
        Object userIdAttr = session.getAttribute("userId");

        // If the user is not logged in, redirect to the login page
        if (userIdAttr == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // Parse form parameters
        int userId = Integer.parseInt(userIdAttr.toString());
        int exchangeId = Integer.parseInt(request.getParameter("exchangeId"));
        String terms = request.getParameter("terms");
        boolean signedByOwner = request.getParameter("signedByOwner") != null;
        boolean signedByInterestedUser = request.getParameter("signedByInterestedUser") != null;

        LOGGER.info("Received form data: exchangeId=" + exchangeId + ", terms=" + terms + ", signedByOwner=" + signedByOwner + ", signedByInterestedUser=" + signedByInterestedUser);

        try (Connection connection = DBConnection.getConnection()) {
            // Create a ContractDAO instance for database operations
            ContractDAO contractDAO = new ContractDAO(connection);
            
            // Create and populate a ContractModel object with the form data
            ContractModel contract = new ContractModel();
            contract.setExchangeId(exchangeId);
            contract.setTerms(terms);
            contract.setSignedByOwner(signedByOwner);
            contract.setSignedByInterestedUser(signedByInterestedUser);
            contract.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            contract.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            // Save the contract to the database
            contractDAO.createContract(contract);

            // Redirect to MessageServlet to display messages for the exchange
            response.sendRedirect("MessageServlet?exchangeId=" + exchangeId);
        } catch (SQLException e) {
            // Log any database errors and forward the user to an error page
            LOGGER.log(Level.SEVERE, "Database error while creating contract", e);
            request.setAttribute("errorMessage", "An error occurred while creating the contract. Please try again later.");
            request.getRequestDispatcher("Messages.jsp").forward(request, response);
        }
    }
}