package com.example.ggy.service;

import com.example.ggy.data.schema.DocumentEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    DocumentEntity save(MultipartFile file, String name, String documentType, String datetime);

    List<DocumentEntity> findAll();

    DocumentEntity findById(Long id);

    List<DocumentEntity> search(String query);

    DocumentEntity update(Long id, DocumentEntity updatedDocument);

    void delete(Long id);
}
