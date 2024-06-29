package com.example.servlets.Items;

import com.example.models.items.ItemDAO;
import com.example.models.items.ItemModel;
import com.example.config.CloudinaryConfig;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/EditItemServlet")
@MultipartConfig
public class EditItemServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EditItemServlet.class.getName());
    private Cloudinary cloudinary = CloudinaryConfig.getCloudinary();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            ItemModel item = itemDAO.getItemById(itemId);
            if (item == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found");
                return;
            }

            request.setAttribute("item", item);
            request.setAttribute("categories", itemDAO.getAllCategories());
            request.getRequestDispatcher("edit-item.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving item", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while retrieving the item. Please try again later.");
        }
    }

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

        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String condition = request.getParameter("condition");

        ItemDAO itemDAO = new ItemDAO();
        try {
            ItemModel item = itemDAO.getItemById(itemId);
            if (item == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found");
                return;
            }

            item.setCategoryId(categoryId);
            item.setTitle(title);
            item.setDescription(description);
            item.setCondition(condition);

            // Handle photo deletions
            String[] deletedPhotos = request.getParameterValues("deletedPhotos");
            List<String> existingPhotos = new ArrayList<>(item.getPhotos());
            if (deletedPhotos != null) {
                for (String deletedPhoto : deletedPhotos) {
                    existingPhotos.remove(deletedPhoto);
                    // Optionally delete photo from storage if needed
                }
            }

            // Handle new photo uploads
            List<String> photos = new ArrayList<>(item.getPhotos());
            for (Part part : request.getParts()) {
                if (part.getName().equals("photos") && part.getSize() > 0) {
                    // Process uploaded photo
                    byte[] photoBytes = part.getInputStream().readAllBytes();
                    // Upload to Cloudinary
                    Map uploadResult = cloudinary.uploader().upload(photoBytes, ObjectUtils.emptyMap());
                    String photoUrl = (String) uploadResult.get("url");
                    photos.add(photoUrl);
                }
            }
            item.setPhotos(photos);

            boolean updated = itemDAO.updateItem(item);
            if (updated) {
                response.sendRedirect("UserItemsServlet");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update item");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating item", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the item. Please try again later.");
        }
    }
}
