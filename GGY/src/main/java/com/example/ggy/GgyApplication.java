package com.example.ggy;

import com.example.ggy.data.repository.documentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GgyApplication {

    @Autowired
    private documentRepository dRepository;

    public static void main(String[] args) {
        SpringApplication.run(GgyApplication.class, args);
    }

}
