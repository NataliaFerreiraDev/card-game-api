package br.com.card_game_api.service;

import br.com.card_game_api.adapter.outbound.DeckOfCardsClient;
import br.com.card_game_api.domain.GameHistory;
import br.com.card_game_api.domain.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardGameServiceTest {

    @Mock
    private DeckOfCardsClient deckOfCardsClient;

    @Mock
    private InputValidator inputValidator;

    @Mock
    private DeckCalculatorService deckCalculatorService;

    @Mock
    private CardDistributorService cardDistributorService;

    @Mock
    private GameResultService gameResultService;

    @Mock
    private GamePersistenceService gamePersistenceService;

    @Autowired
    @InjectMocks
    private CardGameService cardGameService;

    @Test
    void playGame_ShouldSaveGameHistoryAndReturnIt() {
        // Arrange
        int numPlayers = 2;
        int cardsPerHand = 5;
        String winner = "Player 1";
        String deckId = "deck123";
        List<Player> mockPlayers = List.of(
                new Player("Jogador 1", 10, "10 de Copas"),
                new Player("Jogador 2", 8, "8 de Espadas")
        );
        GameHistory mockGameHistory = new GameHistory();
        mockGameHistory.setNumberOfPlayers(numPlayers);
        mockGameHistory.setCardsPerPlayer(cardsPerHand);
        mockGameHistory.setDeckId(deckId);
        mockGameHistory.setWinner(winner);

        doNothing().when(inputValidator).validateInputs(numPlayers, cardsPerHand);
        when(deckCalculatorService.calculateDecks(numPlayers, cardsPerHand))
                .thenReturn(1);
        when(deckOfCardsClient.createDeck(anyInt()))
                .thenReturn(deckId);
        when(cardDistributorService.distributeCards(numPlayers, cardsPerHand, deckId))
                .thenReturn(mockPlayers);
        when(gameResultService.determineWinner(anyList()))
                .thenReturn("Player 1");

        when(gamePersistenceService.saveGameHistory(numPlayers, cardsPerHand, deckId, winner, mockPlayers))
                .thenReturn(mockGameHistory);

        // Act
        GameHistory result = cardGameService.playGame(numPlayers, cardsPerHand);

        // Assert
        assertNotNull(result);
        assertEquals(numPlayers, result.getNumberOfPlayers());
        assertEquals(cardsPerHand, result.getCardsPerPlayer());
        verify(deckCalculatorService, times(1)).calculateDecks(numPlayers, cardsPerHand);
    }

}
