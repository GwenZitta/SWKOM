# Wähle ein Python-Image als Basis
FROM python:3.9-slim

# Setze das Arbeitsverzeichnis im Container
WORKDIR /app

# Kopiere die requirements.txt Datei und installiere die Abhängigkeiten
COPY requirements.txt .

# Installiere Tesseract, Poppler und andere Abhängigkeiten
RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    poppler-utils \
    && pip install --no-cache-dir -r requirements.txt

# Kopiere den gesamten Code in den Container
COPY . .

# Setze den Befehl zum Ausführen des OCR Workers
CMD ["python", "ocr_worker.py"]
