package com.example.tennisScore.kafka;

import com.example.tennisScore.service.TennisScoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class KafkaConsumerServiceTest {

    @Mock
    private TennisScoringService scoringService;

    @Mock
    private KafkaProducerService kafkaProducerService;

    private KafkaConsumerService kafkaConsumerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kafkaConsumerService = new KafkaConsumerService(scoringService, kafkaProducerService);
    }

    @Test
    void testReceiveScore() {
        Consumer<String> consumer = kafkaConsumerService.receiveScore();

        String testMessage = "ABABAA";

        when(scoringService.computeScore(testMessage)).thenReturn(Arrays.asList(
                "Player A: 15 / Player B: 0",
                "Player A: 15 / Player B: 15",
                "Player A wins the game"
        ));

        consumer.accept(testMessage);

        verify(scoringService, times(1)).computeScore(testMessage);

        verify(kafkaProducerService, times(1)).sendMessage("Player A: 15 / Player B: 0", "sendScore-out-0");
        verify(kafkaProducerService, times(1)).sendMessage("Player A: 15 / Player B: 15", "sendScore-out-0");
        verify(kafkaProducerService, times(1)).sendMessage("Player A wins the game", "sendScore-out-0");
    }
}
