import pytesseract
import fitz  # PyMuPDF
import argparse
from PIL import Image
import io
import os
from minio import Minio

# MinIO-Konfiguration aus Umgebungsvariablen
MINIO_URL = os.getenv("MINIO_URL", "http://minio:9000")
MINIO_ACCESS_KEY = os.getenv("MINIO_ACCESS_KEY", "minioadmin")
MINIO_SECRET_KEY = os.getenv("MINIO_SECRET_KEY", "minioadmin")
MINIO_BUCKET_NAME = os.getenv("MINIO_BUCKET_NAME", "documents")

# Tesseract-Pfad konfigurieren (für Docker-Umgebung)
pytesseract.pytesseract.tesseract_cmd = "/usr/bin/tesseract"


# Tesseract-Pfad um nur Ocr-Worker zu testen (ohne docker)
# pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'

def process_pdf(pdf_path):
    """Konvertiert ein PDF in Bilder und führt OCR auf jedem Bild durch."""
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
        return None


def process_image(image_path):
    """Führt OCR auf einem Bild durch."""
    try:
        img = Image.open(image_path)
        text = pytesseract.image_to_string(img)
        return text.strip()
    except Exception as e:
        print(f"Fehler bei der Verarbeitung des Bildes: {e}")
        return None


if __name__ == "__main__":
    # MinIO-Client initialisieren
    try:
        minio_client = Minio(
            MINIO_URL.replace("http://", "").replace("https://", ""),
            access_key=MINIO_ACCESS_KEY,
            secret_key=MINIO_SECRET_KEY,
            secure=False  # Falls HTTPS verwendet wird, setze das auf True
        )
        print("Verbindung mit MinIO erfolgreich!")
    except Exception as e:
        print(f"Fehler bei der Verbindung mit MinIO: {e}")
        exit(1)

    # Datei von MinIO herunterladen
    OBJECT_NAME = "1733495106412_semester-project.pdf"
    LOCAL_FILE = f"/tmp/{OBJECT_NAME}"

    try:
        minio_client.fget_object(MINIO_BUCKET_NAME, OBJECT_NAME, LOCAL_FILE)
        print(f"{OBJECT_NAME} wurde heruntergeladen und liegt unter {LOCAL_FILE}.")
    except Exception as e:
        print(f"Fehler beim Herunterladen von {OBJECT_NAME}: {e}")
        exit(1)

    # Verarbeite die heruntergeladene Datei
    if LOCAL_FILE.lower().endswith('.pdf'):
        print(f"Verarbeite PDF: {LOCAL_FILE}")
        ocr_text = process_pdf(LOCAL_FILE)
    elif LOCAL_FILE.lower().endswith(('.png', '.jpg', '.jpeg', '.bmp', '.gif')):
        print(f"Verarbeite Bild: {LOCAL_FILE}")
        ocr_text = process_image(LOCAL_FILE)
    else:
        print("Unbekannter Dateityp. Bitte eine PDF- oder Bilddatei verwenden.")
        ocr_text = None

    # Ausgabe des OCR-Ergebnisses
    if ocr_text:
        print("OCR-Ergebnis:")
        print(ocr_text)
    else:
        print("Fehler bei der OCR-Verarbeitung.")
