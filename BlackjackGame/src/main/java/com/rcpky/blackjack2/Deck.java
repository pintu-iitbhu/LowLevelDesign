package com.rcpky.blackjack2;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (int v = 1; v <= 13; v++) {
                cards.add(new Card(suit, v));
            }
        }
    }

    public List<Card> getCards() {
        return cards;
    }
}
