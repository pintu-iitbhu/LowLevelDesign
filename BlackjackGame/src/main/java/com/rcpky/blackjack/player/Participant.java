package com.rcpky.blackjack.player;

import com.rcpky.blackjack.model.Hand;

public interface Participant {
    Hand getHand();
    void newHand();
}
