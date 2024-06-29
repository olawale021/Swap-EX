package com.example.servlets.Items;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.config.CloudinaryConfig;
import com.example.models.items.ItemModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        String description = request.getParameter("description");
        String condition = request.getParameter("condition");
        int categoryId = Integer.parseInt(request.getParameter("category"));

        ItemModel item = new ItemModel();
        item.setId(itemId);
        item.setDescription(description);
        item.setCondition(condition);
        item.setCategoryId(categoryId);

        // Handle existing images
        String[] existingImages = request.getParameterValues("existingPhotos");
        if (existingImages != null) {
            for (String image : existingImages) {
                item.getPhotos().add(image);
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

        String photosJson = convertPhotosListToJson(item.getPhotos());
        item.setPhotosJson(photosJson);

        String apiUrl = constructApiUrl(request, "/api/items/" + itemId);
        LOGGER.info("API URL: " + apiUrl);

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = OBJECT_MAPPER.writeValueAsString(item);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            String responseMessage = readResponseBody(conn);

            LOGGER.info("Response Code: " + responseCode);
            LOGGER.info("Response Message: " + responseMessage);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                LOGGER.info("Item updated successfully.");
                response.sendRedirect("UserItemsServlet?success=true");
            } else {
                LOGGER.warning("Failed to update item. Response Code: " + responseCode);
                response.sendRedirect("UserItemsServlet?error=update-failed");
            }

            conn.disconnect();
        } catch (Exception e) {
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

    private String constructApiUrl(HttpServletRequest request, String path) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + path;
    }

    private String readResponseBody(HttpURLConnection conn) throws IOException {
        InputStream is = (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) ? conn.getInputStream() : conn.getErrorStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
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
