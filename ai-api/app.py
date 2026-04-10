import os
import json
import numpy as np
from flask import Flask, request, jsonify
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
from PIL import Image

app = Flask(__name__)

MODEL_PATH = "model/efficientnetb0_best_model.keras"
CLASS_NAMES_PATH = "model/class_names.json"
UPLOAD_FOLDER = "uploads"
TEMP_FOLDER = "temp"
IMG_SIZE = (224, 224)

os.makedirs(UPLOAD_FOLDER, exist_ok=True)
os.makedirs(TEMP_FOLDER, exist_ok=True)

model = load_model(MODEL_PATH)

with open(CLASS_NAMES_PATH, "r") as f:
    class_names = json.load(f)

def preprocess_image(img_path):
    img = Image.open(img_path).convert("RGB")
    img = img.resize(IMG_SIZE)
    img_array = image.img_to_array(img)
    img_array = img_array / 255.0
    img_array = np.expand_dims(img_array, axis=0)
    return img_array

@app.route("/")
def home():
    return jsonify({
        "message": "AI Forensic API is running"
    })

@app.route("/predict", methods=["POST"])
def predict():
    if "file" not in request.files:
        return jsonify({"error": "No file part in request"}), 400

    file = request.files["file"]

    if file.filename == "":
        return jsonify({"error": "No selected file"}), 400

    file_path = os.path.join(UPLOAD_FOLDER, file.filename)
    file.save(file_path)

    try:
        processed_image = preprocess_image(file_path)
        predictions = model.predict(processed_image)
        predicted_index = int(np.argmax(predictions[0]))
        confidence = float(np.max(predictions[0]))

        predicted_class = class_names[predicted_index] if isinstance(class_names, list) else class_names[str(predicted_index)]

        return jsonify({
            "predicted_class": predicted_class,
            "confidence": round(confidence * 100, 2)
        })

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(debug=True, port=5000)