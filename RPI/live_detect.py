from picamera2 import Picamera2
import cv2
import numpy as np
import tensorflow as tf
import time

#Load TFLite model
interpreter = tf.lite.Interpreter(model_path="best_float32.tflite")
interpreter.allocate_tensors()

input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()
input_size = input_details[0]['shape'][1]

# --- Postprocessing ---
def postprocess(output, conf_threshold=0.5, iou_threshold=0.5):
    output = output[0]
    boxes = output[:4, :].T
    scores = output[4, :]

    mask = scores >= conf_threshold
    boxes = boxes[mask]
    scores = scores[mask]

    if boxes.shape[0] == 0:
        return []

    boxes_xyxy = np.zeros_like(boxes)
    boxes_xyxy[:, 0] = boxes[:, 0] - boxes[:, 2] / 2  # x1
    boxes_xyxy[:, 1] = boxes[:, 1] - boxes[:, 3] / 2  # y1
    boxes_xyxy[:, 2] = boxes[:, 0] + boxes[:, 2] / 2  # x2
    boxes_xyxy[:, 3] = boxes[:, 1] + boxes[:, 3] / 2  # y2
    boxes_xyxy *= input_size

    indices = np.array(cv2.dnn.NMSBoxes(
        boxes_xyxy.tolist(), scores.tolist(),
        score_threshold=conf_threshold,
        nms_threshold=iou_threshold
    )).flatten()

    detections = []
    for idx in indices:
        box = boxes_xyxy[idx]
        conf = scores[idx]
        detections.append([*box.tolist(), float(conf)])
    return detections

#Pi Camera Set Up
picam2 = Picamera2()
picam2.preview_configuration.main.size = (input_size, input_size)
picam2.preview_configuration.main.format = "RGB888"
picam2.configure("preview")
picam2.start()
time.sleep(1)
print("Running live inference. Press 'q' to quit.")

frame_count = 0
start_time = time.time()

while True:
    frame = picam2.capture_array()
    input_data = np.expand_dims(frame.astype(np.float32) / 255.0, axis=0)

    interpreter.set_tensor(input_details[0]['index'], input_data)
    interpreter.invoke()
    output = interpreter.get_tensor(output_details[0]['index'])

    detections = postprocess(output)

    # Draw boxes
    for det in detections:
        x1, y1, x2, y2 = map(int, det[:4])
        conf = det[4]
        cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
        cv2.putText(frame, f"{conf:.2f}", (x1, y1 - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)
                    
    frame_count += 1
    elapsed_time = time.time() - start_time
    fps = frame_count / elapsed_time if elapsed_time > 0 else 0.0
    cv2.putText(frame, f"FPS: {fps:.2f}", (10, 30),
                cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 255), 2)

    cv2.imshow("YOLOv11s Live Detection", frame)
            
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break


#Cleanup
cv2.destroyAllWindows()
picam2.stop()
print("Done.")