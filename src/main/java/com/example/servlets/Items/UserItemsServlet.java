package com.example.servlets.Items;

import com.example.models.category.CategoryDAO;
import com.example.models.category.CategoryModel;
import com.example.models.items.ItemDAO;
import com.example.models.items.ItemModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/UserItemsServlet")
public class UserItemsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UserItemsServlet.class.getName());
    private ItemDAO itemDAO = new ItemDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userIdString = (String) session.getAttribute("userId");

        if (userIdString == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid user ID", e);
            response.sendRedirect("Login.jsp");
            return;
        }

        try {
            List<ItemModel> userItems = itemDAO.getItemsByUserId(userId);
            List<CategoryModel> categories = categoryDAO.getAllCategories();

            request.setAttribute("userItems", userItems);
            request.setAttribute("categories", categories);

            String success = request.getParameter("success");
            String error = request.getParameter("error");

            if ("true".equals(success)) {
                request.setAttribute("message", "Item updated successfully!");
            } else if (error != null) {
                request.setAttribute("errorMessage", "An error occurred while updating the item.");
            }

            request.getRequestDispatcher("UserItems.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching data", e);
            request.setAttribute("errorMessage", "An error occurred while fetching data. Please try again later.");
            request.getRequestDispatcher("Error.jsp").forward(request, response);
        }
    }
}
