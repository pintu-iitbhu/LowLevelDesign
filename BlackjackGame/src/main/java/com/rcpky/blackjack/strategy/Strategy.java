package com.rcpky.blackjack.strategy;

import com.rcpky.blackjack.game.Action;
import com.rcpky.blackjack.model.Hand;

public interface Strategy {
    Action decide(Hand playerHand, int dealerUpcardValue, boolean canDouble, boolean canSplit, boolean firstAction);
}
