import sys
import asyncio
import websockets
import json
from vosk import Model, KaldiRecognizer


if sys.platform.startswith('win'):
    asyncio.set_event_loop_policy(asyncio.WindowsSelectorEventLoopPolicy())

MODEL_PATH = r"C:\vosk_server\vosk-model-small-ru-0.22"

print("Loading Vosk model...")
model = Model(MODEL_PATH)
print("Model loaded.")

async def handle(websocket):  
    print(f"Client connected: {websocket.remote_address}")

    rec = KaldiRecognizer(model, 16000)

    try:
        async for message in websocket:
            if not isinstance(message, (bytes, bytearray)):
                continue

            

            if rec.AcceptWaveform(message):
                result = json.loads(rec.Result())
                text = result.get("text", "")
            else:
                partial = json.loads(rec.PartialResult())
                text = partial.get("partial", "")

            if text:
                await websocket.send(text)

    except websockets.ConnectionClosed:
        print(f"Client disconnected: {websocket.remote_address}")
    except Exception as e:
        print("Error:", e)

async def main():
    print("Starting server...")
    async with websockets.serve(handle, "0.0.0.0", 8765):
        print("Vosk server started on ws://0.0.0.0:8765")
        await asyncio.Future()  

if __name__ == "__main__":
    asyncio.run(main())