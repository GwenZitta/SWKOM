package com.example.ggy.service.mapper;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.dto.DocumentDto;
import org.springframework.stereotype.Service;

@Service
public class DocumentMapper extends AbstractMapper<DocumentEntity, DocumentDto>{

    @Override
    public DocumentDto mapToDto(DocumentEntity source){
        return DocumentDto.builder()
                .id(source.getId())
                .name(source.getName())
                .documentType(source.getDocumentType())
                .pathToDocument(source.getPathToDocument())
                .datetime(source.getDatetime())
                .build();
    }
}
