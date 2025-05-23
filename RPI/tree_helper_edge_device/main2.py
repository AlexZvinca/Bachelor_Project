from comm import SIM7600

def main():
    test_plate = "TM 06 VIC"

    sim = SIM7600()

    try:
        sim.power_on()
        
        lat, lon = sim.get_gps_coordinates()
        location_text = f"Lat: {lat}, Lon: {lon}" if lat and lon else "Unknown location"
        
        authorized = sim.check_plate_authorization(test_plate)
        
        #if authorized is False:
            #print("Unauthorized truck detected!")
        sim.send_sms("+40742845779", f"ALERT: Unauthorized truck detected! Plate: {test_plate} at {location_text}")
        #elif authorized:
            #print("Truck is authorized.")
        #else:
            #print("Could not determine truck status.")

    except Exception as e:
        print(f"Exception occurred: {e}")

    finally:
        sim.power_off()
        sim.shutdown()

if __name__ == "__main__":
    main()
