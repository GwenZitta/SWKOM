package com.example.ggy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.dto.DocumentDto;
import com.example.ggy.service.mapper.DocumentMapper;

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
        documentEntity.setDocumentType("pdf");
        documentEntity.setPathToDocument("/path/to/document");
        documentEntity.setDatetime("2024-12-12T00:00");

        // Initialize a sample DocumentDto
        documentDto = DocumentDto.builder()
                .id(1L)
                .name("Sample Document")
                .documentType("pdf")
                .pathToDocument("/path/to/document")
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
        assertEquals(documentEntity.getDocumentType(), mappedDto.getDocumentType());
        assertEquals(documentEntity.getPathToDocument(), mappedDto.getPathToDocument());
        assertEquals(documentEntity.getDatetime(), mappedDto.getDatetime());
    }

    @Test
    void testMapToEntity() {
        // Test mapping from DocumentDto to DocumentEntity
        DocumentEntity mappedEntity = documentMapper.mapToEntity(documentDto);

        // Verify that all fields are correctly mapped
        assertEquals(documentDto.getId(), mappedEntity.getId());
        assertEquals(documentDto.getName(), mappedEntity.getName());
        assertEquals(documentDto.getDocumentType(), mappedEntity.getDocumentType());
        assertEquals(documentDto.getPathToDocument(), mappedEntity.getPathToDocument());
        assertEquals(documentDto.getDatetime(), mappedEntity.getDatetime());
    }
}
