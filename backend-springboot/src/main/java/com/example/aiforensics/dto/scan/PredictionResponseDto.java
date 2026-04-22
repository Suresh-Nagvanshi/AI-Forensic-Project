package com.example.aiforensics.dto.scan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionResponseDto {

    @JsonProperty("overall_prediction")
    private String overallPrediction;

    @JsonProperty("overall_confidence")
    private double overallConfidence;

    @JsonProperty("risk_level")
    private String riskLevel;

    @JsonProperty("total_faces")
    private int totalFaces;

    @JsonProperty("annotated_image_url")
    private String annotatedImageUrl;

    @JsonProperty("report_message")
    private String reportMessage;

    public String getOverallPrediction() {
        return overallPrediction;
    }

    public void setOverallPrediction(String overallPrediction) {
        this.overallPrediction = overallPrediction;
    }

    public double getOverallConfidence() {
        return overallConfidence;
    }

    public void setOverallConfidence(double overallConfidence) {
        this.overallConfidence = overallConfidence;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public int getTotalFaces() {
        return totalFaces;
    }

    public void setTotalFaces(int totalFaces) {
        this.totalFaces = totalFaces;
    }

    public String getAnnotatedImageUrl() {
        return annotatedImageUrl;
    }

    public void setAnnotatedImageUrl(String annotatedImageUrl) {
        this.annotatedImageUrl = annotatedImageUrl;
    }

    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }
}