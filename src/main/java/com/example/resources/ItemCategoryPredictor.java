package com.example.resources;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;

public class ItemCategoryPredictor {
    private FilteredClassifier classifier;
    private Instances dataStructure;

    public ItemCategoryPredictor() {
        try {
            // Load the saved model
            classifier = (FilteredClassifier) weka.core.SerializationHelper.read("/Users/olawale/Desktop/javacs/MangoEX/src/main/resources/models/RandomsForest.model");

            // Create a structure that matches your original ARFF file
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("item_category", getClassValues()));
            attributes.add(new Attribute("item_name", (ArrayList<String>) null));
            attributes.add(new Attribute("item_features", (ArrayList<String>) null));

            dataStructure = new Instances("TestInstances", attributes, 0);
            dataStructure.setClassIndex(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getClassValues() {
        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("Clothing");
        classValues.add("Collectables");
        classValues.add("Electronics");
        classValues.add("Furniture");
        classValues.add("Jewellery_and_Watches");
        classValues.add("Video_Games_and_Consoles");
        classValues.add("Vehicles");
        return classValues;
    }

    public String predictCategory(String itemName, String itemFeatures) {
        try {
            // Create a new instance
            Instance newInstance = new DenseInstance(3);
            newInstance.setDataset(dataStructure);
            newInstance.setValue(dataStructure.attribute("item_name"), itemName);
            newInstance.setValue(dataStructure.attribute("item_features"), itemFeatures);

            // Add the instance to a new Instances object
            Instances testInstances = new Instances(dataStructure);
            testInstances.add(newInstance);

            // Apply the classifier (which includes the filter)
            double predictedClass = classifier.classifyInstance(testInstances.firstInstance());

            return dataStructure.classAttribute().value((int) predictedClass);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
