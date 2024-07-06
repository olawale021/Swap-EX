package com.example.config;

import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Weka {

    public static void main(String[] args) {
        try {
            // Load the ARFF file
            DataSource source = new DataSource("/Users/olawale/Desktop/javacs/MangoEX/src/main/resources/items.arff");
            Instances data = source.getDataSet();

            // Set the class index (the attribute to be predicted)
            data.setClassIndex(0);

            // Create the StringToWordVector filter
            StringToWordVector filter = new StringToWordVector();
            filter.setAttributeIndices("2-last");

            // Create the RandomForest classifier
            RandomForest rf = new RandomForest();
            rf.setNumIterations(100);

            // Create the FilteredClassifier
            FilteredClassifier fc = new FilteredClassifier();
            fc.setFilter(filter);
            fc.setClassifier(rf);

            // Train the classifier
            fc.buildClassifier(data);

            // Save the model
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("/Users/olawale/Desktop/javacs/MangoEX/src/main/resources/models/RandomsForest.model"));
            out.writeObject(fc);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
