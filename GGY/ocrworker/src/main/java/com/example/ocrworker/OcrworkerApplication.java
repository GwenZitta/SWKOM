package com.example.ocrworker;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.Payload;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class OcrworkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcrworkerApplication.class, args);
    }

    // RabbitListener-Methode für den OCR-Worker
    @RabbitListener(queues = "documentQueue")
    public void receiveMessage(@Payload String message) {
        System.out.println("Received message: " + message);

        try {
            String filePath = parseMessageForFilePath(message);

            String ocrResult = runOcr(filePath);

            System.out.println("OCR Result: " + ocrResult);

        } catch (Exception e) {
            System.err.println("Error processing OCR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hilfsfunktion, um den Dateipfad aus der Nachricht zu extrahieren
    private String parseMessageForFilePath(String message) {
        // Für ein einfaches JSON-Format: {"file_path": "/path/to/file"}
        return message.replace("{\"file_path\":\"", "").replace("\"}", "");
    }

    // OCR-Logik mit Tess4J
    private String runOcr(String filePath) throws IOException, TesseractException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        // Bild laden
        BufferedImage image = ImageIO.read(file);

        net.sourceforge.tess4j.Tesseract tesseract = new net.sourceforge.tess4j.Tesseract();
        tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
        return tesseract.doOCR(image);
    }

}
