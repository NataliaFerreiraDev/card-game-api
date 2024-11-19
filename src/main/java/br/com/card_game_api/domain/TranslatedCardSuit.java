package br.com.card_game_api.domain;

public enum TranslatedCardSuit {
    HEARTS("Copas"),
    SPADES("Espadas"),
    DIAMONDS("Ouros"),
    CLUBS("Paus");

    private final String translatedSuit;

    TranslatedCardSuit(String translatedSuit) {
        this.translatedSuit = translatedSuit;
    }

    public String getTranslatedSuit() {
        return translatedSuit;
    }

    public static TranslatedCardSuit fromString(String suit) {
        for (TranslatedCardSuit translatedCardSuit : TranslatedCardSuit.values()) {
            if (translatedCardSuit.name().equalsIgnoreCase(suit)) {
                return translatedCardSuit;
            }
        }
        throw new IllegalArgumentException("Naipe inválido: " + suit);
    }

}
