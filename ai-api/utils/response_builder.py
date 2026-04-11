def build_prediction_response(predictions, predicted_index, confidence, class_names):
    if isinstance(class_names, list):
        predicted_class = class_names[predicted_index]
        all_predictions = {
            class_names[i]: round(float(predictions[i]) * 100, 2)
            for i in range(len(class_names))
        }
    else:
        predicted_class = class_names[str(predicted_index)]
        all_predictions = {
            class_names[str(i)]: round(float(predictions[i]) * 100, 2)
            for i in range(len(predictions))
        }

    return {
        "predicted_class": predicted_class,
        "confidence": round(confidence * 100, 2),
        "all_predictions": all_predictions,
        "predicted_index": predicted_index,
        "num_classes": len(predictions),
    }
