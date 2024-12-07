import json
import pika
import pytesseract
import fitz  # PyMuPDF
from PIL import Image
import io
import os
import re
from minio import Minio
import traceback
import time  # Zum Warten zwischen den Versuchen

# MinIO-Konfiguration aus Umgebungsvariablen
MINIO_URL = os.getenv("MINIO_URL", "http://minio:9000")
MINIO_ACCESS_KEY = os.getenv("MINIO_ACCESS_KEY", "minioadmin")
MINIO_SECRET_KEY = os.getenv("MINIO_SECRET_KEY", "minioadmin")
MINIO_BUCKET_NAME = os.getenv("MINIO_BUCKET_NAME", "documents")

# Tesseract-Pfad konfigurieren (für Docker-Umgebung)
pytesseract.pytesseract.tesseract_cmd = "/usr/bin/tesseract"

# RabbitMQ-Konfiguration
RABBITMQ_HOST = os.getenv("RABBITMQ_HOST", "rabbitmq")
RABBITMQ_QUEUE = os.getenv("OCR_QUEUE", "documentQueue")

print(f"MINIO_URL: {MINIO_URL}")
print(f"MINIO_ACCESS_KEY: {MINIO_ACCESS_KEY}")
print(f"MINIO_SECRET_KEY: {MINIO_SECRET_KEY}")
print(f"MINIO_BUCKET_NAME: {MINIO_BUCKET_NAME}")
print(f"RABBITMQ_HOST: {RABBITMQ_HOST}")
print(f"RABBITMQ_QUEUE: {RABBITMQ_QUEUE}")

# Maximale Anzahl der Verbindungsversuche
MAX_RETRIES = 20
RETRY_DELAY = 5  # Sekunden zwischen den Versuchen


def on_message(ch, method, properties, body):
    """Callback-Funktion für Nachrichten von RabbitMQ."""
    try:
        # Nachricht als String interpretieren
        message = body.decode('utf-8')
        print(f"Nachricht empfangen: {message}")

        # Falls Nachricht JSON enthält, dekodieren
        try:
            data = json.loads(message)
            partial_name = data.get("file_path", message)
        except json.JSONDecodeError:
            # Falls keine JSON-Formatierte Nachricht, nehme den reinen String
            partial_name = message

        print(f"Extrahierter Dokumentname/Referenz: {partial_name}")

        # Präfix entfernen
        document_name = removePrefix(partial_name)

        # Datei aus MinIO herunterladen und verarbeiten
        OBJECT_NAME = get_object_name_with_prefix(minio_client, MINIO_BUCKET_NAME, document_name)
        if OBJECT_NAME:
            LOCAL_FILE = f"/tmp/{OBJECT_NAME}"
            minio_client.fget_object(MINIO_BUCKET_NAME, OBJECT_NAME, LOCAL_FILE)
            print(f"{OBJECT_NAME} wurde heruntergeladen.")

            # Datei verarbeiten
            if LOCAL_FILE.lower().endswith('.pdf'):
                print(f"Verarbeite PDF: {LOCAL_FILE}")
                result = process_pdf(LOCAL_FILE)
            elif LOCAL_FILE.lower().endswith(('.png', '.jpg', '.jpeg', '.bmp', '.gif')):
                print(f"Verarbeite Bild: {LOCAL_FILE}")
                result = process_image(LOCAL_FILE)
            else:
                print("Unbekannter Dateityp. Bitte eine PDF- oder Bilddatei verwenden.")
                result = None

            if result:
                print("OCR-Ergebnis:")
                print(result)
            else:
                print("Fehler bei der OCR-Verarbeitung.")
        else:
            print(f"Dokument mit Name '{document_name}' nicht gefunden.")

    except Exception as e:
        print(f"Fehler beim Verarbeiten der Nachricht: {e}")
        print(traceback.format_exc())  # Detaillierte Fehlerausgabe


# RabbitMQ-Verbindung einrichten und Nachrichten konsumieren
def start_rabbitmq_consumer():
    connection = None
    attempt = 0

    while attempt < MAX_RETRIES:
        try:
            connection = pika.BlockingConnection(
                pika.ConnectionParameters(
                    host="172.19.0.4",  # Stelle sicher, dass dies die korrekte IP ist
                    port=5672,
                    credentials=pika.PlainCredentials('test', 'test')
                )
            )
            print("Erfolgreich mit RabbitMQ verbunden.")
            break  # Verbindung erfolgreich, Schleife verlassen
        except pika.exceptions.AMQPConnectionError as e:
            attempt += 1
            print(f"Fehler bei der Verbindung zu RabbitMQ: {e}")
            print(f"Versuch {attempt} von {MAX_RETRIES}...")
            if attempt >= MAX_RETRIES:
                print("Maximale Anzahl der Versuche erreicht. Beende Verbindung.")
                print(traceback.format_exc())
                return
            print(f"Warte {RETRY_DELAY} Sekunden bevor der nächste Versuch erfolgt.")
            time.sleep(RETRY_DELAY)  # Warten bevor der nächste Versuch erfolgt

    if connection:
        try:
            channel = connection.channel()
            channel.queue_declare(queue=RABBITMQ_QUEUE, durable=True)

            print(f"Warte auf Nachrichten in der Queue '{RABBITMQ_QUEUE}'...")
            channel.basic_consume(queue=RABBITMQ_QUEUE, on_message_callback=on_message, auto_ack=True)
            print("Warte auf Nachrichten...")
            channel.start_consuming()
        except Exception as e:
            print(f"Fehler beim Konsumieren von Nachrichten: {e}")
            print(traceback.format_exc())  # Detaillierte Fehlerausgabe
        finally:
            connection.close()  # Verbindung immer schließen


# Entfernt das Präfix (z.B. Zahlen vor dem Dateinamen)
def removePrefix(file_name):
    regex = "^\\d+_(.*)"
    match = re.match(regex, file_name)
    if match:
        return match.group(1)
    return file_name


def process_pdf(pdf_path):
    try:
        doc = fitz.open(pdf_path)
        text = ""
        for page_num in range(doc.page_count):
            page = doc.load_page(page_num)
            pix = page.get_pixmap()
            img = Image.open(io.BytesIO(pix.tobytes("png")))
            text += pytesseract.image_to_string(img)
        return text.strip()
    except Exception as e:
        print(f"Fehler bei der Verarbeitung des PDFs: {e}")
        print(traceback.format_exc())  # Detaillierte Fehlerausgabe
        return None


def process_image(image_path):
    try:
        img = Image.open(image_path)
        text = pytesseract.image_to_string(img)
        return text.strip()
    except Exception as e:
        print(f"Fehler bei der Verarbeitung des Bildes: {e}")
        print(traceback.format_exc())  # Detaillierte Fehlerausgabe
        return None


def get_object_name_with_prefix(minio_client, bucket_name, partial_name):
    try:
        objects = minio_client.list_objects(bucket_name)
        for obj in objects:
            if partial_name in obj.object_name:
                return obj.object_name
        return None
    except Exception as e:
        print(f"Fehler beim Abrufen der Objektnamen: {e}")
        print(traceback.format_exc())  # Detaillierte Fehlerausgabe
        return None


if __name__ == "__main__":
    print("OCR WORKER START")
    try:
        minio_client = Minio(
            MINIO_URL.replace("http://", "").replace("https://", ""),
            access_key=MINIO_ACCESS_KEY,
            secret_key=MINIO_SECRET_KEY,
            secure=False
        )
        print("Verbindung mit MinIO erfolgreich!")
    except Exception as e:
        print(f"Fehler bei der Verbindung mit MinIO: {e}")
        print(traceback.format_exc())  # Detaillierte Fehlerausgabe
        exit(1)

    start_rabbitmq_consumer()
