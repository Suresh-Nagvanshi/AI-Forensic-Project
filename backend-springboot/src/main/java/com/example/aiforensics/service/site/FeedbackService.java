package com.example.aiforensics.service.site;

import com.example.aiforensics.dto.site.FeedbackRequestDto;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    public void save(FeedbackRequestDto request) {
        System.out.println("Feedback received from: " + request.getEmail());
    }
}