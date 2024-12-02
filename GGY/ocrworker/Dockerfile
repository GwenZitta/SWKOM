FROM openjdk:17-jdk-slim

# Install Tesseract and dependencies
RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    tesseract-ocr-eng \
    libtesseract-dev \
    && rm -rf /var/lib/apt/lists/*

# Add application JAR
COPY target/ocrworker-0.0.1-SNAPSHOT.jar /app/ocrworker.jar

# Run application
ENTRYPOINT ["java", "-jar", "/app/ocrworker.jar"]