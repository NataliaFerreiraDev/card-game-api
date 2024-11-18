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

    @Column(nullable = false)
    private String hand; // Mão de cartas do jogador como uma string concatenada

    @ManyToOne
    @JoinColumn(name = "game_history_id", nullable = false)
    private GameHistory gameHistory; // Associacao com o jogo

    public Player() {}

    public Player(String identifier, int score, String hand) {
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

    public GameHistory getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(GameHistory gameHistory) {
        this.gameHistory = gameHistory;
    }

}
