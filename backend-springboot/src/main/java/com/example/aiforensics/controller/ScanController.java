package com.example.aiforensics.controller;

import com.example.aiforensics.dto.scan.PredictionResponseDto;
import com.example.aiforensics.service.scan.PredictionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ScanController {

    private final PredictionService predictionService;

    public ScanController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping({"/", "/scan", "/scan/upload"})
    public String showUploadPage() {
        return "scan/upload";
    }

    @PostMapping({"/predict", "/scan/predict"})
    public String predictImage(@RequestParam("file") MultipartFile file, Model model) {
        try {
            if (file.isEmpty()) {
                model.addAttribute("error", "Please select an image file.");
                return "scan/upload";
            }

            PredictionResponseDto result = predictionService.getPrediction(file);
            model.addAttribute("predictedClass", result.getPredictedClass());
            model.addAttribute("confidence", result.getConfidence());
            model.addAttribute("predictedIndex", result.getPredictedIndex());
            model.addAttribute("numClasses", result.getNumClasses());
            model.addAttribute("allPredictions", result.getAllPredictions());
            return "scan/result";
        } catch (IOException exception) {
            model.addAttribute("error", "File processing failed: " + exception.getMessage());
            return "scan/upload";
        } catch (Exception exception) {
            model.addAttribute("error", "Prediction failed: " + exception.getMessage());
            return "scan/upload";
        }
    }
}
