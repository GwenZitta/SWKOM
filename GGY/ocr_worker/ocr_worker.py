import pika
import time
from PIL import Image
from io import BytesIO
import requests
import pytesseract

# RabbitMQ Configuration
QUEUE_NAME = "documentQueue"
EXCHANGE_NAME = "documentExchange"
ROUTING_KEY = "document.routingKey"
RABBITMQ_HOST = "localhost"  # Use the service name defined in docker-compose
RABBITMQ_PORT = 15672  # Default port for RabbitMQ


def download_file(file_url):
    """Lädt die Datei von der angegebenen URL herunter."""
    try:
        response = requests.get(file_url)
        response.raise_for_status()
        return BytesIO(response.content)
    except Exception as e:
        print(f"Fehler beim Herunterladen der Datei: {e}")
        return None


def perform_ocr(file_content):
    """Führt OCR auf dem heruntergeladenen Bild aus."""
    try:
        image = Image.open(file_content)
        ocr_result = pytesseract.image_to_string(image)
        return ocr_result.strip()
    except Exception as e:
        print(f"Fehler bei der OCR-Verarbeitung: {e}")
        return None


def on_message(channel, method, properties, body):
    """Callback-Funktion für empfangene Nachrichten."""
    print(f"Erhaltene Nachricht: {body.decode()}")
    file_url = body.decode()

    # Datei herunterladen
    file_content = download_file(file_url)
    if not file_content:
        print("Datei konnte nicht heruntergeladen werden.")
        return

    # OCR durchführen
    ocr_result = perform_ocr(file_content)
    if ocr_result:
        print(f"OCR-Ergebnis: {ocr_result}")
    else:
        print("OCR-Verarbeitung fehlgeschlagen.")

    # Nachricht bestätigen
    channel.basic_ack(delivery_tag=method.delivery_tag)


def start_ocr_worker():
    retry_interval = 5  # Retry every 5 seconds
    while True:
        try:
            # Verbindung zu RabbitMQ
            connection = pika.BlockingConnection(pika.ConnectionParameters(host=RABBITMQ_HOST))
            channel = connection.channel()

            # Queue deklarieren
            channel.queue_declare(queue=QUEUE_NAME, durable=True)

            print("Verbunden mit RabbitMQ. Warte auf Nachrichten...")
            channel.basic_consume(queue=QUEUE_NAME, on_message_callback=on_message)
            channel.start_consuming()

        except pika.exceptions.AMQPConnectionError as e:
            print(f"Verbindung fehlgeschlagen: {e}. Neuer Versuch in {retry_interval} Sekunden...")
            time.sleep(retry_interval)

        except KeyboardInterrupt:
            print("Worker gestoppt durch Benutzer.")
            break

        finally:
            if 'connection' in locals() and connection.is_open:
                connection.close()


if __name__ == "__main__":
    print("OCR Worker START")
    start_ocr_worker()
