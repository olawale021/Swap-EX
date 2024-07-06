package com.example.config;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class WekaClassifierExample {

    public static void main(String[] args) {
        BufferedReader reader = null;
        try {
            // Load the ARFF file
            reader = new BufferedReader(new FileReader("/Users/olawale/Desktop/javacs/MangoEX/src/main/resources/items.arff"));
            Instances data = new Instances(reader);

            // Set the class index (the attribute to be predicted)
            data.setClassIndex(0);

            // Apply StringToWordVector filter with adjusted parameters
            StringToWordVector filter = new StringToWordVector();
            filter.setAttributeIndices("2-last");
            filter.setWordsToKeep(100);
            filter.setMinTermFreq(2);
            filter.setLowerCaseTokens(true);
            filter.setOutputWordCounts(true);
            filter.setInputFormat(data);
            Instances filteredData = Filter.useFilter(data, filter);

            // Apply SMOTE to handle class imbalance
            SMOTE smote = new SMOTE();
            smote.setPercentage(100);
            smote.setNearestNeighbors(5);
            smote.setInputFormat(filteredData);
            filteredData = Filter.useFilter(filteredData, smote);

            // Check if SMote has been correctly applied
            if (filteredData == null) {
                throw new Exception("SMote did not correctly process the data.");
            }

            // Evaluate and save multiple classifiers
            evaluateAndSaveClassifier(new RandomForest(), filteredData, "RandomForest.model", filter);
            evaluateAndSaveClassifier(new J48(), filteredData, "J48.model", filter);
            evaluateAndSaveClassifier(new SMO(), filteredData, "SVM.model", filter);
            evaluateAndSaveClassifier(new IBk(), filteredData, "KNN.model", filter);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void evaluateAndSaveClassifier(Classifier classifier, Instances data, String modelFileName, Filter filter) throws Exception {
        // Hyperparameter tuning can be done here for each classifier
        if (classifier instanceof RandomForest) {
            ((RandomForest) classifier).setNumIterations(150); // example tuning
            ((RandomForest) classifier).setMaxDepth(15); // example tuning
        } else if (classifier instanceof J48) {
            ((J48) classifier).setConfidenceFactor(0.25f); // example tuning
            ((J48) classifier).setMinNumObj(5); // example tuning
        } else if (classifier instanceof SMO) {
            ((SMO) classifier).setC(1.0); // example tuning
        } else if (classifier instanceof IBk) {
            ((IBk) classifier).setKNN(5); // example tuning
        }

        // Use a FilteredClassifier to apply filters and classifier together
        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setFilter(filter);
        filteredClassifier.setClassifier(classifier);

        // Perform 10-fold cross-validation
        Evaluation evaluation = new Evaluation(data);
        evaluation.crossValidateModel(filteredClassifier, data, 10, new Random(1));

        // Print the evaluation results
        System.out.println("\nResults for " + modelFileName + "\n======");
        System.out.println(evaluation.toSummaryString());
        System.out.println(evaluation.toClassDetailsString());
        System.out.println(evaluation.toMatrixString());

        // Train the classifier on the entire dataset
        filteredClassifier.buildClassifier(data);

        // Save the trained model and the filter to a file
        SerializationHelper.write("/Users/olawale/Desktop/javacs/MangoEX/src/main/resources/models/" + modelFileName, filteredClassifier);
        SerializationHelper.write("/Users/olawale/Desktop/javacs/MangoEX/src/main/resources/models/" + modelFileName + ".filter", filter);

        System.out.println("Model and filter saved to: " + modelFileName);

        // Example of using the trained model to make predictions
        System.out.println("\nPredictions for " + modelFileName + ":");
        for (int i = 0; i < data.numInstances(); i++) {
            double actualClass = data.instance(i).classValue();
            double predictedClass = filteredClassifier.classifyInstance(data.instance(i));
            System.out.println("Actual: " + data.classAttribute().value((int) actualClass) +
                    ", Predicted: " + data.classAttribute().value((int) predictedClass));
        }
    }
}
