package com.example.ggy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.ggy.data.repository.DocumentRepository;
import com.example.ggy.data.schema.DocumentEntity;
import com.example.ggy.service.dto.DocumentDto;
import com.example.ggy.service.impl.DocumentServiceImpl;
import com.example.ggy.service.mapper.DocumentMapper;

class DocumentServiceImplTestMock {

}