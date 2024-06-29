package com.example.resources;

import com.example.models.exchanges.ExchangeDAO;
import com.example.models.exchanges.ExchangeModel;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/exchanges")
public class ExchangeResource {
    private static final Logger LOGGER = Logger.getLogger(ExchangeResource.class.getName());
    private ExchangeDAO exchangeDAO = new ExchangeDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createExchange(String exchangeData) {
        try {
            JSONObject json = new JSONObject(exchangeData);
            ExchangeModel exchange = new ExchangeModel();
            exchange.setItemId(json.getInt("itemId"));
            exchange.setInterestedUserId(json.getInt("interestedUserId"));
            exchange.setOwnerId(json.getInt("ownerId"));
            exchange.setOwnerUsername(json.getString("ownerUsername"));
            exchange.setInterestedUserUsername(json.getString("interestedUserUsername"));
            exchange.setItemTitle(json.getString("itemTitle"));
            exchange.setStatus("pending");
            exchange.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            exchange.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            exchangeDAO.createExchange(exchange);

            JSONObject response = new JSONObject();
            response.put("message", "Exchange created successfully");
            response.put("exchangeId", exchange.getId());
            return Response.status(Response.Status.CREATED).entity(response.toString()).build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating exchange", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating exchange").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid exchange data").build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExchangeById(@PathParam("id") int id) {
        try {
            ExchangeModel exchange = exchangeDAO.getExchangeById(id);
            if (exchange != null) {
                return Response.ok(exchange).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Exchange not found").build();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching exchange", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error fetching exchange").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllExchanges() {
        try {
            List<ExchangeModel> exchanges = exchangeDAO.getAllExchanges();
            return Response.ok(exchanges).build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching exchanges", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error fetching exchanges").build();
        }
    }
}
