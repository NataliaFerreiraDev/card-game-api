package br.com.card_game_api.exception.handler;

import br.com.card_game_api.exception.DeckCreationException;
import br.com.card_game_api.exception.GameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controlador de exceções globais para tratamento de erros.
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * Tratamento para exceções relacionadas à criação de decks.
     */
    @ExceptionHandler(DeckCreationException.class)
    public ResponseEntity<Object> handleDeckCreationException(DeckCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Tratamento para exceções relacionadas à pesquisa de jogos inexistentes.
     */
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<Object> handleGameNotFoundException(GameNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
