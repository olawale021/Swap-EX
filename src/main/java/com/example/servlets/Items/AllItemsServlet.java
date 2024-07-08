package com.example.servlets.Items;

import com.example.models.category.CategoryModel;
import com.example.models.items.ItemDAO;
import com.example.models.items.ItemModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/AllItemsServlet")
public class AllItemsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AllItemsServlet.class.getName());
    
    // Instance of ItemDAO for interacting with the database
    private ItemDAO itemDAO = new ItemDAO();

    // Handles GET requests to fetch all items and categories, and display them on the AllItems.jsp page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve all items sorted by date from the database
            List<ItemModel> items = itemDAO.getAllItemsSortedByDate();
            
            // Retrieve all categories from the database
            List<CategoryModel> categories = itemDAO.getAllCategories();

            // Set the items and categories as request attributes to be accessed in the JSP
            request.setAttribute("items", items);
            request.setAttribute("categories", categories);

            // Forward the request to AllItems.jsp to display the items and categories
            request.getRequestDispatcher("AllItems.jsp").forward(request, response);
        } catch (Exception e) {
            // Log any exceptions that occur during the fetching process
            LOGGER.log(Level.SEVERE, "Error fetching items or categories", e);
            
            // If an error occurs, set an error message and forward to the AllItems.jsp page
            request.setAttribute("errorMessage", "An error occurred while fetching items or categories. Please try again later.");
            request.getRequestDispatcher("AllItems.jsp").forward(request, response);
        }
    }
}