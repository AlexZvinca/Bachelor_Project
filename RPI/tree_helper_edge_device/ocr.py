import requests

API_TOKEN =
API_URL = "https://api.platerecognizer.com/v1/plate-reader/"

def send_to_plate_api(image_path, camera_id="cam_rpi_01", region=None):
    try:
        with open(image_path, 'rb') as img_file:
            files = {'upload': img_file}
            data = {'camera_id': camera_id}
            if region:
                data['region'] = region

            response = requests.post(
                API_URL,
                headers={'Authorization': f'Token {API_TOKEN}'},
                files=files,
                data=data
            )

        if response.status_code in [200, 201]:
            results = response.json().get('results', [])
            if results:
                plate = results[0]['plate'].upper()
                print(f"Detected plate: {plate}")
                return plate
            else:
                print("No plates detected.")
                return None
        else:
            print(f"API Error {response.status_code}: {response.text}")
            return None

    except Exception as e:
        print(f"Exception during API call: {e}")
        return None
