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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int contractId = Integer.parseInt(request.getParameter("contractId"));
        boolean signedByInterestedUser = request.getParameter("signedByInterestedUser") != null;

        LOGGER.info("Signing contract with ID: " + contractId + " by interested user: " + signedByInterestedUser);

        try (Connection connection = DBConnection.getConnection()) {
            ContractDAO contractDAO = new ContractDAO(connection);
            ContractModel contract = contractDAO.getContractById(contractId);

            if (contract != null) {
                contract.setSignedByInterestedUser(signedByInterestedUser);
                contract.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

                contractDAO.updateContract(contract);

                LOGGER.info("Contract signed by interested user: " + contract);

                if (contract.isSignedByOwner() && contract.isSignedByInterestedUser()) {
                    contractDAO.updateExchangeStatus(contract.getExchangeId(), "Confirmed");
                    LOGGER.info("Exchange confirmed for exchange ID: " + contract.getExchangeId());
                }

                response.sendRedirect("MessageServlet?exchangeId=" + contract.getExchangeId());
            } else {
                LOGGER.warning("Contract not found with ID: " + contractId);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Contract not found");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error signing contract", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error signing contract");
        }
    }
}
