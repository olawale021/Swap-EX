package com.example.servlets.contracts;

import com.example.config.DBConnection;
import com.example.models.contracts.ContractDAO;
import com.example.models.contracts.ContractModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/SignContractServlet")
public class SignContractServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SignContractServlet.class.getName());

    // Handles POST requests to sign a contract
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the contract ID and check if the interested user has signed
        int contractId = Integer.parseInt(request.getParameter("contractId"));
        boolean signedByInterestedUser = request.getParameter("signedByInterestedUser") != null;

        LOGGER.info("Signing contract with ID: " + contractId + " by interested user: " + signedByInterestedUser);

        try (Connection connection = DBConnection.getConnection()) {
            // Create a ContractDAO instance for database operations
            ContractDAO contractDAO = new ContractDAO(connection);
            
            // Fetch the contract details from the database
            ContractModel contract = contractDAO.getContractById(contractId);

            if (contract != null) {
                // Update the contract with the interested user's signature and the current timestamp
                contract.setSignedByInterestedUser(signedByInterestedUser);
                contract.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

                // Save the updated contract in the database
                contractDAO.updateContract(contract);

                LOGGER.info("Contract signed by interested user: " + contract);

                // If both users have signed the contract, update the exchange status to "Confirmed"
                if (contract.isSignedByOwner() && contract.isSignedByInterestedUser()) {
                    contractDAO.updateExchangeStatus(contract.getExchangeId(), "Confirmed");
                    LOGGER.info("Exchange confirmed for exchange ID: " + contract.getExchangeId());
                }

                // Redirect to MessageServlet to display messages for the exchange
                response.sendRedirect("MessageServlet?exchangeId=" + contract.getExchangeId());
            } else {
                // If the contract is not found, log a warning and send a 404 error response
                LOGGER.warning("Contract not found with ID: " + contractId);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Contract not found");
            }
        } catch (SQLException e) {
            // Log any database errors that occur and send a 500 error response
            LOGGER.log(Level.SEVERE, "Error signing contract", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error signing contract");
        }
    }
}