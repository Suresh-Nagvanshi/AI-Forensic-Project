import os
from flask import Flask, request, jsonify

from services.predictor import predict_image

app = Flask(__name__)

UPLOAD_FOLDER = "uploads"
TEMP_FOLDER = "temp"

os.makedirs(UPLOAD_FOLDER, exist_ok=True)
os.makedirs(TEMP_FOLDER, exist_ok=True)

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
        return jsonify(predict_image(file_path))

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(debug=True, port=5000)
