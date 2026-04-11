package com.example.aiforensics.controller;

import com.example.aiforensics.dto.site.FeedbackRequestDto;
import com.example.aiforensics.service.site.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/feedback")
    public String feedbackPage(Model model) {
        model.addAttribute("pageTitle", "Feedback | AI Forensic");
        model.addAttribute("pageDescription", "Share your feedback about AI Forensic, including UI clarity, result wording, and feature suggestions.");
        model.addAttribute("canonicalUrl", "http://localhost:8080/feedback");
        model.addAttribute("ogType", "website");
        model.addAttribute("ogImage", "/images/seo/og-feedback.jpg");
        model.addAttribute("pageClass", "page-feedback");

        if (!model.containsAttribute("feedbackRequest")) {
            model.addAttribute("feedbackRequest", new FeedbackRequestDto());
        }

        return "feedback/index";
    }

    @PostMapping("/feedback")
    public String submitFeedback(
            @Valid @ModelAttribute("feedbackRequest") FeedbackRequestDto feedbackRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        model.addAttribute("pageTitle", "Feedback | AI Forensic");
        model.addAttribute("pageDescription", "Share your feedback about AI Forensic, including UI clarity, result wording, and feature suggestions.");
        model.addAttribute("canonicalUrl", "http://localhost:8080/feedback");
        model.addAttribute("ogType", "website");
        model.addAttribute("ogImage", "/images/seo/og-feedback.jpg");
        model.addAttribute("pageClass", "page-feedback");

        if (bindingResult.hasErrors()) {
            return "feedback/index";
        }

        feedbackService.save(feedbackRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Thank you for your feedback.");
        return "redirect:/feedback";
    }
}