package br.com.card_game_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    private InputValidator inputValidator;

    @BeforeEach
    void setUp() {
        inputValidator = new InputValidator();
    }

    @Test
    void validateInputs_ShouldThrowExceptionWhenNumPlayersIsLessThanOne() {
        // Arrange
        int numPlayers = 0;
        int cardsPerHand = 5;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateInputs(numPlayers, cardsPerHand));
        assertEquals("Número de jogadores ou cartas por mão inválido.", exception.getMessage());
    }

    @Test
    void validateInputs_ShouldThrowExceptionWhenCardsPerHandIsLessThanOne() {
        // Arrange
        int numPlayers = 2;
        int cardsPerHand = 0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateInputs(numPlayers, cardsPerHand));
        assertEquals("Número de jogadores ou cartas por mão inválido.", exception.getMessage());
    }

    @Test
    void validateInputs_ShouldNotThrowExceptionForValidInputs() {
        // Arrange
        int numPlayers = 2;
        int cardsPerHand = 5;

        // Act & Assert
        assertDoesNotThrow(() -> inputValidator.validateInputs(numPlayers, cardsPerHand));
    }
}