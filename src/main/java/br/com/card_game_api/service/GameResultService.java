package br.com.card_game_api.service;

import br.com.card_game_api.domain.Player;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe responsável por calcular o resultado do jogo e determinar o(s) vencedor(es).
 */
@Service
public class GameResultService {

    /**
     * Determina o vencedor do jogo com base nas pontuações dos jogadores.
     * Em caso de empate, todos os jogadores com a maior pontuação são considerados vencedores.
     *
     * @param players Lista de jogadores
     * @return O nome do vencedor ou vencedores (em caso de empate)
     */
    public String determineWinner(List<Player> players) {
        int highestScore = players.stream()
                .mapToInt(Player::getScore)
                .max()
                .orElse(0);

        return players.stream()
                .filter(player -> player.getScore() == highestScore)
                .map(Player::getIdentifier)
                .collect(Collectors.joining(", "));
    }

}
