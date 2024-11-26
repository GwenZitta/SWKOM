package com.example.ggy.controller;

import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.DocumentService;
import com.example.ggy.service.RabbitMQSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
@Validated
public class DocumentController {

    private final DocumentService dService;
    private final RabbitMQSender rabbitMQSender;

    @Autowired
    public DocumentController(DocumentService dService, RabbitMQSender rabbitMQSender) {
        this.dService = dService;
        this.rabbitMQSender = rabbitMQSender;
    }

    @CrossOrigin
    @GetMapping("")
    ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello World!!!!", HttpStatus.OK);
    }

    // Define the file storage path in application.properties or application.yml
    @Value("${file.upload-dir}")
    private String uploadDir;

    @CrossOrigin
    @PostMapping("/document")
    public ResponseEntity<String> createDocument(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("documenttype") String documenttype,
                                                 @RequestParam("datetime") String datetime) {
        // Define the full upload directory path
        File directory = new File(uploadDir);

        // Ensure the directory exists
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdirs();

        }

        // Generate a unique file name to prevent overwrites
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File destinationFile = new File(directory, fileName);

        try {
            // Save the file to the destination path
            file.transferTo(destinationFile);

            // Set up and save the document entity
            DocumentEntity document = DocumentEntity.builder()
                    .name(name)
                    .documentType(documenttype)
                    .datetime(datetime)
                    .pathToDocument(destinationFile.getAbsolutePath()) // Store the file path
                    .file(file)
                    .build();

            DocumentEntity umentEntitysaved_doc = dService.save(document); // Save metadata in the database
            rabbitMQSender.sendMessage("New document created with ID: " + umentEntitysaved_doc.getId());
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @CrossOrigin
    @GetMapping("/document")
    public List<DocumentEntity> getAllDocuments() {
        return dService.findAll();
    }

    @CrossOrigin
    @GetMapping("/document/{id}")
    public DocumentEntity getDocumentById(@PathVariable Long id) {
        return dService.findById(id);
    }

    @CrossOrigin
    @GetMapping("/document/{searchtext}")
    public List<DocumentEntity> getDocumentBySearch(@PathVariable String searchtext) {
        return dService.findBySearch(searchtext);
    }

    @CrossOrigin
    @PutMapping("/document/{id}")
    public DocumentEntity updateDocument(@PathVariable Long id) {
        return dService.update(id);
    }

    @CrossOrigin
    @DeleteMapping("/document/{id}")
    public void deleteDocument(@PathVariable Long id) {
        dService.delete(id);
    }

}