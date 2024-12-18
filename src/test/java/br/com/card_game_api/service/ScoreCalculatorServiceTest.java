package br.com.card_game_api.service;

import br.com.card_game_api.dto.CardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreCalculatorServiceTest {

    private ScoreCalculatorService scoreCalculatorService;

    @BeforeEach
    void setUp() {
        scoreCalculatorService = new ScoreCalculatorService();
    }

    @Test
    void calculateScore_ShouldReturnCorrectScoreForValidCards() {
        // Arrange
        List<CardDTO> cards = List.of(
                new CardDTO("ACE", "S"),  // Ás, valor 1
                new CardDTO("KING", "H"),  // Rei, valor 13
                new CardDTO("QUEEN", "C"),  // Dama, valor 12
                new CardDTO("JACK", "D"),  // Valete, valor 11
                new CardDTO("10", "H")  // 10, valor 10
        );

        // Act
        int score = scoreCalculatorService.calculateScore(cards);

        // Assert
        assertEquals(47, score);  // 1 + 13 + 12 + 11 + 10 = 47
    }

    @Test
    void calculateScore_ShouldReturnZeroForEmptyCardList() {
        // Arrange
        List<CardDTO> cards = List.of();

        // Act
        int score = scoreCalculatorService.calculateScore(cards);

        // Assert
        assertEquals(0, score);
    }

    @Test
    void calculateScore_ShouldHandleOnlyNumericCards() {
        // Arrange
        List<CardDTO> cards = List.of(
                new CardDTO("2", "S"),  // 2, valor 2
                new CardDTO("5", "D"),  // 5, valor 5
                new CardDTO("9", "H")   // 9, valor 9
        );

        // Act
        int score = scoreCalculatorService.calculateScore(cards);

        // Assert
        assertEquals(16, score);  // 2 + 5 + 9 = 16
    }

}
