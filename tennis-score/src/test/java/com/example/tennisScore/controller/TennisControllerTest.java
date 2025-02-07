package com.example.tennisScore.controller;

import com.example.tennisScore.kafka.KafkaProducerService;
import com.example.tennisScore.service.TennisScoringService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TennisController.class)
class TennisControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TennisScoringService scoringService;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private TennisController tennisController;

    @Test
    void testComputeScoreEndpoint() throws Exception {
        String input = "ABABAA";
        List<String> mockResult = List.of(
                "Player A: 15 / Player B: 0",
                "Player A: 15 / Player B: 15",
                "Player A: 30 / Player B: 15",
                "Player A: 30 / Player B: 30",
                "Player A: 40 / Player B: 30",
                "Player A wins the game"
        );

        Mockito.when(scoringService.computeScore(input)).thenReturn(mockResult);

        mockMvc.perform(get("/compute-score?input=ABABAA"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            "Player A: 15 / Player B: 0",
                            "Player A: 15 / Player B: 15",
                            "Player A: 30 / Player B: 15",
                            "Player A: 30 / Player B: 30",
                            "Player A: 40 / Player B: 30",
                            "Player A wins the game"
                        ]
                        """));
    }

    @Test
    void testInvalidCharacterException() throws Exception {
        Mockito.when(scoringService.computeScore("AX"))
                .thenThrow(new IllegalArgumentException("Invalid character in input: X"));

        mockMvc.perform(get("/compute-score?input=AX"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid character in input: X"));
    }

    @Test
    void testSendMessage() throws Exception {
        String message = "HelloKafka";
        String topic = "receiveScore-in-0";

        // Test de l'endpoint
        mockMvc.perform(get("/send-message").param("message", message))
                .andExpect(status().isOk())
                .andExpect(content().string("Message envoyé sur Kafka: " + message));

        // Vérification que le producteur Kafka a bien été appelé avec les bons paramètres
        verify(kafkaProducerService, times(1)).sendMessage(message, topic);
    }
}
