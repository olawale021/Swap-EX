package com.example.resources;

import com.example.models.users.UserDAO;
import com.example.models.users.UserModel;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/users")
public class UserResource {
    private static final Logger LOGGER = Logger.getLogger(UserResource.class.getName());
    private UserDAO userDAO = new UserDAO();

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(String userData) {
        try {
            JSONObject json = new JSONObject(userData);
            UserModel user = new UserModel();
            user.setUsername(json.getString("username"));
            user.setPhoneNumber(json.getInt("phoneNumber"));
            user.setPassword(json.getString("password"));
            JSONObject addressJson = json.getJSONObject("address");
            user.setAddress(addressJson);

            userDAO.addUser(user);

            return Response.status(Response.Status.CREATED).entity("User registered successfully!").build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error registering user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error registering user").build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(String loginData) {
        try {
            JSONObject json = new JSONObject(loginData);
            String username = json.getString("username");
            String password = json.getString("password");

            LOGGER.info("Received login request with data: " + loginData);

            UserModel user = userDAO.getUserByUsernameAndPassword(username, password);
            if (user != null) {
                LOGGER.info("Login successful for username: " + username);
                JSONObject userJson = new JSONObject();
                userJson.put("id", user.getId());  // Assuming UserModel has an getId() method
                userJson.put("username", user.getUsername());
                return Response.status(Response.Status.OK).entity(userJson.toString()).build();
            } else {
                LOGGER.warning("Invalid username or password for username: " + username);
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid username or password").build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error logging in user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error logging in user").build();
        }
    }

    @GET
    @Path("/logout")
    public Response logout(@Context HttpServletRequest request) {
        // Invalidate the session to log the user out
        request.getSession().invalidate();

        // Redirect to the homepage
        URI homepage = URI.create(request.getContextPath() + "/index.jsp");
        return Response.seeOther(homepage).build();
    }
}