package br.com.card_game_api.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DeckCreationExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "Error creating deck";
        DeckCreationException exception = new DeckCreationException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionWithCause() {
        Throwable cause = new Throwable("Cause of error");
        DeckCreationException exception = new DeckCreationException("Error creating deck", cause);

        assertEquals("Error creating deck", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}