package com.example.ggy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.impl.DocumentServiceImpl;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@SpringBootTest
@Transactional // Ensures the test is transactional
@Commit // Ensures that changes are committed, not rolled back
@ContextConfiguration(classes = GgyApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)

class DocumentServiceImplIntegrationTest {

    @Autowired
    private DocumentServiceImpl documentService;
    @Autowired
    private Validator validator;
    private DocumentEntity documentEntity;

    @BeforeEach
    void setUp() {
        documentEntity = new DocumentEntity();
        documentEntity.setId(1L);
        documentEntity.setName("docName");
        documentEntity.setDocumentType("pdf");
        documentEntity.setPathToDocument("root_path");
        documentEntity.setDatetime(LocalDateTime.of(2024, 12, 12, 0, 0).toString());

    }

    @Test
    void testSaveDocument() {
        // Save the document using the real repository
        DocumentEntity savedEntity = documentService.save(documentEntity);

        // Verify that the document was saved to the actual database
        assertNotNull(savedEntity.getId());

        DocumentEntity foundEntity = documentService.findById(savedEntity.getId());

        assertEquals("docName", foundEntity.getName());
        assertEquals("pdf", foundEntity.getDocumentType());
        assertEquals("root_path", foundEntity.getPathToDocument());
        assertEquals("2024-12-12T00:00", foundEntity.getDatetime()); // Ensure datetime is handled correctly

    }

    @Test
    void testUpdateDocument() {
        // First, create document
        DocumentEntity savedEntity = new DocumentEntity();

        // save the entity
        savedEntity.setId(1L);
        savedEntity.setName("old name");
        savedEntity.setDocumentType("docx");
        savedEntity.setPathToDocument("root_path");
        savedEntity.setDatetime(LocalDateTime.of(2024, 12, 12, 0, 0).toString());
        documentService.save(savedEntity);

        savedEntity.setName("new name");

        documentService.update(1L, savedEntity);
        DocumentEntity foundEntity = documentService.findById(savedEntity.getId());

        // Verify that the updates were saved
        assertEquals("new name", foundEntity.getName());
        assertEquals("docx", foundEntity.getDocumentType());
        assertEquals(savedEntity.getId(), foundEntity.getId()); // Ensure the same ID is preserved
    }

    @Test
    void testDeleteDocument() {
        // First, save the document
        DocumentEntity savedEntity = documentService.save(documentEntity);

        // Delete the document
        documentService.delete(savedEntity.getId());

        // Try to find the document (should return null or throw an exception)
        DocumentEntity foundEntity = documentService.findById(savedEntity.getId());

        // Verify the document has been deleted
        assertEquals(null, foundEntity);
    }

    @Test
    void testFindBySearch() {
        // Save some documents
        DocumentEntity doc1 = new DocumentEntity();
        doc1.setName("Test Document 1");
        doc1.setDocumentType("pdf");
        doc1.setPathToDocument("path1");
        doc1.setDatetime(LocalDateTime.now().toString());

        DocumentEntity doc2 = new DocumentEntity();
        doc2.setName("Another Test Document");
        doc2.setDocumentType("docx");
        doc2.setPathToDocument("path2");
        doc2.setDatetime(LocalDateTime.now().toString());

        documentService.save(doc1);
        documentService.save(doc2);

        // Test searching for documents that contain "Test"
        List<DocumentEntity> foundDocuments = documentService.findBySearch("Test");

        // Verify that the search returns the correct results
        assertNotNull(foundDocuments);
        assertTrue(foundDocuments.size() >= 1);
        assertTrue(foundDocuments.stream().anyMatch(doc -> doc.getName().contains("Test")));
    }

    @Test
    void testInvalidDocumentEntity() {
        // Leave fields blank to trigger validation errors
        documentEntity.setName(""); // Invalid name (too short)
        documentEntity.setDocumentType(""); // Invalid document type
        documentEntity.setPathToDocument(""); // Invalid path
        documentEntity.setDatetime(null); // Null datetime

        // Perform validation
        Set<ConstraintViolation<DocumentEntity>> violations = validator.validate(documentEntity);

        // Ensure that there are validation errors
        assertFalse(violations.isEmpty());
    }
}
