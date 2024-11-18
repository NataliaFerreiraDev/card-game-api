package br.com.card_game_api.controller;

import br.com.card_game_api.domain.GameHistory;
import br.com.card_game_api.dto.GameHistoryDTO;
import br.com.card_game_api.dto.GameRequestDTO;
import br.com.card_game_api.service.CardGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameControllerTest {

    @Mock
    private CardGameService cardGameService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void playGame_ShouldReturnGameHistoryDTO_WhenGameIsPlayedSuccessfully() {
        // Arrange
        int numPlayers = 2;
        int cardsPerHand = 5;

        GameRequestDTO gameRequestDTO = new GameRequestDTO();
        gameRequestDTO.setCardsPerHand(cardsPerHand);
        gameRequestDTO.setNumPlayers(numPlayers);

        GameHistory gameHistory = new GameHistory();
        gameHistory.setId(1L);
        // Defina o estado do jogo conforme necessário

        GameHistoryDTO gameHistoryDTO = new GameHistoryDTO();
        gameHistoryDTO.setId(1L);
        // Defina o estado do DTO conforme necessário

        // Mock para o serviço de jogar o jogo
        when(cardGameService.playGame(numPlayers, cardsPerHand)).thenReturn(gameHistory);

        // Mock do ModelMapper para converter o GameHistory para GameHistoryDTO
        when(modelMapper.map(gameHistory, GameHistoryDTO.class)).thenReturn(gameHistoryDTO);

        // Act
        ResponseEntity<GameHistoryDTO> response = gameController.playGame(gameRequestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Verifica se o status HTTP é 201 (Criado)
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser null"); // Verifica se o corpo não é null
        assertEquals(gameHistoryDTO, response.getBody()); // Verifica se o corpo da resposta contém o GameHistoryDTO esperado
        verify(cardGameService).playGame(numPlayers, cardsPerHand); // Verifica se o serviço foi chamado com os parâmetros corretos
        verify(modelMapper).map(gameHistory, GameHistoryDTO.class); // Verifica se o mapeamento foi chamado
    }

    @Test
    void getGameHistory_ShouldReturnGameHistoryDTO_WhenGameIdIsValid() {
        // Arrange
        Long gameId = 1L;

        GameHistory gameHistory = new GameHistory();
        gameHistory.setId(gameId);
        // Defina o estado do jogo conforme necessário

        GameHistoryDTO gameHistoryDTO = new GameHistoryDTO();
        gameHistoryDTO.setId(gameId);
        // Defina o estado do DTO conforme necessário

        // Mock para o serviço de buscar o histórico do jogo
        when(cardGameService.getGameHistoryById(gameId)).thenReturn(gameHistory);

        // Mock do ModelMapper para converter o GameHistory para GameHistoryDTO
        when(modelMapper.map(gameHistory, GameHistoryDTO.class)).thenReturn(gameHistoryDTO);

        // Act
        ResponseEntity<GameHistoryDTO> response = gameController.getGameHistory(gameId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Verifica se o status HTTP é 200 (OK)
        assertEquals(gameHistoryDTO, response.getBody()); // Verifica se o corpo da resposta contém o GameHistoryDTO esperado
        verify(cardGameService).getGameHistoryById(gameId); // Verifica se o serviço foi chamado com o gameId correto
    }

    @Test
    void getAllGameHistories_ShouldReturnListOfGameHistoryDTO_WhenHistoriesExist() {
        // Arrange
        GameHistory gameHistory1 = new GameHistory();
        gameHistory1.setId(1L);
        // Defina o estado do jogo conforme necessário

        GameHistory gameHistory2 = new GameHistory();
        gameHistory2.setId(2L);
        // Defina o estado do jogo conforme necessário

        GameHistoryDTO gameHistoryDTO1 = new GameHistoryDTO();
        gameHistoryDTO1.setId(1L);
        // Defina o estado do DTO conforme necessário

        GameHistoryDTO gameHistoryDTO2 = new GameHistoryDTO();
        gameHistoryDTO2.setId(2L);
        // Defina o estado do DTO conforme necessário

        List<GameHistory> gameHistories = List.of(gameHistory1, gameHistory2);
        List<GameHistoryDTO> gameHistoryDTOs = List.of(gameHistoryDTO1, gameHistoryDTO2);

        // Mock para o serviço de buscar todos os históricos de jogo
        when(cardGameService.getAllGameHistories()).thenReturn(gameHistories);

        // Mock do ModelMapper para converter os GameHistories para GameHistoryDTOs
        when(modelMapper.map(gameHistory1, GameHistoryDTO.class)).thenReturn(gameHistoryDTO1);
        when(modelMapper.map(gameHistory2, GameHistoryDTO.class)).thenReturn(gameHistoryDTO2);

        // Act
        ResponseEntity<List<GameHistoryDTO>> response = gameController.getAllGameHistories();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Verifica se o status HTTP é 200 (OK)
        assertEquals(gameHistoryDTOs, response.getBody()); // Verifica se o corpo da resposta contém a lista de GameHistoryDTOs esperada
        verify(cardGameService).getAllGameHistories(); // Verifica se o serviço foi chamado para buscar todos os históricos
    }

}
