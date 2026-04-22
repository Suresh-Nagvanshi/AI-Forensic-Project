import os
import traceback
import uuid
from flask import Flask, request, jsonify, send_from_directory
from flask_cors import CORS
from werkzeug.utils import secure_filename

from services.face_detector import detect_faces
from services.predictor import predict_face, get_model
from services.emotion_analyzer import analyze_emotion
from services.gradcam import preprocess_for_gradcam, make_gradcam_heatmap, save_gradcam_overlay
from services.image_drawer import draw_face_annotations
from services.report_builder import build_face_result, build_final_report

app = Flask(__name__)
CORS(app)

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
UPLOAD_FOLDER = os.path.join(BASE_DIR, "uploads")
ANNOTATED_FOLDER = os.path.join(BASE_DIR, "outputs", "annotated")
HEATMAP_FOLDER = os.path.join(BASE_DIR, "outputs", "heatmaps")

ALLOWED_EXTENSIONS = {"png", "jpg", "jpeg"}

os.makedirs(UPLOAD_FOLDER, exist_ok=True)
os.makedirs(ANNOTATED_FOLDER, exist_ok=True)
os.makedirs(HEATMAP_FOLDER, exist_ok=True)


def allowed_file(filename):
    return "." in filename and filename.rsplit(".", 1)[1].lower() in ALLOWED_EXTENSIONS


def save_upload(file):
    if file is None:
        raise ValueError("No file provided")

    if file.filename == "":
        raise ValueError("No selected file")

    if not allowed_file(file.filename):
        raise ValueError("Only png, jpg, jpeg files are allowed")

    safe_name = secure_filename(file.filename)
    ext = safe_name.rsplit(".", 1)[1].lower()
    unique_name = f"{uuid.uuid4().hex}.{ext}"
    save_path = os.path.join(UPLOAD_FOLDER, unique_name)
    file.save(save_path)
    return save_path, unique_name


@app.route("/health", methods=["GET"])
def health():
    return jsonify({
        "status": "ok",
        "message": "AI Forensics API running"
    }), 200


@app.route("/outputs/annotated/<path:filename>", methods=["GET"])
def serve_annotated_file(filename):
    return send_from_directory(ANNOTATED_FOLDER, filename)


@app.route("/outputs/heatmaps/<path:filename>", methods=["GET"])
def serve_heatmap_file(filename):
    return send_from_directory(HEATMAP_FOLDER, filename)


@app.route("/predict", methods=["POST"])
def predict():
    try:
        print('PREDICT: request start')
        file = request.files.get("file")
        image_path, uploaded_name = save_upload(file)
        print('PREDICT: saved upload', image_path)

        faces, original_image = detect_faces(image_path)
        print('PREDICT: face count', len(faces))

        if not faces:
            report = build_final_report([], annotated_image_path=None)
            report["message"] = "No face detected"
            report["uploaded_file"] = uploaded_name
            report["annotated_image_url"] = None
            return jsonify(report), 200

        model = get_model()
        face_results = []

        for idx, face in enumerate(faces, start=1):
            print('PREDICT: processing face', idx)
            crop = face["crop"]

            # Use original_image for prediction because the model was trained on full frames,
            # whereas emotion is fine on cropped faces.
            prediction_result = predict_face(original_image)
            print('PREDICT: prediction', prediction_result)
            emotion_result = analyze_emotion(crop)
            print('PREDICT: emotion', emotion_result)

            heatmap_path = None
            heatmap_url = None

            try:
                print('PREDICT: gradcam input shape', original_image.shape)
                heatmap_input = preprocess_for_gradcam(original_image)
                heatmap = make_gradcam_heatmap(
                    heatmap_input,
                    model,
                    pred_index=prediction_result["pred_index"]
                )
                print('PREDICT: heatmap computed', heatmap.shape)

                heatmap_filename = f"heatmap_face_{idx}_{uuid.uuid4().hex}.jpg"
                heatmap_path = os.path.join(HEATMAP_FOLDER, heatmap_filename)
                save_gradcam_overlay(original_image, heatmap, heatmap_path)
                heatmap_url = f"/outputs/heatmaps/{heatmap_filename}"
            except Exception as heatmap_error:
                traceback.print_exc()
                heatmap_path = None
                heatmap_url = None

            face_result = build_face_result(
                face_id=idx,
                bbox=face["bbox"],
                detection_confidence=face["confidence"],
                prediction_result=prediction_result,
                emotion_result=emotion_result,
                heatmap_path=heatmap_path
            )

            face_result["heatmap_url"] = heatmap_url
            face_results.append(face_result)

        annotated_filename = f"annotated_{uuid.uuid4().hex}.jpg"
        annotated_result = draw_face_annotations(
            original_image,
            face_results,
            output_name=annotated_filename
        )

        report = build_final_report(
            face_results,
            annotated_image_path=annotated_result["output_path"]
        )
        report["uploaded_file"] = uploaded_name
        report["annotated_saved"] = annotated_result["saved"]
        report["annotated_image_url"] = f"/outputs/annotated/{annotated_filename}"

        return jsonify(report), 200

    except ValueError as e:
        return jsonify({"error": str(e)}), 400

    except Exception as e:
        traceback.print_exc()
        return jsonify({"error": f"Internal server error: {str(e)}"}), 500


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=int(os.environ.get("PORT", 5000)), debug=False, use_reloader=False)