package com.example.servlets.Items;

import com.example.models.items.ItemDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/DeleteItemServlet")
public class DeleteItemServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DeleteItemServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemIdParam = request.getParameter("id");

        if (itemIdParam == null || itemIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item ID is required");
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(itemIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Item ID");
            return;
        }

        ItemDAO itemDAO = new ItemDAO();
        try {
            boolean deleted = itemDAO.deleteItem(itemId);
            if (deleted) {
                response.sendRedirect("UserItemsServlet"); // Redirect to the user items page
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting item", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the item. Please try again later.");
        }
    }

    // Ensure GET request also redirects to UserItemsServlet
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
