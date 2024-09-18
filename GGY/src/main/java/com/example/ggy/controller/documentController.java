package com.example.ggy.controller;

import com.example.ggy.data.schema.documentEntity;
import com.example.ggy.service.documentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class documentController {

    @Autowired
    private documentService dService;

    @CrossOrigin
    @PostMapping("/document")
    public documentEntity createDocument(@RequestBody documentEntity document ) {
        return dService.save(document);
    }

    /*@CrossOrigin
    @GetMapping("/document")
    public List<documentEntity> getAllDocuments(){return dService.findAll();}*/

    @CrossOrigin
    @GetMapping("/document")
    public String test(){return "hello world";}

    @CrossOrigin
    @GetMapping("/document/{id}")
    public documentEntity getDocumentById(@PathVariable Long id){return dService.findById(id);}

    @CrossOrigin
    @GetMapping("/document/{searchtext}")
    public List<documentEntity> getDocumentBySearch(@PathVariable String searchtext){return dService.findBySearch(searchtext);}

    @CrossOrigin
    @PutMapping("/document/{id}")
    public documentEntity updateDocument(@PathVariable Long id){return dService.update(id);}

    @CrossOrigin
    @DeleteMapping("/document/{id}")
    public void deleteDocument(@PathVariable Long id){dService.delete(id);}


}
