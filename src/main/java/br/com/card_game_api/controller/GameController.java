package br.com.card_game_api.controller;

import br.com.card_game_api.domain.GameHistory;
import br.com.card_game_api.dto.GameHistoryDTO;
import br.com.card_game_api.service.CardGameService;
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
     * @param numPlayers   Número de jogadores
     * @param cardsPerHand Número de cartas por jogador
     * @return Resposta com o histórico do jogo e status HTTP
     */
    @PostMapping("/play")
    public ResponseEntity<GameHistoryDTO> playGame(@RequestParam int numPlayers, @RequestParam int cardsPerHand) {
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

        return new ResponseEntity<>(gameHistoryDTOs, HttpStatus.OK);
    }

}
