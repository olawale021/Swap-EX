package com.example.servlets.Items;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.config.CloudinaryConfig;
import com.example.models.items.ItemDAO;
import com.example.models.items.ItemModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/UpdateItemServlet")
@MultipartConfig
public class UpdateItemServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UpdateItemServlet.class.getName());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private ItemDAO itemDAO = new ItemDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        String description = request.getParameter("description");
        String condition = request.getParameter("condition");
        String features = request.getParameter("features");
        int categoryId = Integer.parseInt(request.getParameter("category"));

        ItemModel item = new ItemModel();
        item.setId(itemId);
        item.setDescription(description);
        item.setCondition(condition);
        item.setCategoryId(categoryId);
        item.setFeatures(features);

        // Handle existing images
        String[] existingImages = request.getParameterValues("existingPhotos");
        if (existingImages != null) {
            for (String image : existingImages) {
                if (image != null && !image.trim().isEmpty()) {
                    item.getPhotos().add(image);
                }
            }
        }

        // Handle new images
        for (Part part : request.getParts()) {
            if (part.getName().equals("photos") && part.getSize() > 0) {
                // Use Cloudinary to upload the image
                String photoUrl = uploadImageToCloudinary(part);
                item.getPhotos().add(photoUrl);
            }
        }

        try {
            // Update the item using ItemDAO
            itemDAO.updateItem(item);
            LOGGER.info("Item updated successfully.");
            response.sendRedirect("UserItemsServlet?success=true");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating item", e);
            response.sendRedirect("UserItemsServlet?error=update-error");
        }
    }

    private String uploadImageToCloudinary(Part part) throws IOException {
        Cloudinary cloudinary = CloudinaryConfig.getCloudinary();

        Map uploadResult;
        try (InputStream inputStream = part.getInputStream()) {
            File tempFile = File.createTempFile("upload-", ".tmp");
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                inputStream.transferTo(out);
            }
            uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
            tempFile.delete(); // Clean up the temp file after upload
        }
        return uploadResult.get("url").toString();
    }

    private String convertPhotosListToJson(List<String> photos) {
        if (photos == null || photos.isEmpty()) {
            return "[]";
        }
        return "[\"" + String.join("\",\"", photos) + "\"]";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Either handle the GET request or redirect to an appropriate page
        response.sendRedirect("UserItemsServlet");
    }
}
