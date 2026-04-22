package com.example.aiforensics.dto.scan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForensicReportDto {
    @JsonProperty("report_id") private String reportId;
    @JsonProperty("overall_prediction") private String overallPrediction;
    @JsonProperty("overall_confidence") private double overallConfidence;
    @JsonProperty("risk_level") private String riskLevel;
    @JsonProperty("total_faces") private int totalFaces;
    @JsonProperty("uploaded_file") private String uploadedFile;
    @JsonProperty("annotated_image_url") private String annotatedImageUrl;
    @JsonProperty("annotated_saved") private boolean annotatedSaved;
    @JsonProperty("message") private String message;
    @JsonProperty("faces") private List<FaceResultDto> faces;

    public String getReportId() { return reportId; }
    public String getOverallPrediction() { return overallPrediction; }
    public double getOverallConfidence() { return overallConfidence; }
    public String getRiskLevel() { return riskLevel; }
    public int getTotalFaces() { return totalFaces; }
    public String getUploadedFile() { return uploadedFile; }
    public String getAnnotatedImageUrl() { return annotatedImageUrl; }
    public void setAnnotatedImageUrl(String u) { this.annotatedImageUrl = u; }
    public boolean isAnnotatedSaved() { return annotatedSaved; }
    public String getMessage() { return message; }
    public List<FaceResultDto> getFaces() { return faces; }
}
