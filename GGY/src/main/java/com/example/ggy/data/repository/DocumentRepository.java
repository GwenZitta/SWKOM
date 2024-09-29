package com.example.ggy.data.repository;

import com.example.ggy.data.schema.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {


}
