import cv2
import time
import numpy as np
from PIL import Image

def draw_detections(frame, detections):
    for det in detections:
        x1, y1, x2, y2 = map(int, det[:4])
        conf = det[4]
        cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
        cv2.putText(frame, f"{conf:.2f}", (x1, y1 - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)
    return frame

def run_on_image(image_path, detector, output_path="output_image_annotated.jpg"):
    image = Image.open(image_path).convert("RGB")
    input_size = detector.input_details[0]['shape'][1]
    resized = image.resize((input_size, input_size))
    image_np = np.array(resized)

    input_dtype = detector.input_details[0]['dtype']
    quant_params = detector.input_details[0]['quantization']

    if input_dtype == np.uint8:
        input_data = np.expand_dims(image_np.astype(np.uint8), axis=0)
    elif input_dtype == np.int8:
        scale, zero_point = quant_params
        input_data = np.expand_dims(image_np.astype(np.float32) / 255.0, axis=0)
        input_data = (input_data / scale + zero_point).astype(np.int8)
    else:
        raise ValueError("Unsupported input dtype!")

    detector.interpreter.set_tensor(detector.input_details[0]['index'], input_data)
    detector.interpreter.invoke()

    output = detector.interpreter.get_tensor(detector.output_details[0]['index'])
    if detector.output_details[0]['dtype'] == np.int8:
        scale, zero_point = detector.output_details[0]['quantization']
        output = (output.astype(np.float32) - zero_point) * scale

    detections = detector.postprocess(output)

    image_draw = np.array(image)
    scale_x = image_draw.shape[1] / input_size
    scale_y = image_draw.shape[0] / input_size

    for det in detections:
        x1, y1, x2, y2, conf = det
        x1 = int(x1 * scale_x)
        y1 = int(y1 * scale_y)
        x2 = int(x2 * scale_x)
        y2 = int(y2 * scale_y)

        cv2.rectangle(image_draw, (x1, y1), (x2, y2), (0, 255, 0), 2)
        cv2.putText(image_draw, f"{conf:.2f}", (x1, y1 - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)

    cv2.imwrite(output_path, cv2.cvtColor(image_draw, cv2.COLOR_RGB2BGR))
    print(f"Saved annotated image to {output_path}")

def run_on_video(video_path, detector, output_path="output_annotated.mp4"):
    cap = cv2.VideoCapture(video_path)
    if not cap.isOpened():
        print(f"Failed to open video: {video_path}")
        return

    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    fps = cap.get(cv2.CAP_PROP_FPS) or 25  # fallback FPS if unknown

    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    out = cv2.VideoWriter(output_path, fourcc, fps, (width, height))

    print(f"Processing video: {video_path}")
    print(f"Saving to: {output_path}")

    frame_count = 0
    start_time = time.time()

    input_size = detector.input_details[0]['shape'][1]
    input_dtype = detector.input_details[0]['dtype']
    quant_params = detector.input_details[0]['quantization']

    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            break

        resized = cv2.resize(frame, (input_size, input_size))

        if input_dtype == np.uint8:
            input_data = np.expand_dims(resized.astype(np.uint8), axis=0)
        elif input_dtype == np.int8:
            scale, zero_point = quant_params
            input_data = np.expand_dims(resized.astype(np.float32) / 255.0, axis=0)
            input_data = (input_data / scale + zero_point).astype(np.int8)
        else:
            raise ValueError("Unsupported input dtype!")

        detector.interpreter.set_tensor(detector.input_details[0]['index'], input_data)
        detector.interpreter.invoke()

        output = detector.interpreter.get_tensor(detector.output_details[0]['index'])
        if detector.output_details[0]['dtype'] == np.int8:
            scale, zero_point = detector.output_details[0]['quantization']
            output = (output.astype(np.float32) - zero_point) * scale

        detections = detector.postprocess(output)

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
    elapsed = time.time() - start_time
    print(f"Finished. {frame_count} frames in {elapsed:.2f}s "
          f"({frame_count / elapsed:.2f} FPS)")

def run_live_feed(detector, camera):
    print("Starting live feed...")
    frame_count = 0
    start_time = time.time()

    try:
        while True:
            frame = camera.get_frame()
            detections = detector.detect(frame)
            output = draw_detections(frame, detections)

            frame_count += 1
            fps = frame_count / (time.time() - start_time)
            cv2.putText(output, f"FPS: {fps:.2f}", (10, 30),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 255), 2)

            cv2.imshow("Detection - Live", output)
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

    finally:
        cv2.destroyAllWindows()
        camera.stop()
        print("Live feed stopped.")
