package br.com.card_game_api.service;

import org.springframework.stereotype.Service;

/**
 * Classe responsável por validar os dados de entrada fornecidos pelos adaptadores de entrada.
 * Segue o princípio da responsabilidade única, isolando a lógica de validação.
 */
@Service
public class InputValidator {

    /**
     * Valida os parâmetros fornecidos para o número de jogadores e cartas por jogador.
     *
     * @param numPlayers Número de jogadores
     * @param cardsPerHand Número de cartas por jogador
     */
    public void validateInputs(int numPlayers, int cardsPerHand) {
        if (numPlayers < 1 || cardsPerHand < 1) {
            throw new IllegalArgumentException("Número de jogadores ou cartas por mão inválido.");
        }
    }

}
