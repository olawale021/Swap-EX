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

@WebServlet("/EditContractServlet")
public class EditContractServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EditContractServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int contractId = Integer.parseInt(request.getParameter("contractId"));
        String terms = request.getParameter("terms");

        try (Connection connection = DBConnection.getConnection()) {
            ContractDAO contractDAO = new ContractDAO(connection);
            ContractModel contract = contractDAO.getContractById(contractId);

            if (contract != null) {
                contract.setTerms(terms);
                contract.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

                contractDAO.updateContract(contract);

                response.sendRedirect("MessageServlet?exchangeId=" + contract.getExchangeId());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Contract not found");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating contract", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating contract");
        }
    }
}
