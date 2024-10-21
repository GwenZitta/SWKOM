package com.example.ggy.service;

import com.example.ggy.data.schema.DocumentEntity;

import java.util.List;

public interface DocumentService {

    DocumentEntity save(DocumentEntity document);

    List<DocumentEntity> findAll();

    DocumentEntity findById(Long id);

    List<DocumentEntity> findBySearch(String searchtext);

    public DocumentEntity update(Long id);

    DocumentEntity update(Long id, DocumentEntity updatedDocument);

    void delete(Long id);
}
