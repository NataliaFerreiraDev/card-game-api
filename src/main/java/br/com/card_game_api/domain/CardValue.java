package br.com.card_game_api.domain;

public enum CardValue {
    ACE("Ás"),
    KING("Rei"),
    QUEEN("Rainha"),
    JACK("Valete");

    private final String translatedValue;

    CardValue(String translatedValue) {
        this.translatedValue = translatedValue;
    }

    public String getTranslatedValue() {
        return translatedValue;
    }

    public static CardValue fromString(String value) {
        for (CardValue cardValue : CardValue.values()) {
            if (cardValue.name().equalsIgnoreCase(value)) {
                return cardValue;
            }
        }
        throw new IllegalArgumentException("Valor de carta inválido: " + value);
    }

}
