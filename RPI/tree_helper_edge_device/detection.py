import cv2
import numpy as np
from tflite_runtime.interpreter import Interpreter, load_delegate

class Detector:
    def __init__(self, model_path):
        self.interpreter = Interpreter(
            model_path=model_path,
            experimental_delegates=[load_delegate('libedgetpu.so.1')]
        )
        self.interpreter.allocate_tensors()
        self.input_details = self.interpreter.get_input_details()
        self.output_details = self.interpreter.get_output_details()
        self.input_size = self.input_details[0]['shape'][1]
        self.input_dtype = self.input_details[0]['dtype']

    def preprocess(self, frame):
        if self.input_dtype == np.uint8:
            return np.expand_dims(frame.astype(np.uint8), axis=0)
        elif self.input_dtype == np.int8:
            scale, zero_point = self.input_details[0]['quantization']
            frame_scaled = frame.astype(np.float32) / 255.0
            input_data = (frame_scaled / scale + zero_point).astype(np.int8)
            return np.expand_dims(input_data, axis=0)
        else:
            raise ValueError("Unsupported input type!")


    def postprocess(self, output, conf_threshold=0.5, iou_threshold=0.5):
        output = output[0]
        boxes = output[:4, :].T
        scores = output[4, :]
        mask = scores >= conf_threshold
        boxes, scores = boxes[mask], scores[mask]

        if boxes.shape[0] == 0:
            return []

        boxes_xyxy = np.zeros_like(boxes)
        boxes_xyxy[:, 0] = boxes[:, 0] - boxes[:, 2] / 2
        boxes_xyxy[:, 1] = boxes[:, 1] - boxes[:, 3] / 2
        boxes_xyxy[:, 2] = boxes[:, 0] + boxes[:, 2] / 2
        boxes_xyxy[:, 3] = boxes[:, 1] + boxes[:, 3] / 2
        boxes_xyxy *= self.input_size

        indices = np.array(cv2.dnn.NMSBoxes(
            boxes_xyxy.tolist(), scores.tolist(),
            score_threshold=conf_threshold, nms_threshold=iou_threshold
        )).flatten()

        return [[*boxes_xyxy[i], float(scores[i])] for i in indices]

    def detect(self, frame):
        resized = cv2.resize(frame, (self.input_size, self.input_size))
        input_data = self.preprocess(resized)
        self.interpreter.set_tensor(self.input_details[0]['index'], input_data)
        self.interpreter.invoke()
        output = self.interpreter.get_tensor(self.output_details[0]['index'])

        if self.output_details[0]['dtype'] == np.int8:
            scale, zero_point = self.output_details[0]['quantization']
            output = (output.astype(np.float32) - zero_point) * scale

        return self.postprocess(output), resized