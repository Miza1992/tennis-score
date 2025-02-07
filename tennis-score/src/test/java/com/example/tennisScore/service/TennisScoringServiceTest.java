package com.example.tennisScore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TennisScoringServiceTest {

    private TennisScoringService scoringService;

    @BeforeEach
    void setUp() {
        scoringService = new TennisScoringService();
    }

    @Test
    void testSimpleWinForPlayerA() {
        String input = "AAAA";
        List<String> result = scoringService.computeScore(input);

        assertEquals(4, result.size()); // 4 évolutions de score + message final
        assertEquals("Player A: 15 / Player B: 0", result.get(0));
        assertEquals("Player A: 30 / Player B: 0", result.get(1));
        assertEquals("Player A: 40 / Player B: 0", result.get(2));
        assertEquals("Player A wins the game", result.get(3));
    }
}
