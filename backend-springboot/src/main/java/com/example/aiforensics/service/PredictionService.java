package com.example.aiforensics.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class PredictionService {

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getPrediction(MultipartFile file) throws IOException {

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

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                flaskApiUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        if (response.getBody() == null) {
            throw new RuntimeException("Empty response from Flask API");
        }

        return response.getBody();
    }
}