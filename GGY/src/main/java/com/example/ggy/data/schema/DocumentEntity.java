package com.example.ggy.data.schema;

import org.springframework.web.multipart.MultipartFile;

//import org.springframework.lang.NonNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "document")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Document name cannot be blank")
    @Size(min = 3, max = 100, message = "Document name must be between 3 and 100 characters")
    private String name;
    @NotBlank(message = "Document type cannot be blank")
    @Size(min = 3, max = 10, message = "Document type must be between 3 and 10 characters")
    private String documentType;
    @NotBlank(message = "Path to document cannot be blank")
    private String pathToDocument;
    @NotNull(message = "Date and time cannot be null")
    private String datetime;

    // Transient field to hold the uploaded file
    @Transient
    private MultipartFile file;

}
