//package com.example.resources;
//
//import com.example.config.DBConnection;
//import com.example.models.messages.MessageDAO;
//import com.example.models.messages.MessageModel;
//import org.json.JSONObject;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@Path("/messages")
//public class MessageResource {
//    private static final Logger LOGGER = Logger.getLogger(MessageResource.class.getName());
//
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response sendMessage(String messageData) {
//        try (Connection connection = DBConnection.getConnection()) {
//            MessageDAO messageDAO = new MessageDAO(connection);
//
//            JSONObject json = new JSONObject(messageData);
//            int exchangeId = json.getInt("exchangeId");
//            int senderId = json.getInt("senderId");
//            int receiverId = json.getInt("receiverId");
//            String content = json.getString("content");
//
//            // Create message record
//            MessageModel message = new MessageModel();
//            message.setExchangeId(exchangeId);
//            message.setSenderId(senderId);
//            message.setReceiverId(receiverId);
//            message.setContent(content);
//            message.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//            messageDAO.createMessage(message);
//
//            return Response.status(Response.Status.CREATED).entity("Message sent successfully").build();
//        } catch (SQLException e) {
//            LOGGER.log(Level.SEVERE, "Error sending message", e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error sending message").build();
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Unexpected error", e);
//            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid message data").build();
//        }
//    }
//
//    @GET
//    @Path("/exchange/{exchangeId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getMessagesByExchangeId(@PathParam("exchangeId") int exchangeId) {
//        try (Connection connection = DBConnection.getConnection()) {
//            MessageDAO messageDAO = new MessageDAO(connection);
//            return Response.ok(messageDAO.getMessagesByExchangeId(exchangeId)).build();
//        } catch (SQLException e) {
//            LOGGER.log(Level.SEVERE, "Error fetching messages", e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error fetching messages").build();
//        }
//    }
//}
