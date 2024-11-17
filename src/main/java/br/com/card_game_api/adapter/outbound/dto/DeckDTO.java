package br.com.card_game_api.adapter.outbound.dto;

import br.com.card_game_api.dto.CardDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO para representar a resposta da API externa quando criamos um deck ou distribuímos cartas.
 */
public class DeckDTO {

    @JsonProperty("deck_id")
    private String deckId;

    @JsonProperty("remaining")
    private int remaining;

    @JsonProperty("cards")
    private List<CardDTO> cards;

    // Getters e Setters
    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

    public void setCards(List<CardDTO> cards) {
        this.cards = cards;
    }

}
