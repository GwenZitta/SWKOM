package com.example.ggy.service.mapper;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.dto.DocumentDto;
import org.springframework.stereotype.Service;

@Service
public class DocumentMapper extends AbstractMapper<DocumentEntity, DocumentDto> {

    @Override
    public DocumentDto mapToDto(DocumentEntity source) {
        return DocumentDto.builder()
                .id(source.getId())
                .name(source.getName())
                .documentType(source.getDocumenttype())
                .pathToDocument(source.getPathtodocument())
                .datetime(source.getDatetime())
                .build();
    }

    public DocumentEntity mapToEntity(DocumentDto dto) {
        DocumentEntity entity = new DocumentEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPathtodocument(dto.getPathToDocument());
        entity.setDatetime(dto.getDatetime());
        entity.setDocumenttype(dto.getDocumentType());
        return entity;
    }
}
