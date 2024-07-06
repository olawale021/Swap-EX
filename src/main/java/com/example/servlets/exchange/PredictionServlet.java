package com.example.servlets.exchange;

import com.example.resources.ItemCategoryPredictor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/predict")
public class PredictionServlet extends HttpServlet {
    private ItemCategoryPredictor predictor;

    @Override
    public void init() throws ServletException {
        predictor = new ItemCategoryPredictor();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemName = request.getParameter("itemName");
        String itemFeatures = request.getParameter("itemFeatures");

        if (itemName == null || itemFeatures == null || itemName.isEmpty() || itemFeatures.isEmpty()) {
            request.setAttribute("error", "Please provide both item name and features.");
        } else {
            String prediction = predictor.predictCategory(itemName, itemFeatures);
            request.setAttribute("prediction", prediction);
        }

        request.getRequestDispatcher("/Predict.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/Predict.jsp").forward(request, response);
    }
}
