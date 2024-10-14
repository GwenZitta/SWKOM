package com.example.ggy.service.impl;

import com.example.ggy.data.repository.DocumentRepository;
import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository dRepository;

    @Override
    public DocumentEntity save(DocumentEntity document) {
        return dRepository.save(document);
    }

    @Override
    public List<DocumentEntity> findAll() {
        return dRepository.findAll();
    }

    @Override
    public DocumentEntity findById(Long id) {
        return dRepository.findById(id).orElse(null);
    }

    @Override
    public List<DocumentEntity> findBySearch(String searchtext) {
        return null;
    }

    @Override
    public DocumentEntity update(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {
        dRepository.deleteById(id);
    }
}
