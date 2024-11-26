package com.example.ggy.service.impl;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.data.repository.DocumentRepository;
import com.example.ggy.service.DocumentService;
import com.example.ggy.service.minio.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private MinioService minioService;

    @Override
    public DocumentEntity save(MultipartFile file, String name, String documentType, String datetime) {
        try {
            // Zuerst die Datei auf MinIO hochladen und die URL zurückbekommen
            String fileUrl = uploadFileToMinIO(file);

            // Dokument-Objekt erstellen und in der Datenbank speichern
            DocumentEntity document = new DocumentEntity();
            document.setName(name);
            document.setDocumentType(documentType);
            document.setDatetime(datetime);
            document.setPathToDocument(fileUrl);  // Setze die MinIO URL als Pfad
            return documentRepository.save(document);
        } catch (Exception e) {
            // Logge den Fehler detailliert
            e.printStackTrace();
            throw new RuntimeException("Error uploading file and document", e);
        }
    }


    @Override
    public List<DocumentEntity> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public DocumentEntity findById(Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    @Override
    public List<DocumentEntity> findBySearch(String searchtext) {
        return documentRepository.findByNameContaining(searchtext);
    }

    @Override
    public DocumentEntity update(Long id, DocumentEntity updatedDocument) {
        return documentRepository.findById(id).map(existingDocument -> {
            existingDocument.setName(updatedDocument.getName());
            existingDocument.setDocumentType(updatedDocument.getDocumentType());
            existingDocument.setPathToDocument(updatedDocument.getPathToDocument());
            existingDocument.setDatetime(updatedDocument.getDatetime());
            return documentRepository.save(existingDocument);
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        documentRepository.deleteById(id);
    }

    private String uploadFileToMinIO(MultipartFile file) {
        try {
            // Generiere einen eindeutigen Dateinamen
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            return minioService.uploadDocument(fileName, file);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file content", e);
        }
    }
}
