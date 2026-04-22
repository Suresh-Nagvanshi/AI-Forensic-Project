package com.example.aiforensics.dto.scan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FaceResultDto {
    @JsonProperty("face_id") private int faceId;
    @JsonProperty("bbox") private List<Integer> bbox;
    @JsonProperty("detection_confidence") private double detectionConfidence;
    @JsonProperty("prediction") private String prediction;
    @JsonProperty("confidence") private double confidence;
    @JsonProperty("class_scores") private Map<String, Double> classScores;
    @JsonProperty("dominant_emotion") private String dominantEmotion;
    @JsonProperty("emotion_scores") private Map<String, Double> emotionScores;
    @JsonProperty("heatmap_url") private String heatmapUrl;
    @JsonProperty("suspicious_region") private boolean suspiciousRegion;

    public int getFaceId() { return faceId; }
    public List<Integer> getBbox() { return bbox; }
    public double getDetectionConfidence() { return detectionConfidence; }
    public String getPrediction() { return prediction; }
    public double getConfidence() { return confidence; }
    public Map<String, Double> getClassScores() { return classScores; }
    public String getDominantEmotion() { return dominantEmotion; }
    public Map<String, Double> getEmotionScores() { return emotionScores; }
    public String getHeatmapUrl() { return heatmapUrl; }
    public void setHeatmapUrl(String u) { this.heatmapUrl = u; }
    public boolean isSuspiciousRegion() { return suspiciousRegion; }
}
