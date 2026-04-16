import os
import cv2
from ultralytics import YOLO

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
MODEL_PATH = os.path.join(BASE_DIR, "model", "yolo_face.pt")

face_model = None


def get_face_model():
    global face_model
    if face_model is None:
        if not os.path.exists(MODEL_PATH):
            raise FileNotFoundError(f"YOLO face model not found at: {MODEL_PATH}")
        face_model = YOLO(MODEL_PATH)
    return face_model


def detect_faces(image_path, conf=0.35):
    model = get_face_model()

    image = cv2.imread(image_path)
    if image is None:
        raise ValueError(f"Could not read image: {image_path}")

    results = model.predict(source=image, conf=conf, verbose=False)

    faces = []
    for result in results:
        if result.boxes is None:
            continue

        for box in result.boxes:
            x1, y1, x2, y2 = map(int, box.xyxy[0].tolist())
            score = float(box.conf[0])

            x1 = max(0, x1)
            y1 = max(0, y1)
            x2 = min(image.shape[1], x2)
            y2 = min(image.shape[0], y2)

            crop = image[y1:y2, x1:x2]
            if crop is None or crop.size == 0:
                continue

            faces.append({
                "bbox": [x1, y1, x2, y2],
                "confidence": score,
                "crop": crop
            })

    return faces, image
