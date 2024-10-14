package com.example.ggy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

@SpringBootTest
@ContextConfiguration(classes = GgyApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@Commit
class DocumentServiceImplIntegrationTest {

    @Autowired
    private DocumentServiceImpl documentService;

    private DocumentEntity documentEntity;

    @BeforeEach
    void setUp() {
        documentEntity = new DocumentEntity();
        // documentEntity.setId(1L);
        documentEntity.setName("docName");
        documentEntity.setDocumenttype("pdf");
        documentEntity.setPathtodocument("root_path");
        documentEntity.setDatetime("12_12_2024");

    }

    @Test
    void testSaveDocument() {
        // Save the document using the real repository
        DocumentEntity savedEntity = documentService.save(documentEntity);

        // Verify that the document was saved to the actual database
        assertNotNull(savedEntity.getId());

        DocumentEntity foundEntity = documentService.findById(savedEntity.getId());

        assertEquals("docName", foundEntity.getName());

    }
}
