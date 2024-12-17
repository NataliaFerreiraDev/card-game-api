package br.com.card_game_api.service;

import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelo cálculo do número de baralhos necessários
 * com base no número de jogadores e cartas por jogador.
 */
@Service
public class DeckCalculatorService {

    /**
     * Calcula a quantidade de baralhos necessários com base no número de jogadores e cartas por jogador.
     *
     * @param numPlayers Número de jogadores
     * @param cardsPerHand Número de cartas por jogador
     * @return Número de baralhos necessários
     */
    public int calculateDecks(int numPlayers, int cardsPerHand) {
        return (int) Math.ceil((double) (numPlayers * cardsPerHand) / 52);
    }

}
