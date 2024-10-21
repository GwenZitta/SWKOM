package com.example.ggy;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.dto.DocumentDto;
import com.example.ggy.service.mapper.DocumentMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentMapperTest {

    private DocumentMapper documentMapper;
    private DocumentEntity documentEntity;
    private DocumentDto documentDto;

    @BeforeEach
    void setUp() {
        documentMapper = new DocumentMapper();

        // Initialize a sample DocumentEntity
        documentEntity = new DocumentEntity();
        documentEntity.setId(1L);
        documentEntity.setName("Sample Document");
        documentEntity.setDocumenttype("pdf");
        documentEntity.setPathtodocument("/path/to/document");
        documentEntity.setDatetime("2024-12-12T00:00");

        // Initialize a sample DocumentDto
        documentDto = DocumentDto.builder()
                .id(1L)
                .name("Sample Document")
                .documenttype("pdf")
                .pathtodocument("/path/to/document")
                .datetime("2024-12-12T00:00")
                .build();
    }

    @Test
    void testMapToDto() {
        // Test mapping from DocumentEntity to DocumentDto
        DocumentDto mappedDto = documentMapper.mapToDto(documentEntity);

        // Verify that all fields are correctly mapped
        assertEquals(documentEntity.getId(), mappedDto.getId());
        assertEquals(documentEntity.getName(), mappedDto.getName());
        assertEquals(documentEntity.getDocumenttype(), mappedDto.getDocumenttype());
        assertEquals(documentEntity.getPathtodocument(), mappedDto.getPathtodocument());
        assertEquals(documentEntity.getDatetime(), mappedDto.getDatetime());
    }

    @Test
    void testMapToEntity() {
        // Test mapping from DocumentDto to DocumentEntity
        DocumentEntity mappedEntity = documentMapper.mapToEntity(documentDto);

        // Verify that all fields are correctly mapped
        assertEquals(documentDto.getId(), mappedEntity.getId());
        assertEquals(documentDto.getName(), mappedEntity.getName());
        assertEquals(documentDto.getDocumenttype(), mappedEntity.getDocumenttype());
        assertEquals(documentDto.getPathtodocument(), mappedEntity.getPathtodocument());
        assertEquals(documentDto.getDatetime(), mappedEntity.getDatetime());
    }
}
