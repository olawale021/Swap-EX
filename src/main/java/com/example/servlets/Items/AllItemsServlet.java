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
    private ItemDAO itemDAO = new ItemDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ItemModel> items = itemDAO.getAllItemsSortedByDate();
            List<CategoryModel> categories = itemDAO.getAllCategories();

            request.setAttribute("items", items);
            request.setAttribute("categories", categories);

            request.getRequestDispatcher("AllItems.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching items or categories", e);
            request.setAttribute("errorMessage", "An error occurred while fetching items or categories. Please try again later.");
            request.getRequestDispatcher("AllItems.jsp").forward(request, response);
        }
    }
}
