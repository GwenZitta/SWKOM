package com.example.ggy.service.impl;

import com.example.ggy.data.repository.DocumentRepository;
import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.DocumentService;
import com.example.ggy.service.mapper.DocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository dRepository;
    @Autowired
    private DocumentMapper dMapper;



    @Override
    public DocumentEntity save(DocumentEntity document) {
        return null;
    }

    @Override
    public List<DocumentEntity> findAll() {
        return null;
    }

    @Override
    public DocumentEntity findById(Long id) {
        return null;
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

    }
}
