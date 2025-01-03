package com.example.ggy.data.repository;

import com.example.ggy.data.schema.DocumentEntity;
import org.springframework.stereotype.Repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("SearchRepository")
public interface SearchRepository extends ElasticsearchRepository<DocumentEntity, String> {
    List<DocumentEntity> findByContentContaining(String keyword);
}
