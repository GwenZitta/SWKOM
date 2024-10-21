package com.example.ggy;

import com.example.ggy.data.repository.DocumentRepository;
import com.example.ggy.data.schema.DocumentEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GgyApplication {

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
                document1.setDocumenttype("pdf");
                document1.setPathtodocument("/path/to/document1.pdf");
                document1.setDatetime(LocalDateTime.now().toString());

                DocumentEntity document2 = new DocumentEntity();
                document2.setName("Test Document 2");
                document2.setDocumenttype("docx");
                document2.setPathtodocument("/path/to/document2.docx");
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
