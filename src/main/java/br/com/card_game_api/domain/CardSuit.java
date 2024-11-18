package br.com.card_game_api.domain;

public enum CardSuit {
    HEARTS("Copas"),
    SPADES("Espadas"),
    DIAMONDS("Ouros"),
    CLUBS("Paus");

    private final String translatedSuit;

    CardSuit(String translatedSuit) {
        this.translatedSuit = translatedSuit;
    }

    public String getTranslatedSuit() {
        return translatedSuit;
    }

    public static CardSuit fromString(String suit) {
        for (CardSuit cardSuit : CardSuit.values()) {
            if (cardSuit.name().equalsIgnoreCase(suit)) {
                return cardSuit;
            }
        }
        throw new IllegalArgumentException("Naipe inválido: " + suit);
    }

}
