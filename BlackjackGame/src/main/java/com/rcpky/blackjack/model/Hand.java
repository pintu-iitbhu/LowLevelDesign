package com.rcpky.blackjack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();
    private boolean isCompleted; // stood, busted, or blackjack resolved
    private boolean isDoubled;
    private boolean isSplitAces; // rule: often 1 card only when split aces
    private boolean surrendered;

    public void add(Card c) { cards.add(c); }

    public List<Card> cards() { return Collections.unmodifiableList(cards); }

    public int hardValue() {
        int sum = 0;
        for (Card c : cards) sum += c.value() == 11 ? 1 : c.value();
        return sum;
    }

    public int bestValue() {
        int sum = 0; int aces = 0;
        for (Card c : cards) {
            if (c.isAce()) aces++; else sum += c.value();
        }
        // count all aces as 1 initially
        sum += aces; 
        // upgrade some aces to 11 if it doesn't bust
        while (aces > 0 && sum + 10 <= 21) { // add extra 10 for one ace
            sum += 10; aces--;
        }
        return sum;
    }

    public boolean isBlackjack() { return cards.size() == 2 && bestValue() == 21; }
    public boolean isBust() { return bestValue() > 21; }
    public boolean isSoft() {
        // soft if any ace counted as 11
        int sum = 0; int aces = 0;
        for (Card c : cards) { if (c.isAce()) aces++; else sum += c.value(); }
        sum += aces;
        return aces > 0 && sum + 10 <= 21; 
    }

    public boolean canSplit() {
        return cards.size() == 2 && cards.get(0).rank() == cards.get(1).rank();
    }

    public void markCompleted() { this.isCompleted = true; }
    public boolean isCompleted() { return isCompleted; }
    public void markDoubled() { this.isDoubled = true; }
    public boolean isDoubled() { return isDoubled; }
    public void setSplitAces(boolean v) { this.isSplitAces = v; }
    public boolean isSplitAces() { return isSplitAces; }
    public void markSurrendered() { this.surrendered = true; this.isCompleted = true; }
    public boolean isSurrendered() { return surrendered; }

    public void clear() {
        this.cards.clear();
        this.isCompleted = false;
        this.isDoubled = false;
        this.isSplitAces = false;
        this.surrendered = false;
    }
}
