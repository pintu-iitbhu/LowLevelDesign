package com.rcpky.blackjack2;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;

    public Hand(Card card1, Card card2) {
        cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getScore() {
        int total = 0, aces = 0;
        for (Card c : cards) {
            int v = c.getValue();
            total += v;
            if (c.getFaceValue() == 1) aces++;
        }
        while (aces > 0 && total + 10 <= 21) {
            total += 10;
            aces--;
        }
        return total;
    }

    @Override
    public String toString() {
        return cards.toString() + " (" + getScore() + ")";
    }
}
