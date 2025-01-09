package br.com.card_game_api.exception.handler;

import br.com.card_game_api.exception.DeckCreationException;
import br.com.card_game_api.exception.GameNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomExceptionHandlerTest {

    private final CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();

    @Test
    void handleDeckCreationException_ShouldReturn500() {
        // Arrange
        DeckCreationException exception = new DeckCreationException("Deck creation failed");

        // Act
        ResponseEntity<Object> response = customExceptionHandler.handleDeckCreationException(exception);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Deck creation failed", response.getBody());
    }

    @Test
    void handleGameNotFoundException_ShouldReturn404() {
        // Arrange
        GameNotFoundException exception = new GameNotFoundException("Game not found");

        // Act
        ResponseEntity<Object> response = customExceptionHandler.handleGameNotFoundException(exception);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Game not found", response.getBody());
    }

}
