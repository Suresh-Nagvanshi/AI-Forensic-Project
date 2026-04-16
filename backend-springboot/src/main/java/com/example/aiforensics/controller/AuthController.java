package com.example.aiforensics.controller;

import com.example.aiforensics.dto.SignupRequest;
import com.example.aiforensics.service.auth.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Sign In | AI Forensic");
        model.addAttribute("pageDescription", "Sign in to access AI Forensic image scanning and review workflows.");
        model.addAttribute("canonicalUrl", "http://localhost:8080/login");
        model.addAttribute("ogType", "website");
        model.addAttribute("ogImage", "/images/seo/og-auth.jpg");
        model.addAttribute("pageClass", "page-auth");
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        if (!model.containsAttribute("signupRequest")) {
            model.addAttribute("signupRequest", new SignupRequest());
        }

        model.addAttribute("pageTitle", "Create Account | AI Forensic");
        model.addAttribute("pageDescription", "Create your AI Forensic account to start scanning and reviewing suspicious images.");
        model.addAttribute("canonicalUrl", "http://localhost:8080/signup");
        model.addAttribute("ogType", "website");
        model.addAttribute("ogImage", "/images/seo/og-auth.jpg");
        model.addAttribute("pageClass", "page-auth");

        return "auth/signup";
    }

    @PostMapping("/signup")
    public String registerUser(
            @Valid @ModelAttribute("signupRequest") SignupRequest signupRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {

        System.out.println("POST /signup reached");
        System.out.println("fullName = " + signupRequest.getFullName());
        System.out.println("email = " + signupRequest.getEmail());
        System.out.println("password = " + signupRequest.getPassword());
        System.out.println("confirmPassword = " + signupRequest.getConfirmPassword());
        System.out.println("binding errors = " + bindingResult.hasErrors());
        System.out.println("all errors = " + bindingResult.getAllErrors());

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Create Account | AI Forensic");
            model.addAttribute("pageDescription", "Create your AI Forensic account to start scanning and reviewing suspicious images.");
            model.addAttribute("canonicalUrl", "http://localhost:8080/signup");
            model.addAttribute("ogType", "website");
            model.addAttribute("ogImage", "/images/seo/og-auth.jpg");
            model.addAttribute("pageClass", "page-auth");
            return "auth/signup";
        }

        try {
            userService.registerUser(signupRequest);
        } catch (IllegalArgumentException ex) {
            System.out.println("Signup error: " + ex.getMessage());

            model.addAttribute("signupError", ex.getMessage());
            model.addAttribute("pageTitle", "Create Account | AI Forensic");
            model.addAttribute("pageDescription", "Create your AI Forensic account to start scanning and reviewing suspicious images.");
            model.addAttribute("canonicalUrl", "http://localhost:8080/signup");
            model.addAttribute("ogType", "website");
            model.addAttribute("ogImage", "/images/seo/og-auth.jpg");
            model.addAttribute("pageClass", "page-auth");
            return "auth/signup";
        } catch (Exception ex) {
            System.out.println("Unexpected signup error: " + ex.getMessage());
            ex.printStackTrace();

            model.addAttribute("signupError", "Something went wrong while creating your account.");
            model.addAttribute("pageTitle", "Create Account | AI Forensic");
            model.addAttribute("pageDescription", "Create your AI Forensic account to start scanning and reviewing suspicious images.");
            model.addAttribute("canonicalUrl", "http://localhost:8080/signup");
            model.addAttribute("ogType", "website");
            model.addAttribute("ogImage", "/images/seo/og-auth.jpg");
            model.addAttribute("pageClass", "page-auth");
            return "auth/signup";
        }

        redirectAttributes.addFlashAttribute(
                "signupSuccess",
                "Account created successfully. Please sign in."
        );

        return "redirect:/login";
    }
}