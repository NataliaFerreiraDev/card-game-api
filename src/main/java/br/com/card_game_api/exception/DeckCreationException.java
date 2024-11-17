package br.com.card_game_api.exception;

/**
 * Exceção customizada para erros relacionados à criação de um deck.
 */
public class DeckCreationException extends RuntimeException {

    public DeckCreationException(String message) {
        super(message);
    }

    public DeckCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
