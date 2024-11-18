package br.com.card_game_api.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO que representa uma carta do baralho.
 */
public class CardDTO {

    @NotNull
    private String value;

    @NotNull
    private String suit;

    public CardDTO() {}

    public CardDTO (String value, String suit) {
        this.value = value;
        this.suit = suit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

}
