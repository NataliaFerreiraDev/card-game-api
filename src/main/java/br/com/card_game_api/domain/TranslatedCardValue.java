package br.com.card_game_api.domain;

public enum TranslatedCardValue {
    ACE("Ás"),
    KING("Rei"),
    QUEEN("Rainha"),
    JACK("Valete");

    private final String translatedValue;

    TranslatedCardValue(String translatedValue) {
        this.translatedValue = translatedValue;
    }

    public String getTranslatedValue() {
        return translatedValue;
    }

    public static TranslatedCardValue fromString(String value) {
        for (TranslatedCardValue translatedCardValue : TranslatedCardValue.values()) {
            if (translatedCardValue.name().equalsIgnoreCase(value)) {
                return translatedCardValue;
            }
        }
        throw new IllegalArgumentException("Valor de carta inválido: " + value);
    }

}
