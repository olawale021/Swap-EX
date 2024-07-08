package com.example.servlets.Items;

import com.example.models.items.ItemDAO;
import com.example.models.items.ItemModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/ItemDetailsServlet")
public class ItemDetailsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ItemDetailsServlet.class.getName());
    private ItemDAO itemDAO = new ItemDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemIdParam = request.getParameter("id");
        if (itemIdParam != null && !itemIdParam.isEmpty()) {
            try {
                int itemId = Integer.parseInt(itemIdParam);
                ItemModel item = itemDAO.getItemById(itemId);
                if (item != null) {
                    request.setAttribute("item", item);
                    request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found");
                }
            } catch (NumberFormatException | SQLException e) {
                LOGGER.log(Level.SEVERE, "Error fetching item details", e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID format");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID");
        }
    }
}
