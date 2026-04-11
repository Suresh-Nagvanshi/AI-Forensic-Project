package com.example.aiforensics.dto.scan;

public class ClassProbabilityDto {

    private String className;
    private double probability;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
