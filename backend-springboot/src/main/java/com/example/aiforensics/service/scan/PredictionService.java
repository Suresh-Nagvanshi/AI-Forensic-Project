package com.example.aiforensics.service.scan;

import com.example.aiforensics.dto.scan.FaceResultDto;
import com.example.aiforensics.dto.scan.ForensicReportDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PredictionService {

    private static final String FLASK_BASE = "http://127.0.0.1:5000";
    private final RestTemplate rest = new RestTemplate();

    public ForensicReportDto predict(MultipartFile file) throws Exception {
        ByteArrayResource res = new ByteArrayResource(file.getBytes()) {
            @Override public String getFilename() { return file.getOriginalFilename(); }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        HttpHeaders fh = new HttpHeaders();
        fh.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        body.add("file", new HttpEntity<>(res, fh));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ResponseEntity<ForensicReportDto> resp = rest.exchange(
            FLASK_BASE + "/predict", HttpMethod.POST,
            new HttpEntity<>(body, headers), ForensicReportDto.class);

        ForensicReportDto report = resp.getBody();
        if (report == null) throw new RuntimeException("Empty response from AI API");

        // Fix relative URLs → absolute so Thymeleaf <img> works
        if (report.getAnnotatedImageUrl() != null && report.getAnnotatedImageUrl().startsWith("/"))
            report.setAnnotatedImageUrl(FLASK_BASE + report.getAnnotatedImageUrl());

        if (report.getFaces() != null)
            for (FaceResultDto f : report.getFaces())
                if (f.getHeatmapUrl() != null && f.getHeatmapUrl().startsWith("/"))
                    f.setHeatmapUrl(FLASK_BASE + f.getHeatmapUrl());

        return report;
    }
}