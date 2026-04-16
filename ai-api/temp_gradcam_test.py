import os
import cv2

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
os.sys.path.insert(0, BASE_DIR)

from services.face_detector import detect_faces
from services.predictor import predict_face, get_model
from services.gradcam import preprocess_for_gradcam, make_gradcam_heatmap, save_gradcam_overlay

image_path = os.path.abspath(os.path.join(BASE_DIR, '..', 'test.jpg'))
print('image_path:', image_path)

faces, original = detect_faces(image_path)
print('faces count:', len(faces))
if not faces:
    raise SystemExit('No faces detected')

crop = faces[0]['crop']
print('crop shape:', crop.shape)

prediction = predict_face(crop)
print('prediction:', prediction)

model = get_model()
print('model loaded:', model.name)

inp = preprocess_for_gradcam(crop)
print('input shape:', inp.shape)

heatmap = make_gradcam_heatmap(inp, model, pred_index=prediction['pred_index'])
print('heatmap shape:', heatmap.shape, 'min', heatmap.min(), 'max', heatmap.max())

out_path = os.path.join(BASE_DIR, 'outputs', 'heatmaps', 'temp_heatmap.jpg')
data = save_gradcam_overlay(crop, heatmap, out_path)
print('saved path:', data)