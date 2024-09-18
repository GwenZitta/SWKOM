package com.example.ggy.service;

import com.example.ggy.data.schema.documentEntity;

import java.util.List;

public interface documentService {


    documentEntity save(documentEntity document);

    List<documentEntity> findAll();

    documentEntity findById(Long id);

    List<documentEntity> findBySearch(String searchtext);

    documentEntity update(Long id);

    void delete(Long id);
}
