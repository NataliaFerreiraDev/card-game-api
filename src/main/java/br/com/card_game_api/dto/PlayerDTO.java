package br.com.card_game_api.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO que representa um jogador no jogo de cartas.
 */
public class PlayerDTO {

    @NotNull
    private Long id;

    @NotNull
    private String identifier;

    @NotNull
    private int score;

    @NotNull
    private String hand;

    public PlayerDTO() {
    }

    public PlayerDTO(Long id, String identifier, int score, String hand) {
        this.id = id;
        this.identifier = identifier;
        this.score = score;
        this.hand = hand;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getHand() {
        return hand;
    }

    public void setHand(String hand) {
        this.hand = hand;
    }

}
