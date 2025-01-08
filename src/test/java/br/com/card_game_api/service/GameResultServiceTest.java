package br.com.card_game_api.service;

import br.com.card_game_api.domain.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GameResultServiceTest {

    @InjectMocks
    private GameResultService gameResultService;

    @Test
    void determineWinner_ShouldReturnSingleWinnerWhenNoTie() {
        // Arrange
        List<Player> players = List.of(
                new Player("Player 1", 15, "ACE of Spades"),
                new Player("Player 2", 20, "KING of Hearts"),
                new Player("Player 3", 10, "2 of Clubs")
        );

        // Act
        String result = gameResultService.determineWinner(players);

        // Assert
        assertEquals("Player 2", result);
    }

    @Test
    void determineWinner_ShouldReturnMultipleWinnersInCaseOfTie() {
        // Arrange
        List<Player> players = List.of(
                new Player("Player 1", 20, "QUEEN of Diamonds"),
                new Player("Player 2", 20, "JACK of Hearts"),
                new Player("Player 3", 10, "7 of Spades")
        );

        // Act
        String result = gameResultService.determineWinner(players);

        // Assert
        assertEquals("Player 1, Player 2", result);
    }

    @Test
    void determineWinner_ShouldReturnEmptyStringWhenNoPlayers() {
        // Arrange
        List<Player> players = List.of();

        // Act
        String result = gameResultService.determineWinner(players);

        // Assert
        assertEquals("", result);
    }

    @Test
    void determineWinner_ShouldHandleSinglePlayerCorrectly() {
        // Arrange
        List<Player> players = List.of(
                new Player("Player 1", 30, "10 of Hearts")
        );

        // Act
        String result = gameResultService.determineWinner(players);

        // Assert
        assertEquals("Player 1", result);
    }

}
