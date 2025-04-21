import cv2
import numpy as np
from tflite_runtime.interpreter import Interpreter, load_delegate
import time


interpreter = Interpreter(
    model_path="best_320_full_integer_quant_edgetpu.tflite",
    experimental_delegates=[load_delegate('libedgetpu.so.1')]
)
interpreter.allocate_tensors()

input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()
input_size = input_details[0]['shape'][1]
input_dtype = input_details[0]['dtype']


def postprocess(output, input_size, conf_threshold=0.5, iou_threshold=0.7):
    output = output[0]
    boxes = output[:4, :].T
    scores = output[4, :]

    mask = scores >= conf_threshold
    boxes = boxes[mask]
    scores = scores[mask]

    if boxes.shape[0] == 0:
        return []

    boxes_xyxy = np.zeros_like(boxes)
    boxes_xyxy[:, 0] = boxes[:, 0] - boxes[:, 2] / 2
    boxes_xyxy[:, 1] = boxes[:, 1] - boxes[:, 3] / 2
    boxes_xyxy[:, 2] = boxes[:, 0] + boxes[:, 2] / 2
    boxes_xyxy[:, 3] = boxes[:, 1] + boxes[:, 3] / 2
    boxes_xyxy *= input_size

    indices = cv2.dnn.NMSBoxes(
        boxes_xyxy.tolist(), scores.tolist(),
        score_threshold=conf_threshold,
        nms_threshold=iou_threshold
    )

    detections = []
    for i in indices:
        i = i[0] if isinstance(i, (list, tuple, np.ndarray)) else i
        box = boxes_xyxy[i]
        conf = scores[i]
        detections.append([*box.tolist(), float(conf)])
    return detections


video_path = "input.mp4"
cap = cv2.VideoCapture(video_path)

width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
fps = cap.get(cv2.CAP_PROP_FPS)


fourcc = cv2.VideoWriter_fourcc(*'mp4v')
out = cv2.VideoWriter("output_annotated.mp4", fourcc, fps, (width, height))

frame_count = 0
start_time = time.time()

while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break


    resized = cv2.resize(frame, (input_size, input_size))
    
    if input_dtype == np.uint8:
        input_data = np.expand_dims(resized.astype(np.uint8), axis=0)
    elif input_dtype == np.int8:
        scale, zero_point = input_details[0]['quantization']
        input_data = np.expand_dims(resized.astype(np.float32) / 255.0, axis=0)
        input_data = (input_data / scale + zero_point).astype(np.int8)
    else:
        raise ValueError("Unsupported input dtype!")

    interpreter.set_tensor(input_details[0]['index'], input_data)
    interpreter.invoke()


    output = interpreter.get_tensor(output_details[0]['index'])
    if output_details[0]['dtype'] == np.int8:
        scale, zero_point = output_details[0]['quantization']
        output = (output.astype(np.float32) - zero_point) * scale

    detections = postprocess(output, input_size)

    scale_x = width / input_size
    scale_y = height / input_size
    for det in detections:
        x1, y1, x2, y2, conf = det
        x1 = int(x1 * scale_x)
        y1 = int(y1 * scale_y)
        x2 = int(x2 * scale_x)
        y2 = int(y2 * scale_y)

        cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
        cv2.putText(frame, f"{conf:.2f}", (x1, y1 - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)

    out.write(frame)
    frame_count += 1

cap.release()
out.release()
# cv2.destroyAllWindows()

elapsed = time.time() - start_time
print(f"Done! {frame_count} frames processed in {elapsed:.2f}s ({frame_count/elapsed:.2f} FPS)")
