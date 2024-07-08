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

    // Instance of ItemDAO for interacting with the database
    private ItemDAO itemDAO = new ItemDAO();

    // Handles GET requests to display the AddItem.jsp page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch categories from the database and forward them to the AddItem.jsp page
        fetchCategories(request, response);
    }

    // Handles POST requests to add a new item
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current user's ID from the session
        HttpSession session = request.getSession();
        String userIdString = (String) session.getAttribute("userId");
        Integer userId;

        // Validate user ID
        if (userIdString == null || userIdString.isEmpty()) {
            LOGGER.log(Level.SEVERE, "User ID is missing from session");
            response.sendRedirect("Login.jsp");
            return;
        }

        try {
            // Convert the user ID from String to Integer
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid user ID format", e);
            response.sendRedirect("Login.jsp");
            return;
        }

        // Retrieve and validate item details from the request parameters
        String title = request.getParameter("title");
        String categoryIdString = request.getParameter("categoryId");
        String description = request.getParameter("description");
        String condition = request.getParameter("condition");
        String features = request.getParameter("features");

        if (title == null || title.trim().isEmpty() ||
            categoryIdString == null || categoryIdString.trim().isEmpty() ||
            description == null || description.trim().isEmpty() ||
            condition == null || condition.trim().isEmpty()) {

            request.setAttribute("errorMessage", "All fields are required. Please fill in all the details.");
            fetchCategories(request, response);
            return;
        }

        int categoryId;
        try {
            // Validate category ID
            categoryId = Integer.parseInt(categoryIdString);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid category ID format", e);
            request.setAttribute("errorMessage", "Invalid category ID. Please select a valid category.");
            fetchCategories(request, response);
            return;
        }

        // Handle file uploads (photos)
        List<String> photoBase64List = new ArrayList<>();
        for (Part part : request.getParts()) {
            // Check if the part is a file (image)
            if (part.getContentType() != null && part.getContentType().startsWith("image/")) {
                if (part.getSize() > 1024 * 1024 * 5) { // Check if the image size exceeds 5MB
                    request.setAttribute("errorMessage", "File size must be less than 5MB.");
                    fetchCategories(request, response);
                    return;
                }

                // Convert the image file to a Base64-encoded string
                byte[] photoBytes = part.getInputStream().readAllBytes();
                String photoBase64 = Base64.getEncoder().encodeToString(photoBytes);
                // Add a data URL prefix to the Base64 string for use in HTML
                String photoBase64WithPrefix = "data:" + part.getContentType() + ";base64," + photoBase64;
                photoBase64List.add(photoBase64WithPrefix);
            }
        }

        // Create an ItemModel object and set its properties
        ItemModel item = new ItemModel();
        item.setUserId(userId);
        item.setTitle(title);
        item.setCategoryId(categoryId);
        item.setDescription(description);
        item.setCondition(condition);
        item.setFeatures(features);
        item.setPhotos(photoBase64List);

        // Set createdAt and updatedAt timestamps
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        item.setCreatedAt(currentTimestamp);
        item.setUpdatedAt(currentTimestamp);

        try {
            // Save the item in the database using ItemDAO
            itemDAO.addItem(item);
            // Redirect to the dashboard page after successful item addition
            response.sendRedirect("Dashboard.jsp");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during add item process", e);
            // If there is an error, set an error message and re-fetch categories to forward back to AddItem.jsp
            request.setAttribute("errorMessage", "An error occurred while adding the item. Please try again later.");
            fetchCategories(request, response); // Re-fetch categories and forward to AddItem.jsp
        }
    }

    // Helper method to fetch categories from the database and forward to AddItem.jsp
    private void fetchCategories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve the list of categories from the database
            List<CategoryModel> categories = itemDAO.getAllCategories();
            // Set categories as a request attribute to be accessed in the JSP
            request.setAttribute("categories", categories);
            // Forward the request to AddItem.jsp to display the categories
            request.getRequestDispatcher("AddItem.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories", e);
            // If there is an error fetching categories, set an error message and forward to AddItem.jsp
            request.setAttribute("errorMessage", "An error occurred while fetching categories. Please try again later.");
            request.getRequestDispatcher("AddItem.jsp").forward(request, response);
        }
    }
}