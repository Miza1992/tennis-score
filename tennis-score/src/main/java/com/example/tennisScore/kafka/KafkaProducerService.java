package com.example.tennisScore.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final StreamBridge streamBridge;

    public KafkaProducerService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendMessage(String message,String bindingName) {
        logger.info("Sending message to Kafka: {}", message);
        streamBridge.send(bindingName, message);
    }
}
