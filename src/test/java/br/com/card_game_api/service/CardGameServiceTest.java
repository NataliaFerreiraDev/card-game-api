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

        when(deckOfCardsClient.createDeck(anyInt())).thenReturn(deckId);
        when(deckOfCardsClient.dealCards(eq(deckId), eq(cardsPerHand)))
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
    void validateInputs_ShouldThrowExceptionWhenNumPlayersIsZero() {
        // Arrange
        int numPlayers = 0;
        int cardsPerHand = 5;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> cardGameService.playGame(numPlayers, cardsPerHand));
        assertEquals("Número de jogadores ou cartas por mão inválido.", exception.getMessage());
    }

    @Test
    void validateInputs_ShouldThrowExceptionWhenCardVaueIsInvalid() {
        // Arrange
        int numPlayers = 2;
        int cardsPerHand = 5;
        String deckId = "deck123";

        when(deckOfCardsClient.createDeck(anyInt())).thenReturn(deckId);
        when(deckOfCardsClient.dealCards(eq(deckId), eq(cardsPerHand)))
                .thenReturn(List.of(new CardDTO("ACES", "HEARTS")));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> cardGameService.playGame(numPlayers, cardsPerHand));
        assertEquals("Valor inválido para a carta: " + "ACES", exception.getMessage());
    }

    @Test
    void calculateScore_shouldThrowIllegalArgumentExceptionWhenInvalidValueIsProvided() {
        // Arrange
        int numPlayers = 2;
        int cardsPerHand = 0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> cardGameService.playGame(numPlayers, cardsPerHand));
        assertEquals("Número de jogadores ou cartas por mão inválido.", exception.getMessage());
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