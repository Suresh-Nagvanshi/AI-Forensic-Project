import json
from pathlib import Path

import numpy as np
from tensorflow.keras.models import load_model

from services.preprocessing import preprocess_image
from utils.response_builder import build_prediction_response

BASE_DIR = Path(__file__).resolve().parents[1]
MODEL_PATH = BASE_DIR / "model" / "efficientnetb0_best_model.keras"
CLASS_NAMES_PATH = BASE_DIR / "model" / "class_names.json"

model = load_model(MODEL_PATH)

with CLASS_NAMES_PATH.open("r", encoding="utf-8") as file:
    class_names = json.load(file)


def predict_image(img_path: str | Path) -> dict:
    processed_image = preprocess_image(img_path)
    predictions = model.predict(processed_image, verbose=0)[0].tolist()
    predicted_index = int(np.argmax(predictions))
    confidence = float(np.max(predictions))
    return build_prediction_response(predictions, predicted_index, confidence, class_names)
