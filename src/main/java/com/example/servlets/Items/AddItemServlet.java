package com.example.servlets.Items;

import com.example.models.category.CategoryModel;
import com.example.models.items.ItemDAO;
import com.example.models.items.ItemModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/AddItemServlet")
@MultipartConfig
public class AddItemServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AddItemServlet.class.getName());
    private ItemDAO itemDAO = new ItemDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch categories and forward to AddItem.jsp
        fetchCategories(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userIdString = (String) session.getAttribute("userId");
        Integer userId;

        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid user ID format", e);
            response.sendRedirect("Login.jsp");
            return;
        }
        String title = request.getParameter("title");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String description = request.getParameter("description");
        String condition = request.getParameter("condition");
        String features = request.getParameter("features");

        // Handle file uploads
        List<String> photoBase64List = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getContentType() != null && part.getContentType().startsWith("image/")) {
                byte[] photoBytes = part.getInputStream().readAllBytes();
                String photoBase64 = Base64.getEncoder().encodeToString(photoBytes);
                // Adding the prefix for Base64 data URL
                String photoBase64WithPrefix = "data:" + part.getContentType() + ";base64," + photoBase64;
                photoBase64List.add(photoBase64WithPrefix);
            }
        }

        // Create ItemModel object for item data
        ItemModel item = new ItemModel();
        item.setUserId(userId);
        item.setTitle(title);
        item.setCategoryId(categoryId);
        item.setDescription(description);
        item.setCondition(condition);
        item.setFeatures(features);
        item.setPhotos(photoBase64List);

        // Set createdAt and updatedAt fields
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        item.setCreatedAt(currentTimestamp);
        item.setUpdatedAt(currentTimestamp);

        try {
            // Save the item using ItemDAO
            itemDAO.addItem(item);
            response.sendRedirect("Dashboard.jsp");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during add item process", e);
            request.setAttribute("errorMessage", "An error occurred while adding the item. Please try again later.");
            fetchCategories(request, response); // Re-fetch categories and forward to AddItem.jsp
        }
    }

    private void fetchCategories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<CategoryModel> categories = itemDAO.getAllCategories();
            // Set categories as a request attribute
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("AddItem.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories", e);
            request.setAttribute("errorMessage", "An error occurred while fetching categories. Please try again later.");
            request.getRequestDispatcher("AddItem.jsp").forward(request, response);
        }
    }
}
