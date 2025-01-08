package br.com.card_game_api.service;

import br.com.card_game_api.adapter.outbound.DeckOfCardsClient;
import br.com.card_game_api.domain.GameHistory;
import br.com.card_game_api.domain.Player;
import br.com.card_game_api.repository.GameHistoryRepository;
import br.com.card_game_api.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe de serviço responsável pela lógica de negócio do jogo.
 * Gerencia funcionalidades principais como integração com a API externa, início do jogo, cálculo de vencedores e tratamento de empates.
 */
@Service
public class CardGameService {

    private final DeckOfCardsClient deckOfCardsClient;
    private final GameHistoryRepository gameHistoryRepository;
    private final PlayerRepository playerRepository;

    private final InputValidator inputValidator;

    private final DeckCalculatorService deckCalculatorService;

    private final CardDistributorService cardDistributorService;

    private final GameResultService gameResultService;

    public CardGameService(DeckOfCardsClient deckOfCardsClient, GameHistoryRepository gameHistoryRepository, PlayerRepository playerRepository, InputValidator inputValidator, DeckCalculatorService deckCalculatorService, CardDistributorService cardDistributorService, GameResultService gameResultService) {
        this.deckOfCardsClient = deckOfCardsClient;
        this.gameHistoryRepository = gameHistoryRepository;
        this.playerRepository = playerRepository;
        this.inputValidator = inputValidator;
        this.deckCalculatorService = deckCalculatorService;
        this.cardDistributorService = cardDistributorService;
        this.gameResultService = gameResultService;
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

        return saveGameHistory(numPlayers, cardsPerHand, deckId, winner, players);
    }

    /**
     * Salva o histórico do jogo no banco de dados.
     * Registra o número de jogadores, as cartas distribuídas, o vencedor e a data do jogo.
     *
     * @param numPlayers Número de jogadores
     * @param cardsPerHand Número de cartas por jogador
     * @param deckId Identificador do baralho utilizado
     * @param winner Nome do vencedor
     * @param players Lista de jogadores
     * @return O histórico do jogo registrado
     */
    private GameHistory saveGameHistory(int numPlayers, int cardsPerHand, String deckId, String winner, List<Player> players) {
        int highestScore = players.stream()
                .mapToInt(Player::getScore)
                .max()
                .orElse(0);

        GameHistory gameHistory = new GameHistory(
                numPlayers,
                cardsPerHand,
                deckId,
                winner,
                highestScore,
                LocalDateTime.now()
        );
        gameHistory = gameHistoryRepository.save(gameHistory);

        savePlayers(players, gameHistory);

        return gameHistory;
    }

    /**
     * Salva os jogadores no banco de dados, associando-os ao histórico do jogo.
     *
     * @param players Lista de jogadores com suas cartas distribuídas
     * @param gameHistory O histórico do jogo que deve ser associado aos jogadores
     */
    private void savePlayers(List<Player> players, GameHistory gameHistory) {
        players.forEach(player -> player.setGameHistory(gameHistory));

        playerRepository.saveAll(players);
    }

    /**
     * Recupera o histórico de um jogo pelo ID.
     *
     * @param gameId O ID do jogo
     * @return O histórico do jogo
     */
    public GameHistory getGameHistoryById(Long gameId) {
        return gameHistoryRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Jogo não encontrado com ID: " + gameId));
    }

    /**
     * Recupera todos os históricos de jogos registrados no banco de dados.
     *
     * @return Lista de todos os históricos de jogos
     */
    public List<GameHistory> getAllGameHistories() {
        return gameHistoryRepository.findAll();
    }

}
