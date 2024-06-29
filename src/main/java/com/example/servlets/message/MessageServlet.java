package com.example.servlets.message;

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
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MessageServlet.class.getName());
    private ExchangeDAO exchangeDAO = new ExchangeDAO();
    private MessageDAO messageDAO = new MessageDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String exchangeIdParam = request.getParameter("exchangeId");
        String content = request.getParameter("content");
        Object userIdAttr = request.getSession().getAttribute("userId");

        if (exchangeIdParam == null || exchangeIdParam.isEmpty() || content == null || content.isEmpty() || userIdAttr == null) {
            LOGGER.severe("Invalid input parameters. exchangeId: " + exchangeIdParam + ", content: " + content + ", userId: " + userIdAttr);
            request.setAttribute("errorMessage", "Invalid input parameters. Please try again.");
            request.getRequestDispatcher("Messages.jsp").forward(request, response);
            return;
        }

        int exchangeId;
        int senderId;
        try {
            exchangeId = Integer.parseInt(exchangeIdParam);
            senderId = Integer.parseInt(userIdAttr.toString());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing input parameters", e);
            request.setAttribute("errorMessage", "Invalid input parameters. Please try again.");
            request.getRequestDispatcher("Messages.jsp").forward(request, response);
            return;
        }

        try {
            ExchangeModel exchange = exchangeDAO.getExchangeById(exchangeId);
            if (exchange == null) {
                LOGGER.severe("Exchange not found. exchangeId: " + exchangeId);
                request.setAttribute("errorMessage", "Exchange not found.");
                request.getRequestDispatcher("Messages.jsp").forward(request, response);
                return;
            }

            int receiverId = exchange.getOtherUserId(senderId);

            // Create message record
            MessageModel message = new MessageModel();
            message.setExchangeId(exchangeId);
            message.setSenderId(senderId);
            message.setReceiverId(receiverId);
            message.setContent(content);
            message.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            messageDAO.createMessage(message);

            // Redirect to the doGet method to refresh the page with updated messages
            response.sendRedirect("MessageServlet?exchangeId=" + exchangeId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            request.setAttribute("errorMessage", "An error occurred while processing your request. Please try again later.");
            request.getRequestDispatcher("Messages.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object userIdAttr = session.getAttribute("userId");

        if (userIdAttr == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        int userId = Integer.parseInt(userIdAttr.toString());

        try {
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
                } else {
                    request.setAttribute("selectedItem", "Item not found.");
                    request.setAttribute("selectedConversation", null);
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
