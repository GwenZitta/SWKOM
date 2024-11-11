package com.example.ggy.service.mapper;

import org.springframework.stereotype.Service;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.dto.DocumentDto;

@Service
public class DocumentMapper extends AbstractMapper<DocumentEntity, DocumentDto> {

    @Override
    public DocumentDto mapToDto(DocumentEntity source) {
        return DocumentDto.builder()
                .id(source.getId())
                .name(source.getName())
                .documentType(source.getDocumentType())
                .pathToDocument(source.getPathToDocument())
                .datetime(source.getDatetime())
                .build();
    }

    public DocumentEntity mapToEntity(DocumentDto dto) {
        DocumentEntity entity = new DocumentEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPathToDocument(dto.getPathToDocument());
        entity.setDatetime(dto.getDatetime());
        entity.setDocumentType(dto.getDocumentType());
        // other mappings...
        return entity;
    }
}
