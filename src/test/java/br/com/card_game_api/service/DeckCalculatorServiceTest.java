package br.com.card_game_api.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DeckCalculatorServiceTest {

    @InjectMocks
    private DeckCalculatorService deckCalculatorService;

    @ParameterizedTest
    @CsvSource({
            "4, 10, 1",   // 4 jogadores * 10 cartas = 40, cabe em 1 baralho
            "6, 10, 2",   // 6 jogadores * 10 cartas = 60, precisa de 2 baralhos
            "0, 10, 0",   // 0 jogadores, deve retornar 0
            "4, 0, 0",    // 4 jogadores, 0 cartas por jogador, deve retornar 0
            "13, 4, 1"    // 13 jogadores * 4 cartas = 52, cabe exatamente em 1 baralho
    })
    void calculateDecks_ShouldReturnCorrectNumberOfDecks(int numPlayers, int cardsPerHand, int expectedDecks) {
        // Act
        int result = deckCalculatorService.calculateDecks(numPlayers, cardsPerHand);

        // Assert
        assertEquals(expectedDecks, result);
    }

}