package br.com.card_game_api.domain;

import br.com.card_game_api.dto.CardDTO;

public enum CardValue {
    ACE("ACE", 1),
    KING("KING", 13),
    QUEEN("QUEEN", 12),
    JACK("JACK",11);

    private final String cardSymbol;
    private final int score;

    CardValue(String cardSymbol, int score) {
        this.cardSymbol = cardSymbol;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getCardSymbol() {
        return cardSymbol;
    }

    // Renomeado para evitar ambiguidade
    public static int getScoreFromCardDTO(CardDTO card) {
        for (CardValue cardValue : CardValue.values()) {
            if (cardValue.getCardSymbol().equalsIgnoreCase(card.getValue())) {
                return cardValue.getScore();
            }
        }
        // Se não for especial, assume que o valor já é numérico
        try {
            return Integer.parseInt(card.getValue());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido para a carta: " + card.getValue());
        }
    }
}