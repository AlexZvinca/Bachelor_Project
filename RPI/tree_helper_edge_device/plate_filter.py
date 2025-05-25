import time
from collections import defaultdict

class PlateFilter:
    def __init__(self, min_confirmations=3, dedup_window=60):
        self.buffer = defaultdict(int)
        self.last_verified_time = {}
        self.min_confirmations = min_confirmations
        self.dedup_window = dedup_window

    def add(self, plate):
        if not plate:
            return None

        self.buffer[plate] += 1

        if self.buffer[plate] >= self.min_confirmations:
            now = time.time()

            # Check deduplication
            last_seen = self.last_verified_time.get(plate, 0)
            if (now - last_seen) > self.dedup_window:
                self.last_verified_time[plate] = now
                self.buffer.clear()
                return plate

        return None