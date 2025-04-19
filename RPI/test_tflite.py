import numpy as np
from PIL import Image
import tensorflow as tf
import cv2

#Load TFLite model
interpreter = tf.lite.Interpreter(model_path="best_float32.tflite")
interpreter.allocate_tensors()

#Get input and output details
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

print("Input shape:", input_details[0]['shape'])
print("Input dtype:", input_details[0]['dtype'])

for i, out in enumerate(output_details):
    print(f"\nOutput {i}:")
    print("  Shape:", out['shape'])
    print("  Dtype:", out['dtype'])

#Load image and preprocessing
image = Image.open("truck.jpg").convert("RGB")
input_shape = input_details[0]['shape'][1:3]
resized_image = image.resize((input_shape[1], input_shape[0]))
input_data = np.expand_dims(np.array(resized_image, dtype=np.float32) / 255.0, axis=0)

# Run inference
interpreter.set_tensor(input_details[0]['index'], input_data)
interpreter.invoke()

output_data = interpreter.get_tensor(output_details[0]['index'])
print("Raw output:", output_data)

def postprocess(output, conf_threshold = 0.5, iou_threshold = 0.5):
    output = output[0]
    boxes = output[:4, :].T
    scores = output[4, :]
    
    mask = scores >= conf_threshold
    boxes = boxes[mask]
    scores = scores[mask]
    if boxes.shape[0] == 0:
        return []

    #Convert to corners
    boxes_xyxy = np.zeros_like(boxes)
    boxes_xyxy[:, 0] = boxes[:, 0] - boxes[:, 2] / 2  # x1
    boxes_xyxy[:, 1] = boxes[:, 1] - boxes[:, 3] / 2  # y1
    boxes_xyxy[:, 2] = boxes[:, 0] + boxes[:, 2] / 2  # x2
    boxes_xyxy[:, 3] = boxes[:, 1] + boxes[:, 3] / 2  # y2
    
    #NMS
    boxes_xyxy *= input_shape[0]
    
    indices = cv2.dnn.NMSBoxes(
        bboxes = boxes_xyxy.tolist(),
        scores = scores.tolist(),
        score_threshold = conf_threshold,
        nms_threshold = iou_threshold
    )
    
    detections = []
    
    indices = np.array(indices).flatten()
    for idx in indices:
        box = boxes_xyxy[idx]
        conf = scores[idx]
        detections.append([*box.tolist(), float(conf)])
    return detections
    
    
    
detections = postprocess(output_data)
# Draw detections
img_draw = np.array(image.resize((input_shape[1], input_shape[0])))
img_draw = cv2.cvtColor(img_draw, cv2.COLOR_RGB2BGR)

for det in detections:
    x1, y1, x2, y2 = map(int, det[:4])
    conf = det[4]
    cv2.rectangle(img_draw, (x1, y1), (x2, y2), (0, 255, 0), 2)
    cv2.putText(img_draw, f"{conf:.2f}", (x1, y1 - 10),
                cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)

cv2.imwrite("output.jpg", img_draw)
print("Detections drawn and saved as output.jpg")
