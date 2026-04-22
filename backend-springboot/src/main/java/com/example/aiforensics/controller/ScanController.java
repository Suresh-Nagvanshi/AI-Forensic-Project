package com.example.aiforensics.controller;

import com.example.aiforensics.dto.scan.ForensicReportDto;
import com.example.aiforensics.service.scan.PredictionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/scan")
public class ScanController {

    private final PredictionService predictionService;

    public ScanController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping
    public String scanPage() {
        return "scan/upload";
    }

    @PostMapping("/predict")
    public String predict(@RequestParam("file") MultipartFile file,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            ForensicReportDto report = predictionService.predict(file);
            model.addAttribute("report", report);
            return "scan/result";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/scan";
        }
    }
}
