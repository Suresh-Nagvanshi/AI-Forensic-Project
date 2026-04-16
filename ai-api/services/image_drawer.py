import os
import cv2
import numpy as np

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ANNOTATED_DIR = os.path.join(BASE_DIR, "outputs", "annotated")
os.makedirs(ANNOTATED_DIR, exist_ok=True)


def _to_py(v):
    if isinstance(v, np.generic):
        return v.item()
    return v


def draw_face_annotations(image, face_results, output_name="annotated.jpg"):
    annotated = image.copy()

    for face in face_results:
        bbox = face.get("bbox", [0, 0, 0, 0])
        x1, y1, x2, y2 = [int(_to_py(v)) for v in bbox]

        prediction = str(face.get("prediction", "unknown"))
        confidence = float(_to_py(face.get("confidence", 0.0)))
        emotion = str(face.get("dominant_emotion", "unknown"))
        det_conf = float(_to_py(face.get("detection_confidence", 0.0)))

        color = (0, 255, 0)
        if prediction.lower() in {"deepfake", "edited", "gan"}:
            color = (0, 0, 255)

        cv2.rectangle(annotated, (x1, y1), (x2, y2), color, 2)

        labels = [
            f"{prediction} {confidence:.2f}",
            f"emotion: {emotion}",
            f"det: {det_conf:.2f}"
        ]

        y_text = max(20, y1 - 10)

        for label in labels:
            (tw, th), _ = cv2.getTextSize(label, cv2.FONT_HERSHEY_SIMPLEX, 0.55, 1)
            top = max(0, y_text - th - 6)

            cv2.rectangle(annotated, (x1, top), (x1 + tw + 8, top + th + 8), color, -1)
            cv2.putText(
                annotated,
                label,
                (x1 + 4, top + th + 2),
                cv2.FONT_HERSHEY_SIMPLEX,
                0.55,
                (255, 255, 255),
                1,
                cv2.LINE_AA
            )
            y_text = top - 4

    output_path = os.path.join(ANNOTATED_DIR, output_name)
    ok = cv2.imwrite(output_path, annotated)

    return {
        "saved": bool(ok),
        "output_path": output_path
    }