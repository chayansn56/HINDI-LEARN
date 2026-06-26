# -*- coding: utf-8 -*-
import subprocess
import time
import os
import sys

adb_path = '/Users/chayansoni/Library/Android/sdk/platform-tools/adb'
apk_path = 'app/build/outputs/apk/debug/app-debug.apk'
dest_path = '/data/local/tmp/app-debug.apk'

if not os.path.exists(apk_path):
    print(f"Error: APK not found at {apk_path}")
    sys.exit(1)

file_size = os.path.getsize(apk_path)
print(f"APK File Size: {file_size} bytes ({file_size / (1024*1024):.2f} MB)")

# Reconnect first
print("Reconnecting to emulator-5554...")
subprocess.run([adb_path, 'disconnect'])
subprocess.run([adb_path, 'connect', '127.0.0.1:5555'])
time.sleep(1)

# We will use emulator-5554 for commands
device_target = 'emulator-5554'

# Test command
res = subprocess.run([adb_path, '-s', device_target, 'shell', 'echo', 'stable'], capture_output=True, text=True)
if 'stable' not in res.stdout:
    print("Warning: Connection to emulator-5554 is unstable. Trying 127.0.0.1:5555...")
    device_target = '127.0.0.1:5555'

print(f"Using target device: {device_target}")

# Start the receiver shell process
print("Piping APK content to device in chunks...")
proc = subprocess.Popen(
    [adb_path, '-s', device_target, 'shell', f'cat > {dest_path}'],
    stdin=subprocess.PIPE,
    stdout=subprocess.PIPE,
    stderr=subprocess.PIPE
)

chunk_size = 64 * 1024  # 64 KB
bytes_sent = 0

with open(apk_path, 'rb') as f:
    while True:
        chunk = f.read(chunk_size)
        if not chunk:
            break
        try:
            proc.stdin.write(chunk)
            proc.stdin.flush()
            bytes_sent += len(chunk)
            # Print progress
            pct = (bytes_sent / file_size) * 100
            print(f"\rProgress: {pct:.1f}% ({bytes_sent}/{file_size} bytes)", end='', flush=True)
            time.sleep(0.005)  # 5ms delay to prevent buffer overflow
        except Exception as e:
            print(f"\nWrite error at {bytes_sent} bytes: {e}")
            break

print("\nFinished sending data. Closing stream...")
proc.stdin.close()
proc.wait()

# Verify file size on device
print("Verifying file size on device...")
verify_res = subprocess.run([adb_path, '-s', device_target, 'shell', f'ls -l {dest_path}'], capture_output=True, text=True)
print(f"Device file info: {verify_res.stdout.strip()}")

# Run pm install
print("Running pm install on device...")
install_res = subprocess.run([adb_path, '-s', device_target, 'shell', f'pm install -r {dest_path}'], capture_output=True, text=True)
print(f"Installation Output:\n{install_res.stdout.strip()}")
if install_res.stderr:
    print(f"Errors:\n{install_res.stderr.strip()}")

# Start the app
print("Launching the app on the emulator...")
subprocess.run([adb_path, '-s', device_target, 'shell', 'monkey -p com.example.hindilearn -c android.intent.category.LAUNCHER 1'], capture_output=True)

# Clean up
print("Cleaning up temp APK...")
subprocess.run([adb_path, '-s', device_target, 'shell', f'rm {dest_path}'])
print("Done!")
