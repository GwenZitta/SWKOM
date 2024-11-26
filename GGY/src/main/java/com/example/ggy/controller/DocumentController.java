package com.example.ggy.controller;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // API-Endpunkt zum Erstellen eines Dokuments
    @PostMapping
    public ResponseEntity<String> createDocument(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("documenttype") String documenttype,
                                                 @RequestParam("datetime") String datetime) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("No file uploaded", HttpStatus.BAD_REQUEST);
        }

        try {
            documentService.save(file, name, documenttype, datetime);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload file and document", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API-Endpunkt zum Abrufen aller Dokumente
    @GetMapping
    public List<DocumentEntity> getAllDocuments() {
        return documentService.findAll();
    }
}
