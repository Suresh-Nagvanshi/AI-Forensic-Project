import uuid
import numpy as np


def _to_py(v):
    if isinstance(v, np.generic):
        return v.item()
    return v


def build_face_result(face_id, bbox, detection_confidence, prediction_result, emotion_result, heatmap_path=None):
    prediction = str(prediction_result.get("prediction", "unknown"))
    confidence = float(_to_py(prediction_result.get("confidence", 0.0)))
    pred_index = int(_to_py(prediction_result.get("pred_index", -1)))
    all_scores = {
        str(k): float(_to_py(v))
        for k, v in prediction_result.get("all_scores", {}).items()
    }

    dominant_emotion = str(emotion_result.get("dominant_emotion", "unknown"))
    emotion_scores = {
        str(k): float(_to_py(v))
        for k, v in emotion_result.get("emotion_scores", {}).items()
    }

    suspicious_region = prediction.lower() in {"deepfake", "edited", "gan"}

    return {
        "face_id": int(face_id),
        "bbox": [int(_to_py(x)) for x in bbox],
        "detection_confidence": float(_to_py(detection_confidence)),
        "prediction": prediction,
        "confidence": confidence,
        "pred_index": pred_index,
        "class_scores": all_scores,
        "dominant_emotion": dominant_emotion,
        "emotion_scores": emotion_scores,
        "heatmap_path": heatmap_path,
        "suspicious_region": suspicious_region
    }


def compute_overall_result(face_results):
    if not face_results:
        return {
            "overall_prediction": "no_face_detected",
            "overall_confidence": 0.0,
            "risk_level": "Low"
        }

    best_face = max(face_results, key=lambda x: x.get("confidence", 0.0))
    prediction = str(best_face.get("prediction", "unknown"))
    confidence = float(best_face.get("confidence", 0.0))
    scores = best_face.get("class_scores", {})
    sorted_scores = sorted(scores.values(), reverse=True)
    margin = sorted_scores[0] - sorted_scores[1] if len(sorted_scores) > 1 else sorted_scores[0]

    suspicious_found = any(face.get("suspicious_region", False) for face in face_results)

    if prediction.lower() == "deepfake" and confidence >= 0.75:
        risk_level = "High"
    elif prediction.lower() == "deepfake" and (confidence >= 0.50 or margin < 0.10):
        risk_level = "Medium"
    elif suspicious_found:
        risk_level = "Medium"
    else:
        risk_level = "Low"

    return {
        "overall_prediction": prediction,
        "overall_confidence": confidence,
        "risk_level": risk_level
    }


def build_final_report(face_results, annotated_image_path=None):
    summary = compute_overall_result(face_results)

    return {
        "report_id": uuid.uuid4().hex,
        "overall_prediction": summary["overall_prediction"],
        "overall_confidence": summary["overall_confidence"],
        "risk_level": summary["risk_level"],
        "total_faces": len(face_results),
        "faces": face_results,
        "annotated_image_path": annotated_image_path
    }