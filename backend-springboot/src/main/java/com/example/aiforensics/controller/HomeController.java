package com.example.aiforensics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "AI Forensic | Detect Deepfake, Edited, GAN and Real Images");
        model.addAttribute("pageDescription",
                "AI Forensic helps users analyze uploaded images for deepfake, edited, GAN-generated, or real-media patterns using AI-assisted forensic screening.");
        model.addAttribute("canonicalUrl", "http://localhost:8080/");
        model.addAttribute("ogType", "website");
        model.addAttribute("ogImage", "/images/seo/og-home.jpg");
        model.addAttribute("pageClass", "page-home");

        return "home/index";
    }
}