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
    
    // Instance of ItemDAO for interacting with the database
    private ItemDAO itemDAO = new ItemDAO();

    // Handles GET requests to display item details
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the item ID from the request parameters
        String itemIdParam = request.getParameter("id");
        
        // Check if the item ID parameter is present and not empty
        if (itemIdParam != null && !itemIdParam.isEmpty()) {
            try {
                // Convert the item ID from String to Integer
                int itemId = Integer.parseInt(itemIdParam);
                
                // Fetch the item details from the database using the item ID
                ItemModel item = itemDAO.getItemById(itemId);
                
                // Check if the item exists
                if (item != null) {
                    // Set the item as a request attribute to be accessed in the JSP
                    request.setAttribute("item", item);
                    
                    // Forward the request to ItemDetails.jsp to display the item details
                    request.getRequestDispatcher("ItemDetails.jsp").forward(request, response);
                } else {
                    // If the item is not found, send a 404 error response
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found");
                }
            } catch (NumberFormatException | SQLException e) {
                // Log any exceptions that occur during the fetching process
                LOGGER.log(Level.SEVERE, "Error fetching item details", e);
                
                // If an error occurs, send a 400 error response for invalid item ID format
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID format");
            }
        } else {
            // If the item ID parameter is missing or invalid, send a 400 error response
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID");
        }
    }
}