package br.com.card_game_api.controller;

import br.com.card_game_api.domain.GameHistory;
import br.com.card_game_api.dto.GameHistoryDTO;
import br.com.card_game_api.dto.GameRequestDTO;
import br.com.card_game_api.service.CardGameService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por expor os endpoints da API REST.
 */
@RestController
@RequestMapping("/game")
public class GameController {

    private final CardGameService cardGameService;
    private final ModelMapper modelMapper;

    public GameController(CardGameService cardGameService, ModelMapper modelMapper) {
        this.cardGameService = cardGameService;
        this.modelMapper = modelMapper;
    }

    /**
     * Endpoint para iniciar um novo jogo.
     *
     * @param gameRequestDTO Objeto que contém o número de jogadores (numPlayers) e o número de cartas por jogador (cardsPerHand).
     * @return Resposta contendo o histórico do jogo em formato DTO e o status HTTP 201 (Criado), caso o jogo seja iniciado com sucesso.
     */
    @PostMapping("/play")
    public ResponseEntity<GameHistoryDTO> playGame(@Valid @RequestBody GameRequestDTO gameRequestDTO) {
        int numPlayers = gameRequestDTO.getNumPlayers();
        int cardsPerHand = gameRequestDTO.getCardsPerHand();

        GameHistory gameHistory = cardGameService.playGame(numPlayers, cardsPerHand);

        GameHistoryDTO gameHistoryDTO = modelMapper.map(gameHistory, GameHistoryDTO.class);

        return new ResponseEntity<>(gameHistoryDTO, HttpStatus.CREATED);
    }

    /**
     * Endpoint para consultar o histórico de um jogo pelo ID.
     *
     * @param gameId ID do jogo
     * @return Resposta com o histórico do jogo ou mensagem de erro
     */
    @GetMapping("/history/{gameId}")
    public ResponseEntity<GameHistoryDTO> getGameHistory(@PathVariable Long gameId) {
        GameHistory gameHistory = cardGameService.getGameHistoryById(gameId);

        // Converte GameHistory para GameHistoryDTO
        GameHistoryDTO gameHistoryDTO = modelMapper.map(gameHistory, GameHistoryDTO.class);

        return new ResponseEntity<>(gameHistoryDTO, HttpStatus.OK);
    }

    /**
     * Endpoint para consultar o histórico de todos os jogos.
     *
     * @return Resposta com uma lista do histórico de jogos ou mensagem de erro, caso não haja jogos.
     */
    @GetMapping("/history")
    public ResponseEntity<List<GameHistoryDTO>> getAllGameHistories() {
        List<GameHistory> gameHistories = cardGameService.getAllGameHistories();

        List<GameHistoryDTO> gameHistoryDTOs = gameHistories.stream()
                .map(gameHistory -> modelMapper.map(gameHistory, GameHistoryDTO.class))
                .toList();

        if (gameHistoryDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(gameHistoryDTOs);
    }

}
