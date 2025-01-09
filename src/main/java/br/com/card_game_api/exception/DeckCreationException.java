package br.com.card_game_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção customizada para erros relacionados à criação de um deck.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeckCreationException extends RuntimeException {

    public DeckCreationException(String message) {
        super(message);
    }

    public DeckCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
