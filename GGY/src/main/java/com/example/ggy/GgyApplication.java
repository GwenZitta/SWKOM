package com.example.ggy;

import com.example.ggy.data.repository.DocumentRepository;
import com.example.ggy.data.schema.DocumentEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GgyApplication {

    @Autowired
    private DocumentRepository dRepository;

    public static void main(String[] args) {
        SpringApplication.run(GgyApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(DocumentRepository dRepository) {
        return args -> {
            // Check if the repository is empty
            if (dRepository.count() == 0) {
                // Create and save new DocumentEntity instances
                DocumentEntity document1 = new DocumentEntity();
                document1.setName("Test Document 1");
                document1.setDocumentType("pdf");
                document1.setPathToDocument("/path/to/document1.pdf");
                document1.setDatetime(LocalDateTime.now().toString());

                DocumentEntity document2 = new DocumentEntity();
                document2.setName("Test Document 2");
                document2.setDocumentType("docx");
                document2.setPathToDocument("/path/to/document2.docx");
                document2.setDatetime(LocalDateTime.now().toString());

                // Save documents to the database
                dRepository.save(document1);
                dRepository.save(document2);

                System.out.println("Documents saved to the database.");
            } else {
                System.out.println("Documents already exist in the database.");
            }
        };
    }
}
