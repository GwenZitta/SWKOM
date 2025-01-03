package com.example.ggy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.example.ggy.service.RabbitMQSender;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class RabbitMQSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RabbitMQSender rabbitMQSender;

    @BeforeEach
    void setUp() {
        // Du kannst hier Setup-Logik hinzufügen, falls nötig
    }

    @Test
    public void testSendMessage() {
        // Arrange
        String message = "Test Message";

        // Act
        rabbitMQSender.sendMessage(message);

        // Assert: Verifiziere den richtigen Exchange-Namen
        verify(rabbitTemplate, times(1)).convertAndSend("documentExchange", "document.routingKey", message);
    }


    @Test
    public void testSendFilePathMessage() throws Exception {
        // Arrange
        String filePath = "/path/to/file.txt";
        String expectedJsonMessage = "{\"file_path\":\"/path/to/file.txt\"}";

        // Simuliere die Objektumwandlung
        when(objectMapper.writeValueAsString(anyMap())).thenReturn(expectedJsonMessage);

        // Act
        rabbitMQSender.sendFilePathMessage(filePath);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend("documentExchange", "document.routingKey", expectedJsonMessage);
    }
}

