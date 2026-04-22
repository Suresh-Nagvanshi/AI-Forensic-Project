# AI Forensic: Advanced Deepfake & Image Screening Platform

![AI Forensic Banner](https://img.shields.io/badge/AI-Forensics-blueviolet?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Flask](https://img.shields.io/badge/Flask-000000?style=for-the-badge&logo=flask&logoColor=white)
![TensorFlow](https://img.shields.io/badge/TensorFlow-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white)

AI Forensic is a premium, full-stack web application designed for the high-integrity screening of digital media. Utilizing state-of-the-art Deep Learning models, the platform identifies Deepfakes, GAN-generated faces, and digital manipulations with high precision, providing detailed forensic reports for human review.

---

## 🚀 Key Features

- **Multi-Class Forensic Analysis:** Detects Deepfakes (FaceForensics++), GAN-generated images (StyleGAN), Digital Edits, and Authentic (Real) media.
- **Explainable AI (XAI):** Integrated **Grad-CAM heatmaps** that highlight exactly which regions of an image the model found suspicious.
- **Face-Level Intelligence:** Powered by **YOLOv8** for robust face detection and automated emotion profiling during screening.
- **Premium UI/UX:** A modern, glassmorphic dashboard built for professional forensic workflows.
- **Dual-Backend Architecture:** High-performance Flask AI API coupled with a secure Spring Boot management system.

---

## 🛠️ Technology Stack

### **AI & Inference (Python)**
- **Framework:** TensorFlow / Keras
- **Core Model:** EfficientNetB0 (Optimized for facial forensic artifacts)
- **Computer Vision:** OpenCV, Ultralytics YOLOv8
- **Explainability:** Custom Grad-CAM Implementation
- **API:** Flask with CORS integration

### **Web Application (Java)**
- **Framework:** Spring Boot 3.x
- **Security:** Spring Security 6 (Role-based access & Auth)
- **Template Engine:** Thymeleaf with Security Extras
- **Database:** PostgreSQL (Report management)

### **Frontend**
- **Aesthetics:** Vanilla CSS3 with Glassmorphism, CSS Grid, and dynamic animations.
- **Icons/Graphics:** Custom SVG Vector Branding.

---

## 📐 System Architecture

1.  **Upload:** User uploads an image via the Spring Boot dashboard.
2.  **Detection:** The image is sent to the Flask AI API where **YOLOv8** identifies and crops all faces.
3.  **Inference:** Each face is processed through an **EfficientNetB0** classifier trained on global forensic datasets.
4.  **Explainability:** The system generates a **Grad-CAM heatmap** for every "Deepfake" or "Edited" prediction.
5.  **Report:** Spring Boot compiles the results into a responsive, premium forensic report.

---

## ⚙️ Installation & Setup

### **1. AI API Setup (Python 3.9+)**
```bash
cd ai-api
pip install -r requirements.txt
# Ensure model files are in ai-api/model/
python app.py
```

### **2. Web Backend Setup (Java 17+)**
```bash
cd backend-springboot
./mvnw clean install
./mvnw spring-boot:run
```
*Access the dashboard at `http://localhost:8080`*

---

## 🛡️ Ethics & Purpose
This tool is built for **Responsible Media Screening**. It is intended to assist journalists, forensic investigators, and platform moderators in identifying potential misinformation. While highly accurate, the system is designed to provide "Suspicious Region" indicators to support, not replace, human judgment.

