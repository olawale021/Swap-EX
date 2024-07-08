package com.example.servlets.exchange;

import com.example.models.category.CategoryDAO;
import com.example.models.category.CategoryModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/fetchCategories")
public class FetchCategoriesServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(FetchCategoriesServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryDAO categoryDAO = new CategoryDAO();

        try {
            List<CategoryModel> categories = categoryDAO.getAllCategories();
            LOGGER.info("Fetched categories: " + categories.toString());

            // Set categories as a request attribute
            request.setAttribute("categories", categories);

            // Forward to JSP
            request.getRequestDispatcher("AddItem.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories", e);
            request.setAttribute("errorMessage", "An error occurred while fetching categories. Please try again later.");
            request.getRequestDispatcher("AddItem.jsp").forward(request, response);
        }
    }
}
