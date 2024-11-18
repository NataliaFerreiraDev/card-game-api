package br.com.card_game_api.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa o histórico de um jogo de cartas.
 * Contém informações sobre os jogadores, vencedor, deck, pontuação e data.
 */
@Entity
@Table(name = "game_history")
public class GameHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int numberOfPlayers; // Quantidade de jogadores

    @Column(nullable = false)
    private int cardsPerPlayer; // Quantidade de cartas por jogador

    @Column(nullable = false, length = 50)
    private String deckId; // ID do deck gerado pela API externa

    @Column(nullable = false, length = 20)
    private String winner; // Nome(s) do vencedor(es), ex: "Jogador 1"

    @Column(nullable = false)
    private int highestScore; // Maior pontuação

    @Column(nullable = false)
    private LocalDateTime gameTimestamp; // Data e hora do jogo

    @OneToMany(mappedBy = "gameHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    public GameHistory() {}

    public GameHistory(int numberOfPlayers, int cardsPerPlayer, String deckId, String winner, int highestScore, LocalDateTime gameTimestamp) {
        this.numberOfPlayers = numberOfPlayers;
        this.cardsPerPlayer = cardsPerPlayer;
        this.deckId = deckId;
        this.winner = winner;
        this.highestScore = highestScore;
        this.gameTimestamp = gameTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getCardsPerPlayer() {
        return cardsPerPlayer;
    }

    public void setCardsPerPlayer(int cardsPerPlayer) {
        this.cardsPerPlayer = cardsPerPlayer;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public LocalDateTime getGameTimestamp() {
        return gameTimestamp;
    }

    public void setGameTimestamp(LocalDateTime gameTimestamp) {
        this.gameTimestamp = gameTimestamp;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        player.setGameHistory(this);
    }

}
