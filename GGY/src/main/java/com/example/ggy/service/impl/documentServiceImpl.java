package com.example.ggy.service.impl;

import com.example.ggy.data.repository.documentRepository;
import com.example.ggy.data.schema.documentEntity;
import com.example.ggy.service.documentService;
import com.example.ggy.service.mapper.documentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class documentServiceImpl implements documentService {

    @Autowired
    private documentRepository dRepository;
    @Autowired
    private documentMapper dMapper;



    @Override
    public documentEntity save(documentEntity document) {
        return null;
    }

    @Override
    public List<documentEntity> findAll() {
        return null;
    }

    @Override
    public documentEntity findById(Long id) {
        return null;
    }

    @Override
    public List<documentEntity> findBySearch(String searchtext) {
        return null;
    }

    @Override
    public documentEntity update(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
