package com.rcpky.blackjack.player;

import com.rcpky.blackjack.model.Hand;

import java.util.ArrayList;
import java.util.List;

public class Player implements Participant {
    private final String name;
    private double bankroll;
    private final List<Hand> hands = new ArrayList<>();

    public Player(String name, double bankroll) {
        this.name = name;
        this.bankroll = bankroll;
    }

    public String getName() { return name; }
    public double getBankroll() { return bankroll; }
    public void debit(double amount) { bankroll -= amount; }
    public void credit(double amount) { bankroll += amount; }

    @Override
    public Hand getHand() {
        if (hands.isEmpty()) hands.add(new Hand());
        return hands.get(0);
    }

    public List<Hand> getHands() { return hands; }

    @Override
    public void newHand() {
        hands.clear();
        hands.add(new Hand());
    }

    public void addSplitHand(Hand hand) { this.hands.add(hand); }
}
