package com.example.servlets.exchange;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

@WebServlet("/PredictServlet")
public class PredictServlet extends HttpServlet {

    // Classifier instance to hold the loaded model
    private Classifier classifier;

    // Initialize the servlet and load the pre-trained model
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Load the RandomForest model from the file system
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/Users/olawale/Desktop/javacs/MangoEX/src/main/resources/models/RandomsForest2.model"));
            classifier = (Classifier) ois.readObject();
            ois.close();
        } catch (Exception e) {
            // Handle any errors during model loading
            throw new ServletException("Error loading model", e);
        }
    }

    // Handle GET requests to forward the user to the prediction page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("new-predict.jsp").forward(request, response);
    }

    // Handle POST requests to make predictions using the loaded model
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve input parameters from the form
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        String type = request.getParameter("type");
        String colour = request.getParameter("colour");
        String ram = request.getParameter("ram");
        String size = request.getParameter("size");

        try {
            // Define the attributes used for prediction
            ArrayList<Attribute> attributes = new ArrayList<>();
            // Possible values for each attribute
            ArrayList<String> brandValues = new ArrayList<String>() {{
                add("IKEA");
                add("HP");
                add("LG");
                add("River_Island");
                add("DELL");
                add("Sony");
                add("Apple");
                add("ASUS");
                add("Samsung");
                add("Crayola");
                add("Nike");
                add("Whirlpool");
                add("Adidas");
                add("None");
            }};
            ArrayList<String> modelValues = new ArrayList<String>() {{
                add("Galaxy_S21");
                add("G5_15");
                add("iPhone_13_Pro_Max");
                add("WH-1000XM4");
                add("MacBook_Air");
                add("Spectre_x360");
                add("ROG_Strix");
                add("Ultra_One");
                add("None");
            }};
            ArrayList<String> typeValues = new ArrayList<String>() {{
                add("Wardrobe");
                add("Monitor");
                add("Laptop");
                add("Table");
                add("Jumper_Dress");
                add("Bookshelf");
                add("Top");
                add("Washer");
                add("Dryer");
                add("Phone");
                add("Camera");
                add("Vacuum_Cleaner");
                add("None");
            }};
            ArrayList<String> colourValues = new ArrayList<String>() {{
                add("Black");
                add("Grey");
                add("White");
                add("Blue");
                add("Red");
                add("Brown");
                add("Pink");
                add("Yellow");
                add("Silver");
                add("None");
            }};
            ArrayList<String> ramValues = new ArrayList<String>() {{
                add("8GB");
                add("16GB");
                add("32GB");
                add("None");
            }};
            ArrayList<String> sizeValues = new ArrayList<String>() {{
                add("S");
                add("M");
                add("L");
                add("XL");
                add("None");
            }};
            ArrayList<String> categoryValues = new ArrayList<String>() {{
                add("Furniture");
                add("Electronics");
                add("Clothing");
            }};

            // Add attributes to the list with their possible values
            attributes.add(new Attribute("Brand", brandValues));
            attributes.add(new Attribute("Model", modelValues));
            attributes.add(new Attribute("Type", typeValues));
            attributes.add(new Attribute("Colour", colourValues));
            attributes.add(new Attribute("RAM", ramValues));
            attributes.add(new Attribute("Size", sizeValues));
            attributes.add(new Attribute("Category", categoryValues));

            // Create a new Instances object to hold the data
            Instances data = new Instances("TestInstances", attributes, 0);
            data.setClassIndex(data.numAttributes() - 1); // Set the class attribute (the one to predict)

            // Create a DenseInstance to represent the user's input
            DenseInstance instance = new DenseInstance(data.numAttributes());
            instance.setValue(attributes.get(0), brand);
            instance.setValue(attributes.get(1), model);
            instance.setValue(attributes.get(2), type);
            instance.setValue(attributes.get(3), colour);
            instance.setValue(attributes.get(4), ram);
            instance.setValue(attributes.get(5), size);

            // Add the instance to the dataset
            data.add(instance);

            // Predict the category using the classifier
            double predictionIndex = classifier.classifyInstance(data.instance(0));
            String prediction = data.classAttribute().value((int) predictionIndex);

            // Set the prediction result as a request attribute
            request.setAttribute("prediction", prediction);
        } catch (Exception e) {
            // Handle any errors during prediction
            throw new ServletException("Error predicting category", e);
        }

        // Forward the request to the prediction results page
        request.getRequestDispatcher("new-predict.jsp").forward(request, response);
    }
}