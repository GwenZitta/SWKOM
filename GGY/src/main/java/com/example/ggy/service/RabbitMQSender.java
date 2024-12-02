package com.example.ggy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ggy.config.RabbitMQConfig;

import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RabbitMQSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    // Beibehalten für generische Nachrichten
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "document.routingKey", message);
        System.out.println("Message sent to RabbitMQ: " + message);
    }

    // Neue Methode für Dateipfad als JSON
    public void sendFilePathMessage(String filePath) {
        try {
            Map<String, String> message = new HashMap<>();
            message.put("file_path", filePath);

            String jsonMessage = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "document.routingKey", jsonMessage);
            System.out.println("File path message sent to RabbitMQ: " + jsonMessage);
        } catch (Exception e) {
            System.err.println("Error sending file path message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

