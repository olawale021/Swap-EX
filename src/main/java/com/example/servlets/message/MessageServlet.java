package com.example.servlets.message;

import com.example.config.DBConnection;
import com.example.models.contracts.ContractDAO;
import com.example.models.contracts.ContractModel;
import com.example.models.exchanges.ExchangeDAO;
import com.example.models.exchanges.ExchangeModel;
import com.example.models.messages.MessageDAO;
import com.example.models.messages.MessageModel;

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

@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MessageServlet.class.getName());

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
            MessageDAO messageDAO = new MessageDAO(connection);
            ContractDAO contractDAO = new ContractDAO(connection);

            List<ExchangeModel> conversations = exchangeDAO.getExchangesByUserId(userId);
            request.setAttribute("conversations", conversations);
            LOGGER.info("Fetched conversations: " + conversations.size());

            String exchangeIdParam = request.getParameter("exchangeId");
            if (exchangeIdParam != null && !exchangeIdParam.isEmpty()) {
                int exchangeId = Integer.parseInt(exchangeIdParam);
                List<MessageModel> messages = messageDAO.getMessagesByExchangeId(exchangeId);
                request.setAttribute("messages", messages);
                LOGGER.info("Fetched messages for exchangeId " + exchangeId + ": " + messages.size());

                ExchangeModel selectedExchange = exchangeDAO.getExchangeById(exchangeId);
                if (selectedExchange != null) {
                    request.setAttribute("selectedItem", selectedExchange.getItemTitle());
                    request.setAttribute("selectedConversation", selectedExchange);
                    LOGGER.info("Selected exchange details set: " + selectedExchange.getItemTitle() + ", " + selectedExchange.getOtherUserUsername(userId));

                    boolean isOwner = selectedExchange.getOwnerId() == userId;
                    request.setAttribute("isOwner", isOwner);
                    LOGGER.info("Is user owner: " + isOwner);

                    ContractModel existingContract = contractDAO.getContractByExchangeId(exchangeId);
                    boolean contractExists = (existingContract != null);
                    request.setAttribute("contractExists", contractExists);
                    request.setAttribute("existingContract", existingContract);
                    if (contractExists) {
                        request.setAttribute("contract", existingContract);
                    }
                } else {
                    request.setAttribute("selectedItem", "Item not found.");
                    request.setAttribute("selectedConversation", null);
                    request.setAttribute("isOwner", false);
                    request.setAttribute("contractExists", false);
                    LOGGER.info("Selected exchange details set: null, null");
                }
            }

            request.getRequestDispatcher("Messages.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error fetching conversations", e);
            request.setAttribute("errorMessage", "An error occurred while fetching your conversations. Please try again later.");
            request.getRequestDispatcher("Messages.jsp").forward(request, response);
        }
    }
}
