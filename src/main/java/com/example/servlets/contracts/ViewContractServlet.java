package com.example.servlets.contracts;

import com.example.config.DBConnection;
import com.example.models.contracts.ContractDAO;
import com.example.models.contracts.ContractModel;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/ViewContractServlet")
public class ViewContractServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ViewContractServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object userIdAttr = session.getAttribute("userId");

        if (userIdAttr == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        int userId = Integer.parseInt(userIdAttr.toString());
        int contractId = Integer.parseInt(request.getParameter("contractId"));

        try (Connection connection = DBConnection.getConnection()) {
            ContractDAO contractDAO = new ContractDAO(connection);
            ContractModel contract = contractDAO.getContractById(contractId);

            if (contract != null) {
                int exchangeId = contract.getExchangeId();
                ExchangeDAO exchangeDAO = new ExchangeDAO(connection);
                ExchangeModel exchange = exchangeDAO.getExchangeById(exchangeId);

                if (exchange != null) {
                    request.setAttribute("contract", contract);
                    boolean isOwner = exchange.getOwnerId() == userId;
                    request.setAttribute("isOwner", isOwner);
                    request.getRequestDispatcher("ViewContract.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Exchange not found.");
                    request.getRequestDispatcher("Messages.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Contract not found.");
                request.getRequestDispatcher("Messages.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error fetching contract", e);
            request.setAttribute("errorMessage", "An error occurred while fetching the contract. Please try again later.");
            request.getRequestDispatcher("Messages.jsp").forward(request, response);
        }
    }
}
