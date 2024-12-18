package br.com.card_game_api.service;

import br.com.card_game_api.adapter.outbound.DeckOfCardsClient;
import br.com.card_game_api.domain.Player;
import br.com.card_game_api.domain.TranslatedCardSuit;
import br.com.card_game_api.domain.TranslatedCardValue;
import br.com.card_game_api.dto.CardDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável por distribuir cartas para os jogadores e calcular suas pontuações.
 */
@Service
public class CardDistributorService {

    private final DeckOfCardsClient deckOfCardsClient;
    private final ScoreCalculatorService scoreCalculatorService;

    public CardDistributorService(DeckOfCardsClient deckOfCardsClient, ScoreCalculatorService scoreCalculatorService) {
        this.deckOfCardsClient = deckOfCardsClient;
        this.scoreCalculatorService = scoreCalculatorService;
    }

    /**
     * Distribui as cartas para os jogadores e retorna a lista de jogadores com suas mãos e pontuações.
     *
     * @param numPlayers Número de jogadores
     * @param cardsPerHand Número de cartas por jogador
     * @param deckId Identificador do baralho
     * @return Lista de jogadores com suas cartas distribuídas
     */
    public List<Player> distributeCards(int numPlayers, int cardsPerHand, String deckId) {
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            List<CardDTO> cardDTOs = deckOfCardsClient.dealCards(deckId, cardsPerHand);
            int score = scoreCalculatorService.calculateScore(cardDTOs);
            String handString = buildHandString(cardDTOs);
            players.add(new Player("Jogador " + i, score, handString));
        }
        return players;
    }

    /**
     * Constrói a string representando a mão do jogador com cartas traduzidas.
     *
     * @param cardDTOs Lista de cartas do jogador
     * @return String formatada com as cartas traduzidas.
     */
    private String buildHandString(List<CardDTO> cardDTOs) {
        return cardDTOs.stream()
                .map(card -> getTranslatedCardValue(card.getValue()) + " de " +
                        TranslatedCardSuit.fromString(card.getSuit()).getTranslatedSuit())
                .collect(Collectors.joining(", "));
    }

    /**
     * Obtém o valor traduzido da carta.
     * Se for um valor numérico, retorna o número como string.
     *
     * @param cardValue Valor da carta
     * @return Valor traduzido da carta
     */
    private String getTranslatedCardValue(String cardValue) {
        try {
            TranslatedCardValue value = TranslatedCardValue.fromString(cardValue);
            return value.getTranslatedValue();
        } catch (IllegalArgumentException e) {
            return cardValue;
        }
    }

}
