package com.example.resources;

import com.example.models.items.ItemDAO;
import com.example.models.items.ItemModel;
import com.example.config.CloudinaryConfig;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/items")
public class ItemResource {
    private static final Logger LOGGER = Logger.getLogger(ItemResource.class.getName());
    private ItemDAO itemDAO = new ItemDAO();
    private Cloudinary cloudinary = CloudinaryConfig.getCloudinary();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllItems() {
        try {
            List<ItemModel> items = itemDAO.getAllItems();
            return Response.ok(items).build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching items from the database", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error fetching items from the database").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unexpected error").build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemById(@PathParam("id") int id) {
        try {
            ItemModel item = itemDAO.getItemById(id);
            if (item != null) {
                return Response.ok(item).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Item not found").build();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching item", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error fetching item").build();
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItem(String itemData) {
        try {
            JSONObject json = new JSONObject(itemData);
            ItemModel item = new ItemModel();
            item.setUserId(json.getInt("userId"));
            item.setCategoryId(json.getInt("categoryId"));
            item.setTitle(json.getString("title"));
            item.setDescription(json.getString("description"));
            item.setCondition(json.getString("condition"));

            // Handle photo uploads
            List<String> photoUrls = new ArrayList<>();
            if (json.has("photos")) {
                JSONArray photoBase64Array = json.getJSONArray("photos");
                for (int i = 0; i < photoBase64Array.length(); i++) {
                    String photoBase64 = photoBase64Array.getString(i);
                    Map uploadResult = cloudinary.uploader().upload(photoBase64, ObjectUtils.emptyMap());
                    photoUrls.add((String) uploadResult.get("url"));
                }
            }
            item.setPhotos(photoUrls);

            // Set timestamps
            Timestamp now = new Timestamp(System.currentTimeMillis());
            item.setCreatedAt(now);
            item.setUpdatedAt(now);

            int itemId = itemDAO.addItem(item);

            if (itemId > 0) {
                JSONObject response = new JSONObject();
                response.put("message", "Item added successfully");
                response.put("itemId", itemId);
                return Response.status(Response.Status.CREATED).entity(response.toString()).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add item").build();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding item to the database", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error adding item to the database").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid item data").build();
        }
    }

    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserItems(@PathParam("userId") int userId) {
        try {
            List<ItemModel> items = itemDAO.getItemsByUserId(userId);
            return Response.ok(items).build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching user items", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error fetching user items").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItem(@PathParam("id") int id) {
        try {
            boolean deleted = itemDAO.deleteItem(id);
            if (deleted) {
                return Response.ok().entity("Item deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Item not found").build();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting item", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting item").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("id") int id, String itemData) {
        try {
            JSONObject json = new JSONObject(itemData);
            ItemModel item = new ItemModel();
            item.setId(id);
            item.setDescription(json.getString("description"));
            item.setCondition(json.getString("condition"));
            item.setCategoryId(json.getInt("categoryId"));

            // Get existing item to retain its photos
            ItemModel existingItem = itemDAO.getItemById(id);
            List<String> photoUrls = new ArrayList<>(existingItem.getPhotos());

            // Handle photos
            if (json.has("photos")) {
                JSONArray photosArray = json.getJSONArray("photos");
                for (int i = 0; i < photosArray.length(); i++) {
                    String photo = photosArray.getString(i);
                    if (photo.startsWith("http")) {
                        // Existing URL
                        photoUrls.add(photo);
                    } else {
                        // New base64 image
                        Map uploadResult = cloudinary.uploader().upload(photo, ObjectUtils.emptyMap());
                        photoUrls.add((String) uploadResult.get("url"));
                    }
                }
            }
            item.setPhotos(photoUrls);

            boolean updated = itemDAO.updateItem(item);
            if (updated) {
                return Response.ok("Item updated successfully").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update item").build();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating item", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating item").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid item data").build();
        }
    }
}
