package com.example.aiforensics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About AI Forensic | Deepfake and Image Manipulation Screening");
        model.addAttribute("pageDescription", "Learn how AI Forensic helps users screen images for deepfake, edited, GAN-generated, and real-media patterns through an AI-assisted review workflow.");
        model.addAttribute("canonicalUrl", "http://localhost:8080/about");
        model.addAttribute("ogType", "website");
        model.addAttribute("ogImage", "/images/seo/og-about.jpg");
        model.addAttribute("pageClass", "page-about");
        return "about/index";
    }
}