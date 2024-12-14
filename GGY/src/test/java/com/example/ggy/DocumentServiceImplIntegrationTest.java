package com.example.ggy;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional // Ensures the test is transactional
@Commit // Ensures that changes are committed, not rolled back
@ContextConfiguration(classes = GgyApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)

class DocumentServiceImplIntegrationTest {

}