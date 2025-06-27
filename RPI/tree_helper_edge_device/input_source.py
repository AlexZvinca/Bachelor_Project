import cv2
import time
import numpy as np
from PIL import Image
from plate_processor import PlateProcessor


def draw_detections(frame, detections, scale_x=1.0, scale_y=1.0):
    for det in detections:
        if len(det) < 5:
            continue
        x1, y1, x2, y2 = [int(det[0]*scale_x), int(det[1]*scale_y),
                          int(det[2]*scale_x), int(det[3]*scale_y)]
        conf = det[4]
        cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
        cv2.putText(frame, f"{conf:.2f}", (x1, y1 - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)
    return frame


def run_on_image(image_path, detector, output_path="output_image_annotated.jpg"):
    image = Image.open(image_path).convert("RGB")
    image_np = np.array(image)
    detections, _ = detector.detect(image_np)
    
    scale_x = image.size[1] / detector.input_size
    scale_y = image.size[0] / detector.input_size
    
    image_annotated = draw_detections(image_np.copy(), detections, scale_x, scale_y)
    cv2.imwrite(output_path, cv2.cvtColor(image_annotated, cv2.COLOR_RGB2BGR))
    print(f"Saved annotated image to {output_path}")
    
    if detections:
        processor = PlateProcessor(min_confirmations=1)
        processor.process_frame(image_np)
        processor.shutdown()

def run_on_video(video_path, detector, output_path="output_annotated.mp4"):
    processor = PlateProcessor()
    processor.ensure_sim_ready()
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

    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            break

        detections, _ = detector.detect(frame)
        if detections and processor.should_process():
            processor.process_frame(frame)
        
        scale_x = frame.shape[1] / detector.input_size
        scale_y = frame.shape[0] / detector.input_size
        
        output = draw_detections(frame.copy(), detections, scale_x, scale_y)
        out.write(output)
        frame_count += 1

    cap.release()
    out.release()
    elapsed = time.time() - start_time
    print(f"Finished. {frame_count} frames in {elapsed:.2f}s "
          f"({frame_count / elapsed:.2f} FPS)")
    processor.shutdown()

def run_live_feed(detector, camera):
    processor = PlateProcessor()
    processor.ensure_sim_ready()
    print("Starting live feed...")
    frame_count = 0
    start_time = time.time()

    try:
        while True:
            frame = camera.get_frame()
            detections, _ = detector.detect(frame)
            
            if detections and processor.should_process():
                processor.process_frame(frame)
            
            scale_x = frame.shape[1] / detector.input_size
            scale_y = frame.shape[0] / detector.input_size
            output = draw_detections(frame, detections, scale_x, scale_y)

            frame_count += 1
            fps = frame_count / (time.time() - start_time)
            cv2.putText(output, f"FPS: {fps:.2f}", (10, 30),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 255), 2)

            cv2.imshow("Detection - Live", output)
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break 

    finally:
        processor.shutdown()
        cv2.destroyAllWindows()
        camera.stop()
        print("Live feed stopped.")
