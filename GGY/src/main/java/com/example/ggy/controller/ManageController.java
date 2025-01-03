package com.example.ggy.controller;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manage")
public class ManageController {

    private final DocumentService documentService;

    @Autowired
    public ManageController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // API-Endpunkt zum Abrufen aller Dokumente
    @GetMapping("/all")
    public ResponseEntity<List<DocumentEntity>> getAllDocuments() {
        try {
            List<DocumentEntity> documents = documentService.findAll();

            System.out.println("Documents: " + documents);

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json")
                    .body(documents);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

