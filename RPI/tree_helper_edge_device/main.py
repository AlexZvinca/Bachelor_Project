import cv2
import time
from detection import Detector
from camera_stream import PiCameraStream
from ocr import extract_plate_text

MODEL_PATH = "/home/raspberry_user/Desktop/TreeHelper_RPi/Models/11n320/best320_full_integer_quant_edgetpu.tflite"

def main():
    detector = Detector(MODEL_PATH)
    camera = PiCameraStream((detector.input_size, detector.input_size))

    frame_count = 0
    start_time = time.time()

    try:
        while True:
            frame = camera.get_frame()
            detections = detector.detect(frame)

            for det in detections:
                x1, y1, x2, y2 = map(int, det[:4])
                conf = det[4]
                print(f"Box: [{x1}, {y1}, {x2}, {y2}], Confidence: {conf:.2f}")
                cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
                cv2.putText(frame, f"{conf:.2f}", (x1, y1 - 10),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)
                plate = extract_plate_text(frame, [x1, y1, x2, y2])
                if plate:
                    print(f"Plate: {plate}")

            frame_count += 1
            elapsed = time.time() - start_time
            fps = frame_count / elapsed if elapsed > 0 else 0.0
            cv2.putText(frame, f"FPS: {fps:.2f}", (10, 30),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 255), 2)

            cv2.imshow("YOLOv11n Detection", frame)

            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

    finally:
        cv2.destroyAllWindows()
        camera.stop()
        print("Shutting down.")

if __name__ == "__main__":
    main()