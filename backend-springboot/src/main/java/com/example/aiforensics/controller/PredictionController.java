package com.example.aiforensics.controller;

import com.example.aiforensics.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @PostMapping("/predict")
    public String predictImage(@RequestParam("file") MultipartFile file, Model model) {
        try {
            if (file.isEmpty()) {
                model.addAttribute("error", "Please select an image file.");
                return "index";
            }

            Map<String, Object> result = predictionService.getPrediction(file);

            model.addAttribute("predictedClass", result.get("predicted_class"));
            model.addAttribute("confidence", result.get("confidence"));
            model.addAttribute("predictedIndex", result.get("predicted_index"));
            model.addAttribute("numClasses", result.get("num_classes"));
            model.addAttribute("allPredictions", result.get("all_predictions"));

        } catch (IOException e) {
            model.addAttribute("error", "File processing failed: " + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "Prediction failed: " + e.getMessage());
        }

        return "index";
    }
}