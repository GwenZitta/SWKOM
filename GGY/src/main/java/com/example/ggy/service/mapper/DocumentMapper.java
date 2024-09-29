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
                .documenttype(source.getDocumenttype())
                .pathtodocument(source.getPathtodocument())
                .datetime(source.getDatetime())
                .build();
    }
}
