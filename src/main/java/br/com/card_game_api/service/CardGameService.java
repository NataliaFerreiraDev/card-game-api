package br.com.card_game_api.service;

import br.com.card_game_api.adapter.outbound.DeckOfCardsClient;
import br.com.card_game_api.domain.GameHistory;
import br.com.card_game_api.domain.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Classe de serviço responsável pela lógica de negócio do jogo.
 * Gerencia funcionalidades principais como integração com a API externa, início do jogo, cálculo de vencedores e tratamento de empates.
 */
@Service
public class CardGameService {

    private final DeckOfCardsClient deckOfCardsClient;

    private final InputValidator inputValidator;

    private final DeckCalculatorService deckCalculatorService;

    private final CardDistributorService cardDistributorService;

    private final GameResultService gameResultService;

    private final GamePersistenceService gamePersistenceService;

    public CardGameService(DeckOfCardsClient deckOfCardsClient,
                           InputValidator inputValidator,
                           DeckCalculatorService deckCalculatorService,
                           CardDistributorService cardDistributorService,
                           GameResultService gameResultService,
                           GamePersistenceService gamePersistenceService) {
        this.deckOfCardsClient = deckOfCardsClient;
        this.inputValidator = inputValidator;
        this.deckCalculatorService = deckCalculatorService;
        this.cardDistributorService = cardDistributorService;
        this.gameResultService = gameResultService;
        this.gamePersistenceService = gamePersistenceService;
    }

    /**
     * Executa a lógica do jogo, distribuindo cartas para os jogadores, calculando pontuações
     * e determinando o(s) vencedor(es).
     *
     * @param numPlayers   Número de jogadores
     * @param cardsPerHand Número de cartas por jogador
     * @return Histórico do jogo registrado
     */
    @Transactional
    public GameHistory playGame(int numPlayers, int cardsPerHand) {
        inputValidator.validateInputs(numPlayers, cardsPerHand);

        int requiredDecks = deckCalculatorService.calculateDecks(numPlayers, cardsPerHand);
        String deckId = deckOfCardsClient.createDeck(requiredDecks);

        List<Player> players = cardDistributorService.distributeCards(numPlayers, cardsPerHand, deckId);

        String winner = gameResultService.determineWinner(players);

        return gamePersistenceService.saveGameHistory(numPlayers, cardsPerHand, deckId, winner, players);
    }

}
