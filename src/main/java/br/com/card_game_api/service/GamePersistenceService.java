package br.com.card_game_api.service;

import br.com.card_game_api.domain.GameHistory;
import br.com.card_game_api.domain.Player;
import br.com.card_game_api.exception.GameNotFoundException;
import br.com.card_game_api.repository.GameHistoryRepository;
import br.com.card_game_api.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável pela persistência de dados relacionados ao jogo.
 * Gerencia o salvamento do histórico de jogos e jogadores no banco de dados.
 */
@Service
public class GamePersistenceService {

    private final GameHistoryRepository gameHistoryRepository;
    private final PlayerRepository playerRepository;

    public GamePersistenceService(GameHistoryRepository gameHistoryRepository, PlayerRepository playerRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Salva o histórico do jogo no banco de dados, incluindo jogadores associados.
     *
     * @param numPlayers   Número de jogadores
     * @param cardsPerHand Número de cartas por jogador
     * @param deckId       Identificador do baralho utilizado
     * @param winner       Nome do vencedor
     * @param players      Lista de jogadores com suas pontuações
     * @return Histórico do jogo registrado
     */
    public GameHistory saveGameHistory(int numPlayers, int cardsPerHand, String deckId, String winner, List<Player> players) {
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
     * @param players     Lista de jogadores
     * @param gameHistory Histórico do jogo associado aos jogadores
     */
    private void savePlayers(List<Player> players, GameHistory gameHistory) {
        players.forEach(player -> player.setGameHistory(gameHistory));
        playerRepository.saveAll(players);
    }

    /**
     * Recupera o histórico de um jogo pelo ID.
     *
     * @param gameId O ID do jogo
     * @return O histórico do jogo registrado
     * @throws IllegalArgumentException Se o jogo não for encontrado
     */
    public GameHistory getGameHistoryById(Long gameId) {
        return gameHistoryRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Jogo com ID " + gameId + " não encontrado."));
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
