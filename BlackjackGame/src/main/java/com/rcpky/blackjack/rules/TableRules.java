package com.rcpky.blackjack.rules;

import com.rcpky.blackjack.model.Hand;

public class TableRules {
    private final boolean dealerHitsSoft17;
    private final double blackjackPayout; // e.g., 1.5 for 3:2, or 1.2 for 6:5
    private final boolean allowDoubleAfterSplit;
    private final boolean allowSurrender;

    public TableRules(boolean dealerHitsSoft17, double blackjackPayout,
                      boolean allowDoubleAfterSplit, boolean allowSurrender) {
        this.dealerHitsSoft17 = dealerHitsSoft17;
        this.blackjackPayout = blackjackPayout;
        this.allowDoubleAfterSplit = allowDoubleAfterSplit;
        this.allowSurrender = allowSurrender;
    }

    public static TableRules defaultVegas() {
        return new TableRules(false, 1.5, true, true);
    }

    public boolean dealerHitsSoft17() { return dealerHitsSoft17; }
    public double blackjackPayout() { return blackjackPayout; }
    public boolean allowDoubleAfterSplit() { return allowDoubleAfterSplit; }
    public boolean allowSurrender() { return allowSurrender; }

    public boolean dealerShouldHit(Hand dealerHand) {
        int value = dealerHand.bestValue();
        if (value < 17) return true;
        if (value > 17) return false;
        return dealerHitsSoft17 && dealerHand.isSoft();
    }
}
