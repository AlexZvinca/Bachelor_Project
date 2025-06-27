from detection import Detector
from camera_stream import PiCameraStream
from input_source import run_on_image, run_on_video, run_live_feed

MODEL =
IMAGE =
VIDEO =

def main():
    mode = "video"
    detector = Detector(MODEL)

    if mode == "image":
        run_on_image(IMAGE, detector)
    elif mode == "video":
        run_on_video(VIDEO, detector)
    elif mode == "live":
        camera = PiCameraStream((detector.input_size, detector.input_size))
        run_live_feed(detector, camera)

if __name__ == "__main__":
    main()
