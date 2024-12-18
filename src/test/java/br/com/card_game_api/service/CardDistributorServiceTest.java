package br.com.card_game_api.service;

import br.com.card_game_api.adapter.outbound.DeckOfCardsClient;
import br.com.card_game_api.domain.Player;
import br.com.card_game_api.dto.CardDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardDistributorServiceTest {

    @Mock
    private DeckOfCardsClient deckOfCardsClient;

    @Mock
    private ScoreCalculatorService scoreCalculatorService;

    @InjectMocks
    private CardDistributorService cardDistributorService;

    @Test
    void distributeCards_ShouldDistributeCardsAndCalculateScores() {
        // Arrange
        int numPlayers = 2;
        int cardsPerHand = 5;
        String deckId = "deck123";
        List<CardDTO> mockCards = List.of(
                new CardDTO("ACE", "HEARTS"),
                new CardDTO("8", "SPADES")
        );
        when(deckOfCardsClient.dealCards(deckId, cardsPerHand)).thenReturn(mockCards);
        when(scoreCalculatorService.calculateScore(mockCards)).thenReturn(9);

        // Act
        List<Player> players = cardDistributorService.distributeCards(numPlayers, cardsPerHand, deckId);

        // Assert
        assertNotNull(players);
        assertEquals(numPlayers, players.size());
        for (int i = 0; i < numPlayers; i++) {
            Player player = players.get(i);
            assertEquals(9, player.getScore());
            assertTrue(player.getHand().contains("Ás de Copas") || player.getHand().contains("8 de Espadas"));
        }
    }

    @Test
    void distributeCards_ShouldHandleEmptyDeckGracefully() {
        // Arrange
        int numPlayers = 1;
        int cardsPerHand = 5;
        String deckId = "deck123";
        when(deckOfCardsClient.dealCards(deckId, cardsPerHand)).thenReturn(List.of());
        when(scoreCalculatorService.calculateScore(List.of())).thenReturn(0);

        // Act
        List<Player> players = cardDistributorService.distributeCards(numPlayers, cardsPerHand, deckId);

        // Assert
        assertNotNull(players);
        assertEquals(1, players.size());
        assertEquals(0, players.get(0).getScore());
        assertEquals("", players.get(0).getHand());
    }

}
