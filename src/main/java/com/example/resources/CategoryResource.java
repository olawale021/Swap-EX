package com.example.resources;

import com.example.models.category.CategoryDAO;
import com.example.models.category.CategoryModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/categories")
public class CategoryResource {

    private CategoryDAO categoryDAO = new CategoryDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCategories() {
        try {
            List<CategoryModel> categories = categoryDAO.getAllCategories();
            return Response.ok(categories).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching categories")
                    .build();
        }
    }
}
