package br.com.card_game_api.service;

import br.com.card_game_api.adapter.outbound.DeckOfCardsClient;
import br.com.card_game_api.domain.GameHistory;
import br.com.card_game_api.domain.Player;
import br.com.card_game_api.repository.GameHistoryRepository;
import br.com.card_game_api.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardGameServiceTest {

    @Mock
    private DeckOfCardsClient deckOfCardsClient;

    @Mock
    private GameHistoryRepository gameHistoryRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private InputValidator inputValidator;

    @Mock
    private DeckCalculatorService deckCalculatorService;

    @Mock
    private CardDistributorService cardDistributorService;

    @Autowired
    @InjectMocks
    private CardGameService cardGameService;

    @Test
    void playGame_ShouldSaveGameHistoryAndReturnIt() {
        // Arrange
        int numPlayers = 2;
        int cardsPerHand = 5;
        String deckId = "deck123";
        List<Player> mockPlayers = List.of(
                new Player("Jogador 1", 10, "10 de Copas"),
                new Player("Jogador 2", 8, "8 de Espadas")
        );

        doNothing().when(inputValidator).validateInputs(numPlayers, cardsPerHand);
        when(deckCalculatorService.calculateDecks(numPlayers, cardsPerHand)).thenReturn(1);
        when(deckOfCardsClient.createDeck(anyInt())).thenReturn(deckId);
        when(cardDistributorService.distributeCards(numPlayers, cardsPerHand, deckId))
                .thenReturn(mockPlayers);

        when(gameHistoryRepository.save(any(GameHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        GameHistory result = cardGameService.playGame(numPlayers, cardsPerHand);

        // Assert
        assertNotNull(result);
        assertEquals(numPlayers, result.getNumberOfPlayers());
        assertEquals(cardsPerHand, result.getCardsPerPlayer());
        verify(playerRepository, times(1)).saveAll(anyList());
        verify(deckCalculatorService, times(1)).calculateDecks(numPlayers, cardsPerHand);
    }

    @Test
    void getGameHistoryById_ShouldReturnGameHistoryWhenFound() {
        // Arrange
        Long gameId = 1L;
        GameHistory gameHistory = new GameHistory();
        when(gameHistoryRepository.findById(gameId)).thenReturn(Optional.of(gameHistory));

        // Act
        GameHistory result = cardGameService.getGameHistoryById(gameId);

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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> cardGameService.getGameHistoryById(gameId));
        assertEquals("Jogo não encontrado com ID: " + gameId, exception.getMessage());
    }

    @Test
    void getAllGameHistory_ShouldReturnGamesHistoriesWhenFound() {
        // Arrange
        List<GameHistory> gameHistory = List.of(new GameHistory());
        when(gameHistoryRepository.findAll()).thenReturn(gameHistory);

        // Act
        List<GameHistory> result = cardGameService.getAllGameHistories();

        // Assert
        assertNotNull(result);
        assertEquals(gameHistory, result);
    }

}
