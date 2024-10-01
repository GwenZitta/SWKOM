package com.example.ggy.controller;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping
public class DocumentController {

    @Autowired
    private DocumentService dService;


    @CrossOrigin
    @GetMapping("")
    ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello World!", HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/document")
    public DocumentEntity createDocument(@RequestBody DocumentEntity document ) {
        return dService.save(document);
    }

    @CrossOrigin
    @GetMapping("/document")
    public List<DocumentEntity> getAllDocuments(){return dService.findAll();}

    @CrossOrigin
    @GetMapping("/document/{id}")
    public DocumentEntity getDocumentById(@PathVariable Long id){return dService.findById(id);}

    @CrossOrigin
    @GetMapping("/document/{searchtext}")
    public List<DocumentEntity> getDocumentBySearch(@PathVariable String searchtext){return dService.findBySearch(searchtext);}

    @CrossOrigin
    @PutMapping("/document/{id}")
    public DocumentEntity updateDocument(@PathVariable Long id){return dService.update(id);}

    @CrossOrigin
    @DeleteMapping("/document/{id}")
    public void deleteDocument(@PathVariable Long id){dService.delete(id);}


}
