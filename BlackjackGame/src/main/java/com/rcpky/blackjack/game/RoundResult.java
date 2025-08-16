package com.rcpky.blackjack.game;

import com.rcpky.blackjack.model.Hand;

public record RoundResult(int handIndex, Hand playerHand, Hand dealerHand, RoundOutcome outcome, double payout) {}
