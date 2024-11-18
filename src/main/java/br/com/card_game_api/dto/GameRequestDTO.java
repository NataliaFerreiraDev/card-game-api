package br.com.card_game_api.dto;

public class GameRequestDTO {

    private int numPlayers;

    private int cardsPerHand;

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getCardsPerHand() {
        return cardsPerHand;
    }

    public void setCardsPerHand(int cardsPerHand) {
        this.cardsPerHand = cardsPerHand;
    }

}
