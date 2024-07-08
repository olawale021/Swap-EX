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
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MessageServlet.class.getName());

    // Handles GET requests to display the messages page and relevant conversation details
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object userIdAttr = session.getAttribute("userId");

        // Check if the user is logged in
        if (userIdAttr == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        int userId = Integer.parseInt(userIdAttr.toString());

        try (Connection connection = DBConnection.getConnection()) {
            // DAO instances for handling exchanges, messages, and contracts
            ExchangeDAO exchangeDAO = new ExchangeDAO(connection);
            MessageDAO messageDAO = new MessageDAO(connection);
            ContractDAO contractDAO = new ContractDAO(connection);

            // Fetch all conversations related to the user
            List<ExchangeModel> conversations = exchangeDAO.getExchangesByUserId(userId);
            request.setAttribute("conversations", conversations);
            LOGGER.info("Fetched conversations: " + conversations.size());

            // Determine the exchange ID to display messages for
            String exchangeIdParam = request.getParameter("exchangeId");
            if ((exchangeIdParam == null || exchangeIdParam.isEmpty()) && !conversations.isEmpty()) {
                exchangeIdParam = String.valueOf(conversations.get(0).getId());
            }

            if (exchangeIdParam != null && !exchangeIdParam.isEmpty()) {
                int exchangeId = Integer.parseInt(exchangeIdParam);
                // Fetch messages for the selected exchange
                List<MessageModel> messages = messageDAO.getMessagesByExchangeId(exchangeId);
                request.setAttribute("messages", messages);
                LOGGER.info("Fetched messages for exchangeId " + exchangeId + ": " + messages.size());

                // Fetch the selected exchange details
                ExchangeModel selectedExchange = exchangeDAO.getExchangeById(exchangeId);
                if (selectedExchange != null) {
                    request.setAttribute("selectedItem", selectedExchange.getItemTitle());
                    request.setAttribute("selectedConversation", selectedExchange);
                    LOGGER.info("Selected exchange details set: " + selectedExchange.getItemTitle() + ", " + selectedExchange.getOtherUserUsername(userId));

                    // Determine if the current user is the item owner
                    boolean isOwner = selectedExchange.getOwnerId() == userId;
                    request.setAttribute("isOwner", isOwner);
                    LOGGER.info("Is user owner: " + isOwner);

                    // Check if a contract exists for the selected exchange
                    ContractModel existingContract = contractDAO.getContractByExchangeId(exchangeId);
                    boolean contractExists = (existingContract != null);
                    request.setAttribute("contractExists", contractExists);
                    request.setAttribute("existingContract", existingContract);
                    if (contractExists) {
                        request.setAttribute("contract", existingContract);
                    }
                } else {
                    // Handle the case where the exchange is not found
                    request.setAttribute("selectedItem", "Item not found.");
                    request.setAttribute("selectedConversation", null);
                    request.setAttribute("isOwner", false);
                    request.setAttribute("contractExists", false);
                    LOGGER.info("Selected exchange details set: null, null");
                }
            }

            // Forward the request to the messages JSP page
            request.getRequestDispatcher("Messages.jsp").forward(request, response);
        } catch (SQLException e) {
            // Log any database errors and display an error message
            LOGGER.log(Level.SEVERE, "Database error fetching conversations", e);
            request.setAttribute("errorMessage", "An error occurred while fetching your conversations. Please try again later.");
            request.getRequestDispatcher("Messages.jsp").forward(request, response);
        }
    }

    // Handles POST requests to send a new message
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object userIdAttr = session.getAttribute("userId");

        // Check if the user is logged in
        if (userIdAttr == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        int userId = Integer.parseInt(userIdAttr.toString());

        // Retrieve the exchange ID and message content from the request
        String exchangeIdParam = request.getParameter("exchangeId");
        String messageContent = request.getParameter("messageContent");

        // Validate the input parameters
        if (exchangeIdParam == null || exchangeIdParam.isEmpty() || messageContent == null || messageContent.isEmpty()) {
            response.sendRedirect("MessageServlet?exchangeId=" + exchangeIdParam);
            return;
        }

        int exchangeId = Integer.parseInt(exchangeIdParam);

        try (Connection connection = DBConnection.getConnection()) {
            ExchangeDAO exchangeDAO = new ExchangeDAO(connection);
            MessageDAO messageDAO = new MessageDAO(connection);

            // Fetch the exchange details
            ExchangeModel exchange = exchangeDAO.getExchangeById(exchangeId);

            if (exchange == null) {
                response.sendRedirect("MessageServlet");
                return;
            }

            // Determine the receiver ID based on the user's role in the exchange
            int receiverId;
            if (exchange.getOwnerId() == userId) {
                receiverId = exchange.getInterestedUserId();
            } else {
                receiverId = exchange.getOwnerId();
            }

            // Create and save the new message
            MessageModel message = new MessageModel();
            message.setExchangeId(exchangeId);
            message.setSenderId(userId);
            message.setReceiverId(receiverId);
            message.setContent(messageContent);
            message.setCreatedAt(new Timestamp(System.currentTimeMillis())); // Set the current timestamp

            messageDAO.createMessage(message);
            LOGGER.info("Added new message to exchangeId " + exchangeId);

            response.sendRedirect("MessageServlet?exchangeId=" + exchangeId);
        } catch (SQLException e) {
            // Log any database errors and display an error message
            LOGGER.log(Level.SEVERE, "Database error adding message", e);
            request.setAttribute("errorMessage", "An error occurred while sending your message. Please try again later.");
            request.getRequestDispatcher("Messages.jsp").forward(request, response);
        }
    }
}
