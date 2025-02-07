package com.example.tennisScore.kafka;

import com.example.tennisScore.service.TennisScoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final TennisScoringService scoringService;
    private final KafkaProducerService kafkaProducerService;

    public KafkaConsumerService(TennisScoringService scoringService, KafkaProducerService kafkaProducerService) {
        this.scoringService = scoringService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Bean
    public Consumer<String> receiveScore() {
        return message -> {
            logger.info("Received score from Kafka: {}", message);
            scoringService.computeScore(message).forEach(m -> kafkaProducerService.sendMessage(m, "sendScore-out-0"));

        };
    }
}
