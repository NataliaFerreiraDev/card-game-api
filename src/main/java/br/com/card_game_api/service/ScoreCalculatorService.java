package br.com.card_game_api.service;

import br.com.card_game_api.dto.CardDTO;
import br.com.card_game_api.domain.CardValue;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pelo cálculo de pontuações no jogo de cartas.
 */
@Service
public class ScoreCalculatorService {

    /**
     * Calcula a pontuação de um jogador com base nas cartas distribuídas.
     * Cartas com valor A (Ás) são tratadas como 1, K (Rei) como 13, Q (Dama) como 12, J (Valete) como 11,
     * e outros valores numéricos são convertidos diretamente.
     *
     * @param cardDTOs Lista de cartas do jogador
     * @return A pontuação do jogador
     */
    public int calculateScore(List<CardDTO> cardDTOs) {
        return cardDTOs.stream()
                .mapToInt(CardValue::getScoreFromCardDTO)
                .sum();
    }

}
