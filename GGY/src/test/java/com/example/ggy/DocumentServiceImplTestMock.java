package com.example.ggy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.ggy.data.repository.DocumentRepository;
import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.dto.DocumentDto;
import com.example.ggy.service.impl.DocumentServiceImpl;
import com.example.ggy.service.mapper.DocumentMapper;

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

    @Test
    void testUpdateDocument() {
        // Mock the behavior of the repository and mapper
        when(documentRepository.findById(1L)).thenReturn(java.util.Optional.of(documentEntity));
        when(documentRepository.save(documentEntity)).thenReturn(documentEntity);
        when(documentMapper.mapToDto(documentEntity)).thenReturn(documentDto);

        // Modify the documentEntity (simulating an update)
        documentEntity.setName("Updated Document");

        // Execute the service method
        DocumentEntity updatedEntity = documentService.update(1L, documentEntity);

        // Verify interactions and assert results
        verify(documentRepository, times(1)).findById(1L);
        verify(documentRepository, times(1)).save(documentEntity);
        assertEquals("Updated Document", updatedEntity.getName());
    }

    @Test
    void testDeleteDocument() {
        // Mock the behavior of the repository
        doNothing().when(documentRepository).deleteById(1L);

        // Execute the service method
        documentService.delete(1L);

        // Verify that deleteById was called once
        verify(documentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindBySearch() {
        // Mock some search results
        List<DocumentEntity> mockDocuments = List.of(documentEntity);
        when(documentRepository.findByNameContaining("Test")).thenReturn(mockDocuments);

        // Execute the service method
        List<DocumentEntity> foundDocuments = documentService.findBySearch("Test");

        // Verify the repository interaction
        verify(documentRepository, times(1)).findByNameContaining("Test");

        // Assert the correct behavior
        assertFalse(foundDocuments.isEmpty());
        assertEquals(1, foundDocuments.size());
        assertEquals("Test Document", foundDocuments.get(0).getName());
    }

    @Test
    void testFindById() {
        // Mock the repository behavior for finding by ID
        when(documentRepository.findById(1L)).thenReturn(java.util.Optional.of(documentEntity));

        // Execute the service method
        DocumentEntity foundEntity = documentService.findById(1L);

        // Verify the repository interaction
        verify(documentRepository, times(1)).findById(1L);

        // Assert the correct behavior
        assertNotNull(foundEntity);
        assertEquals("Test Document", foundEntity.getName());
    }
}
