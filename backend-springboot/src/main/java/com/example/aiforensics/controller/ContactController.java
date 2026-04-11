package com.example.aiforensics.controller;

import com.example.aiforensics.dto.site.ContactRequestDto;
import com.example.aiforensics.service.site.ContactService;
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
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contact")
    public String contactPage(Model model) {
        model.addAttribute("pageTitle", "Contact AI Forensic | Support, Feedback and Collaboration");
        model.addAttribute("pageDescription", "Contact AI Forensic for product questions, support, deployment discussions, or collaboration related to AI-assisted image screening.");
        model.addAttribute("canonicalUrl", "http://localhost:8080/contact");
        model.addAttribute("ogType", "website");
        model.addAttribute("ogImage", "/images/seo/og-contact.jpg");
        model.addAttribute("pageClass", "page-contact");

        if (!model.containsAttribute("contactRequest")) {
            model.addAttribute("contactRequest", new ContactRequestDto());
        }

        return "contact/index";
    }

    @PostMapping("/contact")
    public String submitContactForm(@Valid @ModelAttribute("contactRequest") ContactRequestDto contactRequest,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        model.addAttribute("pageTitle", "Contact AI Forensic | Support, Feedback and Collaboration");
        model.addAttribute("pageDescription", "Contact AI Forensic for product questions, support, deployment discussions, or collaboration related to AI-assisted image screening.");
        model.addAttribute("canonicalUrl", "http://localhost:8080/contact");
        model.addAttribute("ogType", "website");
        model.addAttribute("ogImage", "/images/seo/og-contact.jpg");
        model.addAttribute("pageClass", "page-contact");

        if (bindingResult.hasErrors()) {
            return "contact/index";
        }

        contactService.save(contactRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Your message has been sent successfully.");
        return "redirect:/contact";
    }
}