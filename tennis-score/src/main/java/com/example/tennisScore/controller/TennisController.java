package com.example.tennisScore.controller;

import com.example.tennisScore.kafka.KafkaProducerService;
import com.example.tennisScore.service.TennisScoringService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TennisController {
    private final TennisScoringService scoringService;
    private final KafkaProducerService kafkaProducerService;

    public TennisController(TennisScoringService scoringService, KafkaProducerService kafkaProducerService) {
        this.scoringService = scoringService;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Endpoint GET qui calcule et renvoie l'évolution du score
     * en passant en paramètre la séquence des balles gagnées ("A" ou "B").
     * Ex : /compute-score?input=ABABAA
     */
    @GetMapping("/compute-score")
    public List<String> computeScore(@RequestParam String input) {
        return scoringService.computeScore(input);
    }

    @GetMapping("/send-message")
    public String sendMessage(@RequestParam String message) {
        kafkaProducerService.sendMessage(message,"receiveScore-in-0");
        return "Message envoyé sur Kafka: " + message;
    }
}
