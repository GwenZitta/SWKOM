package com.example.ggy;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.data.repository.DocumentRepository;
import com.example.ggy.service.impl.DocumentServiceImpl;
import com.example.ggy.service.minio.MinioService;
import com.example.ggy.service.RabbitMQSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class DocumentServiceImplIntegrationTest {

    private DocumentServiceImpl documentService;
    private DocumentRepository documentRepository;
    private MinioService minioService;
    private RabbitMQSender rabbitMQSender;

    @BeforeEach
    void setUp() {
        // Mock die Abhängigkeiten
        documentRepository = mock(DocumentRepository.class);
        minioService = mock(MinioService.class);
        rabbitMQSender = mock(RabbitMQSender.class);

        // Erstelle die Service-Instanz
        documentService = new DocumentServiceImpl();
        documentService.documentRepository = documentRepository;
        documentService.minioService = minioService;
        documentService.rabbitMQSender = rabbitMQSender;
    }

    @Test
    void testSave() throws Exception {
        // Mock-Daten
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(minioService.uploadDocument(anyString(), eq(mockFile))).thenReturn("http://minio-url.com/testfile.txt");

        DocumentEntity mockDocument = new DocumentEntity();
        mockDocument.setId(1L);
        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(mockDocument);

        // Methode aufrufen
        DocumentEntity savedDocument = documentService.save(mockFile, "TestDocument", "PDF", "2025-01-01");

        // Verifiziere Minio-Aufruf
        verify(minioService, times(1)).uploadDocument(anyString(), eq(mockFile));

        // Verifiziere Datenbank-Aufruf
        ArgumentCaptor<DocumentEntity> documentCaptor = ArgumentCaptor.forClass(DocumentEntity.class);
        verify(documentRepository, times(1)).save(documentCaptor.capture());
        DocumentEntity capturedDocument = documentCaptor.getValue();

        assertEquals("TestDocument", capturedDocument.getName());
        assertEquals("PDF", capturedDocument.getDocumentType());
        assertEquals("2025-01-01", capturedDocument.getDatetime());
        assertEquals("http://minio-url.com/testfile.txt", capturedDocument.getPathToDocument());

        // Verifiziere RabbitMQ-Aufruf
        verify(rabbitMQSender, times(1)).sendMessage("TestDocument");

        // Assertions für Rückgabewert
        assertNotNull(savedDocument);
        assertEquals(1L, savedDocument.getId());
    }

    @Test
    void testSaveThrowsExceptionOnMinIOError() throws Exception {
        // Mock Fehler beim MinIO-Upload
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(minioService.uploadDocument(anyString(), eq(mockFile))).thenThrow(new RuntimeException("MinIO error"));

        // Test und Erwartung
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            documentService.save(mockFile, "TestDocument", "PDF", "2025-01-01");
        });

        assertEquals("Error uploading file and document", exception.getMessage());

        // Verifiziere, dass keine weiteren Aufrufe stattfanden
        verify(documentRepository, never()).save(any());
        verify(rabbitMQSender, never()).sendMessage(anyString());
    }
}
