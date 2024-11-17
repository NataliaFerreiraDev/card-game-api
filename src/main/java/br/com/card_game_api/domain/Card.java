package br.com.card_game_api.domain;


import jakarta.persistence.*;

/**
 * Representa uma carta do baralho.
 * Cada carta possui um valor e um naipe.
 */
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2)
    private String value; // Valor da carta (ex: "A", "2", "K")

    @Column(nullable = false, length = 10)
    private String suit; // Naipe da carta (ex: "Espadas", "Copas")

    public Card() {}

    public Card(String value, String suit) {
        this.value = value;
        this.suit = suit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

}
