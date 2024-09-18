package com.example.ggy.data.repository;

import com.example.ggy.data.schema.documentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface documentRepository extends JpaRepository<documentEntity, Long> {


}
