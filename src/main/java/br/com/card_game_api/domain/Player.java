package br.com.card_game_api.domain;

import jakarta.persistence.*;

/**
 * Representa um jogador no jogo de cartas.
 * Cada jogador possui um identificador e uma pontuação.
 */
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String identifier; // Identificador do jogador (Jogador 1, Jogador 2, etc.)

    @Column(nullable = false)
    private int score; // Pontuação do jogador

    public Player() {}

    public Player(String identifier, int score) {
        this.identifier = identifier;
        this.score = score;
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

}
