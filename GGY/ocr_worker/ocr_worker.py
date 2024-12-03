import pytesseract
import fitz  # PyMuPDF
import argparse
from PIL import Image
import io
import os

# Setze den Pfad zu Tesseract (falls nötig)
pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'

def process_pdf(pdf_path):
    """Konvertiert ein PDF in Bilder und führt OCR auf jedem Bild durch."""
    try:
        # Öffne das PDF mit PyMuPDF
        doc = fitz.open(pdf_path)
        text = ""

        # Iteriere über jede Seite des PDFs
        for page_num in range(doc.page_count):
            page = doc.load_page(page_num)  # Lade die Seite
            pix = page.get_pixmap()  # Konvertiere die Seite in ein Bild (Pixmap)

            # Umwandeln des Pixmap in ein PIL Image
            img = Image.open(io.BytesIO(pix.tobytes("png")))  # Pixmap in Bytes umwandeln und in Image laden

            # OCR auf dem Bild durchführen
            text += pytesseract.image_to_string(img)

        return text.strip()  # Entferne führende und nachfolgende Leerzeichen
    except Exception as e:
        print(f"Fehler bei der Verarbeitung des PDFs: {e}")
        return None

def process_image(image_path):
    """Führt OCR auf einem Bild durch."""
    try:
        # Öffne das Bild
        img = Image.open(image_path)

        # OCR auf dem Bild durchführen
        text = pytesseract.image_to_string(img)

        return text.strip()  # Entferne führende und nachfolgende Leerzeichen
    except Exception as e:
        print(f"Fehler bei der Verarbeitung des Bildes: {e}")
        return None

def is_pdf(file_path):
    """Überprüft, ob die Datei eine PDF ist."""
    return file_path.lower().endswith('.pdf')

def is_image(file_path):
    """Überprüft, ob die Datei ein Bild ist."""
    image_extensions = ['.png', '.jpg', '.jpeg', '.bmp', '.gif']
    return any(file_path.lower().endswith(ext) for ext in image_extensions)

if __name__ == "__main__":
    # Argumente mit argparse verarbeiten
    parser = argparse.ArgumentParser(description="Führe OCR auf einer Datei (PDF oder Bild) durch.")
    parser.add_argument("file_path", help="Pfad zur Datei (PDF oder Bild)")

    # Argumente parsen
    args = parser.parse_args()

    # Überprüfe, ob es sich um eine PDF oder ein Bild handelt und führe die entsprechende Funktion aus
    file_path = args.file_path

    if is_pdf(file_path):
        print(f"Verarbeite PDF: {file_path}")
        ocr_text = process_pdf(file_path)
    elif is_image(file_path):
        print(f"Verarbeite Bild: {file_path}")
        ocr_text = process_image(file_path)
    else:
        print("Unbekannter Dateityp. Bitte eine PDF oder Bilddatei angeben.")
        ocr_text = None

    if ocr_text:
        print("OCR-Ergebnis:")
        print(ocr_text)
    else:
        print("Fehler bei der OCR-Verarbeitung.")
