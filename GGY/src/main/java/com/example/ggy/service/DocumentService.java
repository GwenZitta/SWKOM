package com.example.ggy.service;

import com.example.ggy.data.schema.DocumentEntity;

import java.util.List;

public interface DocumentService {


    DocumentEntity save(DocumentEntity document);

    List<DocumentEntity> findAll();

    DocumentEntity findById(Long id);

    List<DocumentEntity> findBySearch(String searchtext);

    DocumentEntity update(Long id);

    void delete(Long id);
}
