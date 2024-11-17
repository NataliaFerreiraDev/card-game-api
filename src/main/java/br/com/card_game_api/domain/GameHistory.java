package br.com.card_game_api.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class GameHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "game_history_id")
    private List<Player> players;

    private String winner;

    // Construtores
    public GameHistory() {
    }

    public GameHistory(List<Player> players, String winner) {
        this.players = players;
        this.winner = winner;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

}
