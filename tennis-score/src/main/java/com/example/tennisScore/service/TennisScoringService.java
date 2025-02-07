package com.example.tennisScore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TennisScoringService {

    private static final Logger logger = LoggerFactory.getLogger(TennisScoringService.class);

    public List<String> computeScore(String input) {
        List<String> scoreEvolution = new ArrayList<>();

        int pointsA = 0;
        int pointsB = 0;

        for (char c : input.toCharArray()) {
            if (!isValidInput(c)) {
                throw new IllegalArgumentException("Invalid character in input: " + c);
            }

            if (isGameOver(pointsA, pointsB)) {
                break; // Arrêter immédiatement si le jeu est déjà gagné
            }

            if (c == 'A') {
                pointsA++;
            } else {
                pointsB++;
            }

            // Vérifier si le jeu est terminé avant d'ajouter un score
            if (!isGameOver(pointsA, pointsB)) {
                scoreEvolution.add(buildScoreLine(pointsA, pointsB));
            } else {
                // Ajouter le gagnant immédiatement
                String winner = (pointsA > pointsB) ? "Player A" : "Player B";
                scoreEvolution.add(winner + " wins the game");
                break; // Sortir de la boucle car le jeu est terminé
            }
        }

        return scoreEvolution;
    }

    private boolean isValidInput(char c) {
        return c == 'A' || c == 'B';
    }

    private boolean isGameOver(int pointsA, int pointsB) {
        return (pointsA >= 4 || pointsB >= 4) && Math.abs(pointsA - pointsB) >= 2;
    }

    private String buildScoreLine(int pointsA, int pointsB) {
        if (pointsA >= 3 && pointsB >= 3) {
            if (pointsA == pointsB) return "Player A: Deuce / Player B: Deuce";
            if (pointsA == pointsB + 1) return "Player A: Advantage / Player B: 40";
            if (pointsB == pointsA + 1) return "Player A: 40 / Player B: Advantage";
        }
        return "Player A: " + convertPoints(pointsA) + " / Player B: " + convertPoints(pointsB);
    }

    private String convertPoints(int points) {
        return switch (points) {
            case 0 -> "0";
            case 1 -> "15";
            case 2 -> "30";
            case 3 -> "40";
            default -> "40"; // Pour éviter l'affichage incohérent
        };
    }
}
