import RPi.GPIO as GPIO
import serial
import time

class SIM7600:
    def __init__(self, port="/dev/ttyS0", baudrate=115200, power_key=6, pin_code='0000'):
        self.ser = serial.Serial(port, baudrate, timeout=1)
        self.power_key = power_key
        self.pin_code = pin_code
        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)
        GPIO.setup(self.power_key, GPIO.OUT)

    def power_on(self):
        print("\nPowering on SIM7600...")
        GPIO.output(self.power_key, GPIO.HIGH)
        time.sleep(2)
        GPIO.output(self.power_key, GPIO.LOW)
        time.sleep(20)
        self.ser.flushInput()
        print("SIM7600 is ready")

    def power_off(self):
        print("\nPowering off SIM7600...")
        GPIO.output(self.power_key, GPIO.HIGH)
        time.sleep(3)
        GPIO.output(self.power_key, GPIO.LOW)
        time.sleep(18)
        print("SIM7600 is off")

    def send_at(self, command, expected, timeout=2):
        print(f"\n'{command}' →")
        self.ser.write((command + '\r').encode())
        time.sleep(timeout)
        if self.ser.inWaiting():
            response = self.ser.read(self.ser.inWaiting()).decode(errors='ignore')
            print(response)
            return expected in response, response
        return False, ""

    def send_sms(self, phone_number, message):
        print("\nSending SMS alert...")
        self.send_at("AT", "OK", 1)
        self.send_at("AT+CMGF=1", "OK", 1)
        self.send_at('AT+CSCS="GSM"', "OK", 1)
        self.send_at('AT+CSCA="+40744946000"', "OK", 1)

        self.ser.write(f'AT+CMGS="{phone_number}"\r'.encode())
        time.sleep(2)
        if self.ser.inWaiting():
            prompt = self.ser.read(self.ser.inWaiting()).decode(errors='ignore')
            if '>' in prompt:
                self.ser.write(message.encode() + b"\x1A")
                print("Message sent!")
                time.sleep(5)
                if self.ser.inWaiting():
                    print("SMS response:\n", self.ser.read(self.ser.inWaiting()).decode(errors='ignore'))
            else:
                print("No '>' prompt for SMS.")
        else:
            print("No modem response after CMGS.")

    def check_plate_authorization(self, plate_number):
        print(f"\nChecking plate: {plate_number}")
        encoded_plate = plate_number.replace(" ", "%20")
        url = f"https://aef7-81-181-70-236.ngrok-free.app/authorizationRequest/check?plate={encoded_plate}"

        self.send_at("AT", "OK", 1)
        ok, _ = self.send_at('AT+CPIN?', 'SIM PIN', 1)
        if ok:
            self.send_at(f'AT+CPIN="{self.pin_code}"', 'OK', 2)
            time.sleep(5)

        self.send_at('AT+CPIN?', 'READY', 2)
        self.send_at("AT+CGATT=1", "OK", 2)
        self.send_at('AT+CGDCONT=1,"IP","net"', "OK", 2)
        self.send_at("AT+HTTPINIT", "OK", 2)
        self.send_at('AT+HTTPPARA="CID",1', "OK", 2)
        self.send_at(f'AT+HTTPPARA="URL","{url}"', "OK", 2)
        self.send_at('AT+HTTPPARA="CONTENT","application/json"', "OK", 2)

        success, _ = self.send_at("AT+HTTPACTION=0", "+HTTPACTION", 5)
        authorized = None

        if success:
            self.ser.write(b'AT+HTTPREAD=0,100\r')
            time.sleep(3)
            if self.ser.inWaiting():
                body = self.ser.read(self.ser.inWaiting()).decode(errors='ignore').lower()
                print("HTTP Body:\n", body)
                if '"authorized":false' in body:
                    authorized = False
                elif '"authorized":true' in body:
                    authorized = True
                else:
                    print("Unexpected JSON format.")
        else:
            print("HTTPACTION failed.")

        self.send_at("AT+HTTPTERM", "OK", 2)
        return authorized

    def get_gps_coordinates(self, timeout=90):
        print("Enabling GPS...")
        self.send_at("AT+CGPS=1,1", "OK", 1)
        time.sleep(5)

        print(f"📡 Waiting for GPS fix (up to {timeout} seconds)...")
        start_time = time.time()

        while time.time() - start_time < timeout:
            self.ser.write(b'AT+CGPSINFO\r')
            time.sleep(2)

            if self.ser.inWaiting():
                raw = self.ser.read(self.ser.inWaiting()).decode(errors='ignore')
                print("GPS Raw:", raw)

                if "+CGPSINFO:" in raw:
                    gps_data = raw.split("+CGPSINFO:")[1].split('\n')[0].strip()
                    if gps_data and ',' in gps_data and ',,,,,,' not in gps_data:
                        fields = gps_data.split(',')

                        try:
                            lat_str, ns, lon_str, ew = fields[0], fields[1], fields[2], fields[3]
                            lat_deg = float(lat_str[:2])
                            lat_min = float(lat_str[2:])
                            lon_deg = float(lon_str[:3])
                            lon_min = float(lon_str[3:])

                            lat = lat_deg + (lat_min / 60)
                            lon = lon_deg + (lon_min / 60)

                            if ns == 'S': lat = -lat
                            if ew == 'W': lon = -lon

                            print(f"GPS fix: Lat {lat}, Lon {lon}")
                            return lat, lon
                        except Exception as e:
                            print(f"⚠ Parsing error: {e}")

            print("Still waiting for GPS fix...")
            time.sleep(3)

        print("GPS timeout — no fix acquired.")
        self.send_at("AT+CGPS=0", "OK", 1)
        return None, None

    def shutdown(self):
        if self.ser:
            self.ser.close()
        GPIO.cleanup()