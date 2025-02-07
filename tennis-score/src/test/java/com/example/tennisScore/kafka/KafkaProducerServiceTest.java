package com.example.tennisScore.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.stream.function.StreamBridge;

import static org.mockito.Mockito.*;

class KafkaProducerServiceTest {

    @Mock
    private StreamBridge streamBridge;

    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kafkaProducerService = new KafkaProducerService(streamBridge);
    }

    @Test
    void testSendMessage() {
        String message = "HelloKafka";
        String bindingName = "sendScore-out-0";

        kafkaProducerService.sendMessage(message, bindingName);

        verify(streamBridge, times(1)).send(bindingName, message);
    }
}
