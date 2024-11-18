package br.com.card_game_api.service;

import br.com.card_game_api.adapter.outbound.DeckOfCardsClient;
import br.com.card_game_api.domain.GameHistory;
import br.com.card_game_api.domain.Player;
import br.com.card_game_api.dto.CardDTO;
import br.com.card_game_api.repository.GameHistoryRepository;
import br.com.card_game_api.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public CardGameService(DeckOfCardsClient deckOfCardsClient, GameHistoryRepository gameHistoryRepository, PlayerRepository playerRepository) {
        this.deckOfCardsClient = deckOfCardsClient;
        this.gameHistoryRepository = gameHistoryRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Executa a lógica do jogo, distribuindo cartas para os jogadores, calculando pontuações
     * e determinando o(s) vencedor(es).
     *
     * @param numPlayers   Número de jogadores
     * @param cardsPerHand Número de cartas por jogador
     * @return Histórico do jogo registrado
     */
    public GameHistory playGame(int numPlayers, int cardsPerHand) {
        validateInputs(numPlayers, cardsPerHand);

        int requiredDecks = calculateDecks(numPlayers, cardsPerHand);
        String deckId = deckOfCardsClient.createDeck(requiredDecks);

        List<Player> players = distributeCards(numPlayers, cardsPerHand, deckId);

        String winner = determineWinner(players);

        return saveGameHistory(numPlayers, cardsPerHand, deckId, winner, players);
    }

    /**
     * Valida os parâmetros fornecidos para o número de jogadores e cartas por jogador.
     *
     * @param numPlayers Número de jogadores
     * @param cardsPerHand Número de cartas por jogador
     */
    private void validateInputs(int numPlayers, int cardsPerHand) {
        if (numPlayers <= 0) {
            throw new IllegalArgumentException("O número de jogadores deve ser maior que zero.");
        }
        if (cardsPerHand <= 0) {
            throw new IllegalArgumentException("O número de cartas por jogador deve ser maior que zero.");
        }
    }

    /**
     * Calcula a quantidade de baralhos necessários com base no número de jogadores e cartas por jogador.
     *
     * @param numPlayers Número de jogadores
     * @param cardsPerHand Número de cartas por jogador
     * @return Número de baralhos necessários
     */
    private int calculateDecks(int numPlayers, int cardsPerHand) {
        return (int) Math.ceil((double) (numPlayers * cardsPerHand) / 52);
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

            String handString = cardDTOs.stream()
                    .map(card -> card.getValue() + " of " + card.getSuit())
                    .collect(Collectors.joining(", "));

            Player player = new Player("Jogador " + i, score, handString);
            playerRepository.save(player); // Salva o jogador no banco

            players.add(player);
        }

        return players;
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
        Map<String, Integer> cardValues = Map.of(
                "A", 1, "K", 13, "Q", 12, "J", 11
        );

        return cardDTOs.stream()
                .mapToInt(card -> {
                    String value = card.getValue();
                    return cardValues.getOrDefault(value, Integer.parseInt(value));
                })
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
        gameHistoryRepository.save(gameHistory);

        return gameHistory;
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
