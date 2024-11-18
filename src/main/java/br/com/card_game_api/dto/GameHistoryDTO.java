package br.com.card_game_api.dto;

import java.time.LocalDateTime;

/**
 * DTO que representa o histórico de um jogo de cartas.
 */
public class GameHistoryDTO {

    private Long id;
    private int numberOfPlayers;
    private int cardsPerPlayer;
    private String deckId;
    private String winner;
    private int highestScore;
    private LocalDateTime gameTimestamp;

    public GameHistoryDTO() {
    }

    public GameHistoryDTO(Long id,
                          int numberOfPlayers,
                          int cardsPerPlayer,
                          String deckId,
                          String winner,
                          int highestScore,
                          LocalDateTime gameTimestamp) {
        this.id = id;
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

}
