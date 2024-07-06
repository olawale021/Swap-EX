package com.example.config;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class ModelPredictor {

    private Classifier model;
    private Filter filter;

    public ModelPredictor(String modelPath, String filterPath) throws Exception {
        // Load the model
        model = (Classifier) SerializationHelper.read(modelPath);
        // Load the filter
        filter = (Filter) SerializationHelper.read(filterPath);
    }

    public double classifyInstance(Instances data, int instanceIndex) throws Exception {
        // Apply the same filter to the test data
        filter.setInputFormat(data);
        Instances filteredData = Filter.useFilter(data, filter);
        return model.classifyInstance(filteredData.instance(instanceIndex));
    }

    public static void main(String[] args) {
        try {
            // Load data
            DataSource source = new DataSource("/Users/olawale/Desktop/javacs/MangoEX/src/main/resources/items.arff");
            Instances data = source.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);

            // Load model and filter
            ModelPredictor predictor = new ModelPredictor("/Users/olawale/Desktop/javacs/MangoEX/src/main/resources/models/RandomForest.model",
                    "/Users/olawale/Desktop/javacs/MangoEX/src/main/resources/models/RandomForest.model.filter");

            // Classify an instance
            double prediction = predictor.classifyInstance(data, 0); // Classify the first instance
            System.out.println("Prediction: " + data.classAttribute().value((int) prediction));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
