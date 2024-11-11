package com.example.ggy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ggy.data.repository.DocumentRepository;
import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.DocumentService;
import com.example.ggy.service.mapper.DocumentMapper;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository dRepository;
    @Autowired
    private DocumentMapper dMapper;



    @Override
    public DocumentEntity save(DocumentEntity document) {
        System.out.println("saved ok new document");
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
        return dRepository.findByNameContaining(searchtext);
    }

    @Override
    public DocumentEntity update(Long id, DocumentEntity updatedDocument) {
        return dRepository.findById(id).map(existingDocument -> {
            existingDocument.setName(updatedDocument.getName());
            existingDocument.setDocumentType(updatedDocument.getDocumentType());
            existingDocument.setPathToDocument(updatedDocument.getPathToDocument());
            existingDocument.setDatetime(updatedDocument.getDatetime());
            return dRepository.save(existingDocument);
        }).orElse(null); // Handle the case where the document isn't found
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
