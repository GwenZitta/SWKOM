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
import time
import logging
from elasticsearch import Elasticsearch
import uuid

# MinIO-Konfiguration aus Umgebungsvariablen
MINIO_URL = os.getenv("MINIO_URL", "http://minio:9000")
MINIO_ACCESS_KEY = os.getenv("MINIO_ACCESS_KEY", "minioadmin")
MINIO_SECRET_KEY = os.getenv("MINIO_SECRET_KEY", "minioadmin")
MINIO_BUCKET_NAME = os.getenv("MINIO_BUCKET_NAME", "documents")

# Tesseract-Pfad konfigurieren (für Docker-Umgebung)
pytesseract.pytesseract.tesseract_cmd = "/usr/bin/tesseract"

# RabbitMQ-Konfiguration
#RABBITMQ_HOST = os.getenv("RABBITMQ_HOST", "rabbitmq")
#RABBITMQ_QUEUE = os.getenv("OCR_QUEUE", "documentQueue")
QUEUE_NAME = "documentQueue"
EXCHANGE_NAME = "documentExchange"
ROUTING_KEY = "document.routingKey"
RABBITMQ_HOST = os.getenv('RABBITMQ_HOST', 'localhost')
RABBITMQ_PORT = int(os.getenv('RABBITMQ_PORT', 5672))

ELASTICSEARCH_HOST = os.getenv('ELASTICSEARCH_HOST', 'elasticsearch')
ELASTICSEARCH_PORT = int(os.getenv('ELASTICSEARCH_PORT', 9200))

logging.basicConfig(
    format='%(asctime)s - %(levelname)s - %(message)s',
    level=logging.INFO
)

es = Elasticsearch([{'scheme': 'http', 'host': ELASTICSEARCH_HOST, 'port': ELASTICSEARCH_PORT}])


def insert_into_elasticsearch(doc_name, text):
    try:
        doc_id = str(uuid.uuid4())

        document = {
            "id": doc_id,
            "text": text,
            "document_name": doc_name
        }
        es.index(index="documents", id=doc_id, document=document)
        logging.info(f"Inserted document ({doc_name}) with ID {doc_id} into Elasticsearch.")
    except Exception as e:
        logging.error(f"Error inserting into Elasticsearch: {e}")


def on_message(ch, method, properties, body):
    """Callback-Funktion für Nachrichten von RabbitMQ."""
    try:
        # Nachricht als String interpretieren
        message = body.decode('utf-8')
        logging.info(f"Nachricht empfangen: {message}")

        # Falls Nachricht JSON enthält, dekodieren
        try:
            data = json.loads(message)
            partial_name = data.get("file_path", message)
        except json.JSONDecodeError:
            # Falls keine JSON-Formatierte Nachricht, nehme den reinen String
            partial_name = message

        logging.info(f"Extrahierter Dokumentname/Referenz: {partial_name}")

        # Präfix entfernen
        document_name = removePrefix(partial_name)

        logging.info(f"Document Name nach RemovePrefix: {document_name}")

        # Datei aus MinIO herunterladen und verarbeiten
        OBJECT_NAME = get_object_name_with_prefix(minio_client, MINIO_BUCKET_NAME, document_name)
        if OBJECT_NAME:
            LOCAL_FILE = f"/tmp/{OBJECT_NAME}"
            minio_client.fget_object(MINIO_BUCKET_NAME, OBJECT_NAME, LOCAL_FILE)
            logging.info(f"{OBJECT_NAME} wurde heruntergeladen.")

            # Datei verarbeiten
            if LOCAL_FILE.lower().endswith('.pdf'):
                logging.info(f"Verarbeite PDF: {LOCAL_FILE}")
                result = process_pdf(LOCAL_FILE)
            elif LOCAL_FILE.lower().endswith(('.png', '.jpg', '.jpeg', '.bmp', '.gif')):
                logging.info(f"Verarbeite Bild: {LOCAL_FILE}")
                result = process_image(LOCAL_FILE)
            else:
                logging.error(f"Unbekannter Dateityp. Bitte eine PDF- oder Bilddatei verwenden.")
                result = None

            if result:
                logging.info(f"OCR-Ergebnis: {result}")
                insert_into_elasticsearch(document_name, result)
            else:
                logging.error(f"Fehler bei der OCR-Verarbeitung.")
        else:
            logging.error(f"Dokument mit Name '{document_name}' nicht gefunden.")
        ch.basic_ack(delivery_tag=method.delivery_tag)

    except Exception as e:
        logging.error(f"Fehler beim Verarbeiten der Nachricht: {e}")
        logging.error(traceback.format_exc())


# RabbitMQ-Verbindung einrichten und Nachrichten konsumieren
def start_rabbitmq_consumer():
    connection = None
    retry_intervall = 5

    while True:
        try:
            #connection = pika.BlockingConnection(
            #    pika.ConnectionParameters(
            #        host="172.19.0.4",
            #        port=5672,
            #        credentials=pika.PlainCredentials('test', 'test')
            #    )
            #)
            connection = pika.BlockingConnection(
                pika.ConnectionParameters(host=RABBITMQ_HOST, port=RABBITMQ_PORT)
            )

            logging.info(f"Erfolgreich mit RabbitMQ verbunden.")
            break
        except pika.exceptions.AMQPConnectionError as e:
            logging.error(f"Fehler bei der Verbindung zu RabbitMQ: {e}")
            logging.info(f"Warte {retry_intervall} Sekunden bevor der nächste Versuch erfolgt.")
            time.sleep(retry_intervall)

    if connection:
        try:
            channel = connection.channel()
            #channel.queue_declare(queue=RABBITMQ_QUEUE, durable=True)
            channel.queue_declare(queue=QUEUE_NAME, durable=True)
            channel.queue_bind(exchange=EXCHANGE_NAME, queue=QUEUE_NAME, routing_key=ROUTING_KEY)

            logging.info(f"Warte auf Nachrichten in der Queue '{QUEUE_NAME}'...")

            #channel.basic_consume(queue=RABBITMQ_QUEUE, on_message_callback=on_message, auto_ack=True)
            channel.basic_consume(queue=QUEUE_NAME, on_message_callback=on_message)

            logging.info(f"Warte auf Nachrichten...")
            channel.start_consuming()
        except Exception as e:
            logging.error(f"Fehler beim Konsumieren von Nachrichten: {e}")
            logging.error(traceback.format_exc())
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
        logging.error(f"Fehler bei der Verarbeitung des PDFs: {e}")
        logging.error(traceback.format_exc())
        return None


def process_image(image_path):
    try:
        img = Image.open(image_path)
        text = pytesseract.image_to_string(img)
        return text.strip()
    except Exception as e:
        logging.error(f"Fehler bei der Verarbeitung des Bildes: {e}")
        logging.error(traceback.format_exc())
        return None


def get_object_name_with_prefix(minio_client, bucket_name, partial_name):
    try:
        objects = minio_client.list_objects(bucket_name)
        for obj in objects:
            if partial_name in obj.object_name:
                return obj.object_name
        return None
    except Exception as e:
        logging.error(f"Fehler beim Abrufen der Objektnamen: {e}")
        logging.error(traceback.format_exc())
        return None


if __name__ == "__main__":
    logging.info(f"OCR WORKER START")
    try:
        minio_client = Minio(
            MINIO_URL.replace("http://", "").replace("https://", ""),
            access_key=MINIO_ACCESS_KEY,
            secret_key=MINIO_SECRET_KEY,
            secure=False
        )
        logging.info(f"Verbindung mit MinIO erfolgreich!")
    except Exception as e:
        logging.error(f"Fehler bei der Verbindung mit MinIO: {e}")
        logging.error(traceback.format_exc())
        exit(1)

    start_rabbitmq_consumer()
