import cv2
import time
from ocr import send_to_plate_api
from plate_filter import PlateFilter
from comm import SIM7600

PHONE_NUMBER =

class PlateProcessor:
    def __init__(self, sim_module=None, cooldown=15, min_confirmations=3):
        self.plate_filter = PlateFilter(min_confirmations=min_confirmations)
        self.sim = sim_module or SIM7600()
        self.last_sent_time = 0
        self.cooldown = cooldown
        self.sim_started = False
        
    def ensure_sim_ready(self):
        if not self.sim_started:
            self.sim.initialize()
            self.sim_started = True
    
    def shutdown(self):
        if self.sim_started:
            self.sim.shutdown()
            self.sim_started = False

    def should_process(self):
        return time.time() - self.last_sent_time > self.cooldown

    def process_frame(self, frame, save_path="frame_to_send.jpg"):
        self.ensure_sim_ready()
        now = time.time()

        if not self.should_process():
            return

        self.last_sent_time = now
        
        cv2.imwrite(save_path, frame)
        plate = send_to_plate_api(save_path)

        if not plate:
            print("No plate found in frame.")
            return

        verified = self.plate_filter.add(plate)

        if not verified:
            print(f"Plate '{plate}' buffered for verification...")
            return

        print(f"Verified plate: {verified}")
        authorized = self.sim.check_plate_authorization(verified)

        if authorized is False: 
            print(f"Unauthorized truck detected: {verified}")
            lat, lon = self.sim.gps_coords
            gps_text = f"Location: {lat:.6f}, {lon:.6f}" if lat and lon else ""
            self.sim.send_sms(
                PHONE_NUMBER,
                f"ALERT: Unauthorized truck detected! Plate: {verified} {gps_text}"
            )
        elif authorized is True:
            print(f"Authorized truck: {verified}")
        else:
            print("Authorization check failed or returned unexpected format.")
