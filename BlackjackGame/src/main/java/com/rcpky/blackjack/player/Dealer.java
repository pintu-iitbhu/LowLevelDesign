package com.rcpky.blackjack.player;

import com.rcpky.blackjack.model.Hand;

public class Dealer implements Participant {
    private final Hand hand = new Hand();

    @Override
    public Hand getHand() { return hand; }

    @Override
    public void newHand() {
        // reset the dealer's hand
        hand.clear();
    }
}
