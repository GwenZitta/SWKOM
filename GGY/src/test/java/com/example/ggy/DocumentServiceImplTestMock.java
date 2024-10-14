package com.example.ggy;

import com.example.ggy.data.repository.DocumentRepository;
import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.dto.DocumentDto;
import com.example.ggy.service.impl.DocumentServiceImpl;
import com.example.ggy.service.mapper.DocumentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentServiceImplTestMock {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentMapper documentMapper;

    @InjectMocks
    private DocumentServiceImpl documentService;

    private DocumentEntity documentEntity;
    private DocumentDto documentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        documentEntity = new DocumentEntity();
        documentEntity.setId(1L);
        documentEntity.setName("Test Document");

        documentDto = new DocumentDto();
        documentDto.setName("Test Document");
    }

    @Test
    void testSaveDocument() {
        // Mock the behavior of the repository and mapper
        when(documentMapper.mapToEntity(documentDto)).thenReturn(documentEntity);
        when(documentRepository.save(documentEntity)).thenReturn(documentEntity);
        when(documentMapper.mapToDto(documentEntity)).thenReturn(documentDto);

        // Execute the service method
        DocumentEntity savedEntity = documentService.save(documentEntity);

        // Verify interactions and assert results
        verify(documentRepository, times(1)).save(documentEntity);
        assertEquals("Test Document", savedEntity.getName());
    }

}
