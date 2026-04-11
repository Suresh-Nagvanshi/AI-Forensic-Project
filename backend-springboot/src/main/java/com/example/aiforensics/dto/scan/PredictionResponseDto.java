package com.example.aiforensics.dto.scan;

import java.util.Map;

public class PredictionResponseDto {

    private String predictedClass;
    private double confidence;
    private int predictedIndex;
    private int numClasses;
    private Map<String, Double> allPredictions;

    public String getPredictedClass() {
        return predictedClass;
    }

    public void setPredictedClass(String predictedClass) {
        this.predictedClass = predictedClass;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public int getPredictedIndex() {
        return predictedIndex;
    }

    public void setPredictedIndex(int predictedIndex) {
        this.predictedIndex = predictedIndex;
    }

    public int getNumClasses() {
        return numClasses;
    }

    public void setNumClasses(int numClasses) {
        this.numClasses = numClasses;
    }

    public Map<String, Double> getAllPredictions() {
        return allPredictions;
    }

    public void setAllPredictions(Map<String, Double> allPredictions) {
        this.allPredictions = allPredictions;
    }
}
