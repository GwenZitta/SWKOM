package com.example.ggy.service.mapper;

import com.example.ggy.data.schema.documentEntity;
import com.example.ggy.service.dto.documentDto;
import org.springframework.stereotype.Service;

@Service
public class documentMapper extends AbstractMapper<documentEntity, documentDto>{

    @Override
    public documentDto mapToDto(documentEntity source){
        return documentDto.builder()
                .id(source.getId())
                .name(source.getName())
                .documenttype(source.getDocumenttype())
                .pathtodocument(source.getPathtodocument())
                .datetime(source.getDatetime())
                .build();
    }
}
