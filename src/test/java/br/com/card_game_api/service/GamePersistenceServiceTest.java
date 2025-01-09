package br.com.card_game_api.service;

import br.com.card_game_api.domain.GameHistory;
import br.com.card_game_api.domain.Player;
import br.com.card_game_api.exception.GameNotFoundException;
import br.com.card_game_api.repository.GameHistoryRepository;
import br.com.card_game_api.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GamePersistenceServiceTest {

    @Mock
    private GameHistoryRepository gameHistoryRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private GamePersistenceService gamePersistenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveGameHistory_ShouldPersistGameHistoryAndPlayers_WhenValidDataIsProvided() {
        // Arrange
        int numPlayers = 2;
        int cardsPerHand = 5;
        String deckId = "deck123";
        String winner = "Player 1";

        List<Player> mockPlayers = List.of(
                new Player("Jogador 1", 10, "10 de Copas"),
                new Player("Jogador 2", 8, "8 de Espadas")
        );

        GameHistory gameHistory = new GameHistory(
                numPlayers, cardsPerHand, deckId, winner, 10, LocalDateTime.now()
        );

        when(gameHistoryRepository.save(any(GameHistory.class))).thenReturn(gameHistory);

        // Act
        GameHistory savedGameHistory = gamePersistenceService.saveGameHistory(
                numPlayers, cardsPerHand, deckId, winner, mockPlayers
        );

        // Assert
        assertNotNull(savedGameHistory);
        assertEquals(numPlayers, savedGameHistory.getNumberOfPlayers());
        assertEquals(cardsPerHand, savedGameHistory.getCardsPerPlayer());
        assertEquals(deckId, savedGameHistory.getDeckId());
        assertEquals(winner, savedGameHistory.getWinner());

        verify(gameHistoryRepository).save(any(GameHistory.class));
        verify(playerRepository).saveAll(mockPlayers);
    }

    @Test
    void getGameHistoryById_ShouldReturnGameHistoryWhenFound() {
        // Arrange
        Long gameId = 1L;
        GameHistory gameHistory = new GameHistory();
        when(gameHistoryRepository.findById(gameId)).thenReturn(Optional.of(gameHistory));

        // Act
        GameHistory result = gamePersistenceService.getGameHistoryById(gameId);

        // Assert
        assertNotNull(result);
        assertEquals(gameHistory, result);
    }

    @Test
    void getGameHistoryById_ShouldThrowExceptionWhenNotFound() {
        // Arrange
        Long gameId = 1L;
        when(gameHistoryRepository.findById(gameId)).thenReturn(Optional.empty());

        // Act & Assert
        GameNotFoundException exception = assertThrows(GameNotFoundException.class,
                () -> gamePersistenceService.getGameHistoryById(gameId));
        assertEquals("Jogo com ID " + gameId + " não encontrado.", exception.getMessage());
    }

    @Test
    void getAllGameHistory_ShouldReturnGamesHistoriesWhenFound() {
        // Arrange
        List<GameHistory> gameHistory = List.of(new GameHistory());
        when(gameHistoryRepository.findAll()).thenReturn(gameHistory);

        // Act
        List<GameHistory> result = gamePersistenceService.getAllGameHistories();

        // Assert
        assertNotNull(result);
        assertEquals(gameHistory, result);
    }

}
