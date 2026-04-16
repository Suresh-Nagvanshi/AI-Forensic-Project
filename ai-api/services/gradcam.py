import os
import cv2
import numpy as np
import tensorflow as tf

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
IMG_SIZE = 224
BASE_MODEL_NAME = "efficientnetb0"
LAST_CONV_LAYER_NAME = "top_conv"


def preprocess_for_gradcam(face_bgr):
    face_rgb = cv2.cvtColor(face_bgr, cv2.COLOR_BGR2RGB)
    face_rgb = cv2.resize(face_rgb, (IMG_SIZE, IMG_SIZE))
    face_rgb = face_rgb.astype("float32") / 255.0
    return np.expand_dims(face_rgb, axis=0)


def _find_layer_recursive(model, target_layer_name):
    for layer in model.layers:
        if layer.name == target_layer_name:
            return layer
        if isinstance(layer, tf.keras.Model):
            nested = _find_layer_recursive(layer, target_layer_name)
            if nested is not None:
                return nested
    return None


def _build_gradcam_model(model):
    target_layer = _find_layer_recursive(model, LAST_CONV_LAYER_NAME)
    if target_layer is None:
        raise ValueError(f"Layer '{LAST_CONV_LAYER_NAME}' not found in model")

    return tf.keras.models.Model(
        inputs=model.inputs,
        outputs=[target_layer.output, model.output],
    )


def make_gradcam_heatmap(img_array, model, pred_index=None):
    img_tensor = tf.convert_to_tensor(img_array, dtype=tf.float32)
    grad_model = _build_gradcam_model(model)

    with tf.GradientTape() as tape:
        conv_outputs, preds = grad_model(img_tensor, training=False)
        if pred_index is None:
            pred_index = tf.argmax(preds[0])
        class_channel = preds[:, pred_index]

    grads = tape.gradient(class_channel, conv_outputs)
    if grads is None:
        raise ValueError("Grad-CAM gradients could not be computed for the target convolution layer")

    pooled_grads = tf.reduce_mean(grads, axis=(0, 1, 2))
    conv_outputs = conv_outputs[0]
    heatmap = tf.reduce_sum(conv_outputs * pooled_grads, axis=-1)
    heatmap = tf.maximum(heatmap, 0)

    max_val = tf.reduce_max(heatmap)
    if float(max_val) == 0.0:
        return np.zeros((heatmap.shape[0], heatmap.shape[1]), dtype=np.float32)

    heatmap = heatmap / max_val
    return heatmap.numpy().astype(np.float32)


def save_gradcam_overlay(face_bgr, heatmap, out_path, alpha=0.4):
    face_resized = cv2.resize(face_bgr, (IMG_SIZE, IMG_SIZE))
    heatmap_resized = cv2.resize(heatmap, (IMG_SIZE, IMG_SIZE))
    heatmap_uint8 = np.uint8(255 * heatmap_resized)
    heatmap_color = cv2.applyColorMap(heatmap_uint8, cv2.COLORMAP_JET)
    overlay = cv2.addWeighted(face_resized, 1 - alpha, heatmap_color, alpha, 0)
    cv2.imwrite(out_path, overlay)
    return out_path
