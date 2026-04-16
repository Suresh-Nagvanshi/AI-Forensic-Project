from deepface import DeepFace
import cv2


def analyze_emotion(face_bgr):
    if face_bgr is None or face_bgr.size == 0:
        return {
            "dominant_emotion": "unknown",
            "emotion_scores": {}
        }

    try:
        face_rgb = cv2.cvtColor(face_bgr, cv2.COLOR_BGR2RGB)

        result = DeepFace.analyze(
            img_path=face_rgb,
            actions=["emotion"],
            enforce_detection=False,
            silent=True
        )

        if isinstance(result, list):
            result = result[0]

        raw_scores = result.get("emotion", {})
        emotion_scores = {k: float(v) for k, v in raw_scores.items()}

        return {
            "dominant_emotion": str(result.get("dominant_emotion", "unknown")),
            "emotion_scores": emotion_scores
        }

    except Exception:
        return {
            "dominant_emotion": "unknown",
            "emotion_scores": {}
        }