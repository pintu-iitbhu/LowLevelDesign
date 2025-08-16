package com.rcpky.blackjack.strategy;

import com.rcpky.blackjack.game.Action;
import com.rcpky.blackjack.model.Card;
import com.rcpky.blackjack.model.Hand;
import com.rcpky.blackjack.model.Rank;

public class BasicHeuristicStrategy implements Strategy {
    @Override
    public Action decide(Hand hand, int dealerUp, boolean canDouble, boolean canSplit, boolean firstAction) {
        // Split A,A or 8,8
        if (canSplit) {
            Card c = hand.cards().get(0);
            if (c.rank() == Rank.ACE || c.rank() == Rank.EIGHT) return Action.SPLIT;
        }
        int total = hand.bestValue();
        if (firstAction && canDouble && total == 11) return Action.DOUBLE;
        if (total >= 17) return Action.STAND;
        if (hand.isSoft()) {
            if (total >= 19) return Action.STAND;
            return Action.HIT;
        }
        if (total <= 11) return Action.HIT;
        if (total == 12 && dealerUp >= 4 && dealerUp <= 6) return Action.STAND;
        if (total >= 13 && dealerUp <= 6) return Action.STAND;
        return Action.HIT;
    }
}
