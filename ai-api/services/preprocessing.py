from pathlib import Path

import numpy as np
from PIL import Image
from tensorflow.keras.applications.efficientnet import preprocess_input
from tensorflow.keras.preprocessing import image

IMG_SIZE = (224, 224)


def preprocess_image(img_path: str | Path) -> np.ndarray:
    img = Image.open(img_path).convert("RGB")
    img = img.resize(IMG_SIZE)
    img_array = image.img_to_array(img)
    img_array = preprocess_input(img_array)
    return np.expand_dims(img_array, axis=0)
