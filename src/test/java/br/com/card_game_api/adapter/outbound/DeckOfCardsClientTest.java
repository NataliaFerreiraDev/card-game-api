package br.com.card_game_api.adapter.outbound;

import br.com.card_game_api.adapter.outbound.dto.DeckDTO;
import br.com.card_game_api.dto.CardDTO;
import br.com.card_game_api.exception.DeckCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeckOfCardsClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    private DeckOfCardsClient deckOfCardsClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDeck_ShouldReturnDeckIdWhenCreateDeckIsSuccessful() {
        // Arrange
        String apiBaseUrl = "https://api.deckofcards.com/";
        int numberOfDecks = 1;
        String deckId = "12345";

        // Criação de um DeckDTO simulado com o deckId preenchido
        DeckDTO deckDTO = new DeckDTO();
        deckDTO.setDeckId(deckId);
        deckDTO.setRemaining(3);
        deckDTO.setCards(List.of(new CardDTO("ACE", "HEARTS"),
                new CardDTO("8", "SPADES")));

        // Configura o mock para o RestTemplate
        DeckOfCardsClient deckOfCardsClient = new DeckOfCardsClient(restTemplate);
        ReflectionTestUtils.setField(deckOfCardsClient, "apiBaseUrl", apiBaseUrl); // Mock do valor da URL base

        // Cria a URL de maneira similar ao método real
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + "new/shuffle/")
                .queryParam("deck_count", numberOfDecks)
                .toUriString();

        // Mock do RestTemplate para retornar o deckDTO quando chamado
        when(restTemplate.getForObject(eq(url), eq(DeckDTO.class))).thenReturn(deckDTO);

        // Act
        String result = deckOfCardsClient.createDeck(numberOfDecks);

        // Assert
        assertEquals(deckId, result); // Verifica se o deckId retornado é o esperado
        verify(restTemplate).getForObject(eq(url), eq(DeckDTO.class)); // Verifica se o RestTemplate foi chamado com a URL correta
    }

    @Test
    void createDeck_ShouldThrowDeckCreationExceptionWhenHttpClientErrorExceptionOccurs() {
        // Arrange
        int numberOfDecks = 1;
        String apiBaseUrl = "https://api.deckofcards.com/";
        String url = apiBaseUrl + "new/shuffle/";

        // Configura o mock para o RestTemplate
        DeckOfCardsClient deckOfCardsClient = new DeckOfCardsClient(restTemplate);
        // Simula o valor da URL no @Value (mocking da variável da URL)
        ReflectionTestUtils.setField(deckOfCardsClient, "apiBaseUrl", apiBaseUrl);

        // Simula a exceção HttpClientErrorException para a URL inválida
        when(restTemplate.getForObject(eq(url), eq(DeckDTO.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Act & Assert
        DeckCreationException exception = assertThrows(DeckCreationException.class, () -> {
            deckOfCardsClient.createDeck(numberOfDecks);
        });

        // Verifica se a mensagem da exceção lançada é a esperada
        assertEquals("Erro inesperado ao tentar criar o deck.", exception.getMessage());
    }

    @Test
    void createDeck_ShouldThrowIllegalArgumentExceptionWhenNumberOfDeckIsZero() {
        // Arrange
        int numberOfDecks = 0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            deckOfCardsClient.createDeck(numberOfDecks);
        });
        assertEquals("O número de decks deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void dealCards_ShouldReturnCardsWhenDealIsSuccessful() {
        // Arrange
        String deckId = "12345";
        int numberOfCards = 3;

        // Criação de uma lista simulada de cartas
        List<CardDTO> cards = List.of(
                new CardDTO("ACE", "HEARTS"),
                new CardDTO("8", "SPADES"),
                new CardDTO("KING", "DIAMONDS")
        );

        // Criação de um DeckDTO simulado
        DeckDTO deckDTO = new DeckDTO();
        deckDTO.setCards(cards);

        // Configura o mock para o RestTemplate
        String apiBaseUrl = "https://api.deckofcards.com/";
        DeckOfCardsClient deckOfCardsClient = new DeckOfCardsClient(restTemplate);
        ReflectionTestUtils.setField(deckOfCardsClient, "apiBaseUrl", apiBaseUrl); // Mock do valor da URL base

        // Cria a URL de maneira similar ao método real
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + "{deck_id}/draw/")
                .queryParam("count", numberOfCards)
                .buildAndExpand(deckId)
                .toUriString();

        // Mock do RestTemplate para retornar o deckDTO com as cartas
        when(restTemplate.getForObject(eq(url), eq(DeckDTO.class))).thenReturn(deckDTO);

        // Act
        List<CardDTO> result = deckOfCardsClient.dealCards(deckId, numberOfCards);

        // Assert
        assertEquals(3, result.size()); // Verifica se o número de cartas distribuídas é o esperado
        assertEquals("ACE", result.get(0).getValue()); // Verifica se a primeira carta é "ACE"
        verify(restTemplate).getForObject(eq(url), eq(DeckDTO.class)); // Verifica se o RestTemplate foi chamado com a URL correta
    }

    @Test
    void dealCards_ShouldThrowIllegalArgumentExceptionWhenDeckIdIsNull() {
        // Arrange
        String deckId = null;
        int numberOfCards = 3;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            deckOfCardsClient.dealCards(deckId, numberOfCards);
        });
        assertEquals("O deckId não pode ser nulo ou vazio.", exception.getMessage());
    }

    @Test
    void dealCards_ShouldThrowIllegalArgumentExceptionWhenNumberOfCardsIsZero() {
        // Arrange
        String deckId = "12345";
        int numberOfCards = 0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            deckOfCardsClient.dealCards(deckId, numberOfCards);
        });
        assertEquals("O número de cartas deve ser maior que zero.", exception.getMessage());
    }

}
