package com.rcpky.blackjack.shoe;

import com.rcpky.blackjack.model.Card;
import com.rcpky.blackjack.model.Rank;
import com.rcpky.blackjack.model.Suit;

import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Shoe {
    private final Deque<Card> cards = new ArrayDeque<>();
    private final int decks;

    public Shoe(int decks) {
        if (decks <= 0) throw new IllegalArgumentException("decks must be > 0");
        this.decks = decks;
        List<Card> tmp = new ArrayList<>();
        for (int d = 0; d < decks; d++) {
            for (Suit s : Suit.values()) {
                for (Rank r : Rank.values()) {
                    tmp.add(new Card(r, s));
                }
            }
        }
        long seed = new SecureRandom().nextLong();
        Collections.shuffle(tmp, new java.util.Random(seed));
        for (Card c : tmp) cards.addLast(c);
    }

    public Card deal() { return cards.removeFirst(); }
    public int remaining() { return cards.size(); }
    public int decks() { return decks; }
}
