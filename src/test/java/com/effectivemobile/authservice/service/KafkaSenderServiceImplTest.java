package com.effectivemobile.authservice.service;

import com.effectivemobile.authservice.AbstractContainerTest;
import com.effectivemobile.authservice.service.kafka.KafkaSenderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaSenderServiceImplTest extends AbstractContainerTest {

    private static final String EMAIL_TOPIC = "email-topic";
    private static final String TOKEN_TOPIC = "token-topic";

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaSenderServiceImpl kafkaSenderService;

    @Captor
    private ArgumentCaptor<Message<Object>> messageCaptor;

    @BeforeEach
    void setUp() {
        kafkaSenderService = new KafkaSenderServiceImpl(kafkaTemplate);

        // Устанавливаем значения для @Value полей вручную
        kafkaSenderService.setSendObjectEmailAddressTopicName(EMAIL_TOPIC);
        kafkaSenderService.setSendObjectTokenTopicName(TOKEN_TOPIC);
    }

    @Test
    void sendToTopic_shouldSendMessageToCorrectTopic() {
        // Arrange
        String message = "Test Email Message";

        // Act
        kafkaSenderService.sendToTopic(EMAIL_TOPIC, message);

        // Assert
        verify(kafkaTemplate, times(1)).send(messageCaptor.capture());
        Message<Object> sentMessage = messageCaptor.getValue();

        assertEquals(EMAIL_TOPIC, sentMessage.getHeaders().get("kafka_topic"));
        assertEquals(message, sentMessage.getPayload());
    }
}