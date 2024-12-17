package br.com.card_game_api.service;

import br.com.card_game_api.adapter.outbound.DeckOfCardsClient;
import br.com.card_game_api.domain.GameHistory;
import br.com.card_game_api.dto.CardDTO;
import br.com.card_game_api.repository.GameHistoryRepository;
import br.com.card_game_api.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardGameServiceTest {

    @Mock
    private DeckOfCardsClient deckOfCardsClient;

    @Mock
    private GameHistoryRepository gameHistoryRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private InputValidator inputValidator;

    @Autowired
    @InjectMocks
    private CardGameService cardGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void playGame_ShouldSaveGameHistoryAndReturnIt() {
        // Arrange
        int numPlayers = 2;
        int cardsPerHand = 5;
        String deckId = "deck123";

        doNothing().when(inputValidator).validateInputs(numPlayers, cardsPerHand);
        when(deckOfCardsClient.createDeck(anyInt())).thenReturn(deckId);
        when(deckOfCardsClient.dealCards(deckId, cardsPerHand))
                .thenReturn(List.of(new CardDTO("ACE", "HEARTS"),
                        new CardDTO("8", "SPADES")
                        ));

        when(gameHistoryRepository.save(any(GameHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        GameHistory result = cardGameService.playGame(numPlayers, cardsPerHand);

        // Assert
        assertNotNull(result);
        assertEquals(numPlayers, result.getNumberOfPlayers());
        assertEquals(cardsPerHand, result.getCardsPerPlayer());
        verify(playerRepository, times(1)).saveAll(anyList());
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