import os
import json
import cv2
import numpy as np
import tensorflow as tf

try:
    import keras as standalone_keras
except ImportError:
    standalone_keras = None

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
MODEL_PATH = os.path.join(BASE_DIR, "model", "efficientnetb0_best_model.keras")
CLASS_NAMES_PATH = os.path.join(BASE_DIR, "model", "class_names.json")

classifier_model = None
class_names = None

IMG_SIZE = 224


def get_model():
    global classifier_model
    if classifier_model is None:
        if not os.path.exists(MODEL_PATH):
            raise FileNotFoundError(f"Classifier model not found at: {MODEL_PATH}")

        silu_activation = getattr(tf.keras.activations, "silu", tf.keras.activations.swish)
        custom_objects = {
            "silu": silu_activation,
            "Swish": tf.keras.activations.swish,
        }

        try:
            with tf.keras.utils.custom_object_scope(custom_objects):
                classifier_model = tf.keras.models.load_model(MODEL_PATH, compile=False)
        except Exception as first_error:
            if standalone_keras is not None:
                try:
                    classifier_model = standalone_keras.models.load_model(MODEL_PATH, compile=False)
                except Exception as second_error:
                    raise RuntimeError(
                        f"Failed to load model with tf.keras and keras. tf.keras error: {first_error}; keras error: {second_error}"
                    ) from second_error
            else:
                raise RuntimeError(
                    f"Failed to load model with tf.keras. Install standalone keras or use a compatible TensorFlow version. Original error: {first_error}"
                ) from first_error
    return classifier_model


def get_class_names():
    global class_names
    if class_names is None:
        if not os.path.exists(CLASS_NAMES_PATH):
            raise FileNotFoundError(f"class_names.json not found at: {CLASS_NAMES_PATH}")
        with open(CLASS_NAMES_PATH, "r", encoding="utf-8") as f:
            class_names = json.load(f)
    return class_names


def preprocess_face(face_bgr):
    if face_bgr is None or face_bgr.size == 0:
        raise ValueError("Empty face image received")

    face_rgb = cv2.cvtColor(face_bgr, cv2.COLOR_BGR2RGB)
    face_rgb = cv2.resize(face_rgb, (IMG_SIZE, IMG_SIZE))
    face_rgb = face_rgb.astype("float32") / 255.0
    face_rgb = np.expand_dims(face_rgb, axis=0)
    return face_rgb


def predict_face(face_bgr):
    model = get_model()
    labels = get_class_names()

    input_tensor = preprocess_face(face_bgr)
    preds = model.predict(input_tensor, verbose=0)[0]

    pred_index = int(np.argmax(preds))
    pred_label = labels[pred_index]
    pred_conf = float(preds[pred_index])

    return {
        "prediction": pred_label,
        "confidence": pred_conf,
        "pred_index": pred_index,
        "all_scores": {labels[i]: float(preds[i]) for i in range(len(labels))}
    }