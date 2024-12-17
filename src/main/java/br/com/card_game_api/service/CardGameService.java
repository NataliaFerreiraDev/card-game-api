package br.com.card_game_api.service;

import br.com.card_game_api.adapter.outbound.DeckOfCardsClient;
import br.com.card_game_api.domain.*;
import br.com.card_game_api.dto.CardDTO;
import br.com.card_game_api.repository.GameHistoryRepository;
import br.com.card_game_api.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public CardGameService(DeckOfCardsClient deckOfCardsClient, GameHistoryRepository gameHistoryRepository, PlayerRepository playerRepository, InputValidator inputValidator, DeckCalculatorService deckCalculatorService) {
        this.deckOfCardsClient = deckOfCardsClient;
        this.gameHistoryRepository = gameHistoryRepository;
        this.playerRepository = playerRepository;
        this.inputValidator = inputValidator;
        this.deckCalculatorService = deckCalculatorService;
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

        List<Player> players = distributeCards(numPlayers, cardsPerHand, deckId);

        String winner = determineWinner(players);

        return saveGameHistory(numPlayers, cardsPerHand, deckId, winner, players);
    }

    /**
     * Distribui as cartas para os jogadores e as armazena na lista de cada jogador.
     *
     * @param numPlayers Número de jogadores
     * @param cardsPerHand Número de cartas por jogador
     * @param deckId Identificador do baralho
     * @return Lista de jogadores com suas cartas distribuídas
     */
    private List<Player> distributeCards(int numPlayers, int cardsPerHand, String deckId) {
        List<Player> players = new ArrayList<>();

        for (int i = 1; i <= numPlayers; i++) {
            List<CardDTO> cardDTOs = deckOfCardsClient.dealCards(deckId, cardsPerHand);

            int score = calculateScore(cardDTOs);

            String handString = buildHandString(cardDTOs);

            Player player = new Player("Jogador " + i, score, handString);
            players.add(player);
        }

        return players;
    }

    /**
     * Constrói a string representando a mão do jogador com cartas traduzidas.
     *
     * @param cardDTOs Lista de cartas do jogador
     * @return String com as cartas traduzidas
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

    /**
     * Calcula a pontuação de um jogador com base nas cartas distribuídas.
     * Cartas com valor A (Ás) são tratadas como 1, K (Rei) como 13, Q (Dama) como 12, J (Valete) como 11,
     * e outros valores numéricos são convertidos diretamente.
     *
     * @param cardDTOs Lista de cartas do jogador
     * @return A pontuação do jogador
     */
    private int calculateScore(List<CardDTO> cardDTOs) {
        return cardDTOs.stream()
                .mapToInt(CardValue::getScoreFromCardDTO)
                .sum();
    }

    /**
     * Determina o vencedor do jogo com base nas pontuações dos jogadores.
     * Em caso de empate, todos os jogadores com a maior pontuação são considerados vencedores.
     *
     * @param players Lista de jogadores
     * @return O nome do vencedor ou vencedores (em caso de empate)
     */
    private String determineWinner(List<Player> players) {
        int highestScore = players.stream().mapToInt(Player::getScore).max().orElse(0);

        return players.stream()
                .filter(player -> player.getScore() == highestScore)
                .map(Player::getIdentifier)
                .collect(Collectors.joining(", "));
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
