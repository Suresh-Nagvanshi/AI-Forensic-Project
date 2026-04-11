package com.example.aiforensics.service.site;

import com.example.aiforensics.dto.site.ContactRequestDto;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    public void save(ContactRequestDto request) {
        System.out.println("Contact form received from: " + request.getEmail());
    }
}