package com.example.aiforensics.service.scan;

import com.example.aiforensics.dto.scan.PredictionResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PredictionService {

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public PredictionResponseDto getPrediction(MultipartFile file) throws IOException {
        ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileAsResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(flaskApiUrl, HttpMethod.POST, requestEntity, Map.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("Empty response from Flask API");
        }

        Map<String, Object> payload = response.getBody();
        PredictionResponseDto prediction = new PredictionResponseDto();
        prediction.setPredictedClass(String.valueOf(payload.get("predicted_class")));
        prediction.setConfidence(asDouble(payload.get("confidence")));
        prediction.setPredictedIndex(asInteger(payload.get("predicted_index")));
        prediction.setNumClasses(asInteger(payload.get("num_classes")));
        prediction.setAllPredictions(convertPredictions(payload.get("all_predictions")));
        return prediction;
    }

    private Map<String, Double> convertPredictions(Object rawPredictions) {
        Map<String, Double> normalized = new LinkedHashMap<>();
        if (rawPredictions instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                normalized.put(String.valueOf(entry.getKey()), asDouble(entry.getValue()));
            }
        }
        return normalized;
    }

    private double asDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(String.valueOf(value));
    }

    private int asInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }
}
