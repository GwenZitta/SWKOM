package com.example.ggy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ggy.service.DocumentService;
import com.example.ggy.data.schema.DocumentEntity;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final DocumentService documentService;

    @Autowired
    public SearchController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentEntity>> searchDocuments(@RequestParam("query") String query) {
        try {
            List<DocumentEntity> results = documentService.search(query);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
