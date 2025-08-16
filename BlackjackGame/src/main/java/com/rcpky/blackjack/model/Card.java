package com.rcpky.blackjack.model;

import java.util.Objects;

public final class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = Objects.requireNonNull(rank);
        this.suit = Objects.requireNonNull(suit);
    }

    public Rank rank() { return rank; }
    public Suit suit() { return suit; }

    public boolean isAce() { return rank.isAce(); }

    public int value() { return rank.getValue(); }

    @Override public String toString() { return rank + " of " + suit; }
}
