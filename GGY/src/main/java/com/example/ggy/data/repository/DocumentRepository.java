package com.example.ggy.data.repository;

import com.example.ggy.data.schema.DocumentEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("DocumentRepository")
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    // Custom query method to find documents by name containing the search text
    List<DocumentEntity> findByNameContaining(String searchtext);

}
