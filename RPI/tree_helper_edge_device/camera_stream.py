from picamera2 import Picamera2
import time

class PiCameraStream:
    def __init__(self, resolution):
        self.picam2 = Picamera2()
        self.picam2.preview_configuration.main.size = resolution
        self.picam2.preview_configuration.main.format = "RGB888"
        self.picam2.configure("preview")
        self.picam2.start()
        time.sleep(1)

    def get_frame(self):
        return self.picam2.capture_array()

    def stop(self):
        self.picam2.stop()