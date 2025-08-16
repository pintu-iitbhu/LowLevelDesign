package com.rcpky.blackjack.game;

import com.rcpky.blackjack.model.Card;
import com.rcpky.blackjack.model.Hand;
import com.rcpky.blackjack.player.Dealer;
import com.rcpky.blackjack.player.Player;
import com.rcpky.blackjack.rules.TableRules;
import com.rcpky.blackjack.shoe.Shoe;
import com.rcpky.blackjack.strategy.Strategy;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private final Shoe shoe;
    private final TableRules rules;

    public GameEngine(Shoe shoe, TableRules rules) {
        this.shoe = shoe;
        this.rules = rules;
    }

    public List<RoundResult> playRound(Player player, double baseBet, Strategy strategy) {
        // For simplicity, 1 player vs dealer
        Dealer dealer = new Dealer();
        player.newHand();
        Hand dealerHand = dealer.getHand();
        Hand playerHand = player.getHand();

        // initial deal
        dealCard(playerHand); dealCard(dealerHand);
        dealCard(playerHand); dealCard(dealerHand);

        List<Hand> activeHands = new ArrayList<>();
        activeHands.add(playerHand);
        List<Double> bets = new ArrayList<>();
        bets.add(baseBet);

        // check natural blackjack
        List<RoundResult> results = new ArrayList<>();
        if (playerHand.isBlackjack() || dealerHand.isBlackjack()) {
            RoundOutcome outcome;
            double payout = 0;
            if (playerHand.isBlackjack() && !dealerHand.isBlackjack()) {
                outcome = RoundOutcome.BLACKJACK;
                payout = baseBet * rules.blackjackPayout();
            } else if (!playerHand.isBlackjack() && dealerHand.isBlackjack()) {
                outcome = RoundOutcome.LOSE;
                payout = -baseBet;
            } else {
                outcome = RoundOutcome.PUSH;
            }
            results.add(new RoundResult(0, copy(playerHand), copy(dealerHand), outcome, payout));
            player.credit(payout);
            return results;
        }

        // play player hands
        for (int i = 0; i < activeHands.size(); i++) {
            Hand h = activeHands.get(i);
            boolean firstAction = true;
            while (!h.isBust() && !h.isCompleted()) {
                int dealerUp = dealerHand.cards().get(0).value();
                boolean canDouble = firstAction;
                boolean canSplit = firstAction && h.canSplit();
                Action action = strategy.decide(h, dealerUp, canDouble, canSplit, firstAction);
                switch (action) {
                    case HIT -> dealCard(h);
                    case STAND -> h.markCompleted();
                    case DOUBLE -> {
                        if (canDouble) {
                            bets.set(i, bets.get(i) * 2);
                            h.markDoubled();
                            dealCard(h);
                            h.markCompleted();
                        } else {
                            dealCard(h); // fallback to hit
                        }
                    }
                    case SPLIT -> {
                        if (canSplit) {
                            // split into two hands
                            Hand h2 = new Hand();
                            Card second = h.cards().get(1);
                            // Create new hand objects to simulate split: h keeps first card
                            Hand h1 = new Hand();
                            h1.add(h.cards().get(0));
                            h2.add(second);
                            // replace current hand with h1
                            activeHands.set(i, h1);
                            // add new hand after current
                            activeHands.add(i + 1, h2);
                            // duplicate bet
                            bets.set(i, bets.get(i));
                            bets.add(i + 1, baseBet);
                            // deal one card to each split hand
                            dealCard(h1);
                            dealCard(h2);
                            // continue playing current index hand from start
                            h = h1;
                            firstAction = true; // still first action after split
                        } else {
                            dealCard(h);
                        }
                    }
                    case SURRENDER -> {
                        if (rules.allowSurrender() && firstAction) {
                            h.markSurrendered();
                            double payout = -bets.get(i) / 2;
                            player.credit(payout);
                            results.add(new RoundResult(i, copy(h), null, RoundOutcome.LOSE, payout));
                        } else {
                            dealCard(h);
                        }
                    }
                }
                if (h.isSurrendered()) break; // surrendered hand handled
                if (h.isBust()) h.markCompleted();
                firstAction = false;
            }
        }

        // dealer plays if at least one hand is still live
        boolean anyLive = activeHands.stream().anyMatch(hand -> !hand.isBust());
        if (anyLive) {
            while (rules.dealerShouldHit(dealerHand)) {
                dealCard(dealerHand);
            }
        }

        // settle
        for (int i = 0; i < activeHands.size(); i++) {
            Hand h = activeHands.get(i);
            double bet = bets.get(i);
            if (h.isSurrendered()) {
                // Already settled when surrender occurred
                continue;
            }
            if (h.isBust()) {
                player.credit(-bet);
                results.add(new RoundResult(i, copy(h), copy(dealerHand), RoundOutcome.LOSE, -bet));
                continue;
            }
            int pv = h.bestValue();
            int dv = dealerHand.bestValue();
            if (dealerHand.isBust() || pv > dv) {
                player.credit(bet);
                results.add(new RoundResult(i, copy(h), copy(dealerHand), RoundOutcome.WIN, bet));
            } else if (pv < dv) {
                player.credit(-bet);
                results.add(new RoundResult(i, copy(h), copy(dealerHand), RoundOutcome.LOSE, -bet));
            } else {
                results.add(new RoundResult(i, copy(h), copy(dealerHand), RoundOutcome.PUSH, 0));
            }
        }
        return results;
    }

    private void dealCard(Hand h) { h.add(shoe.deal()); }

    private Hand copy(Hand h) {
        Hand c = new Hand();
        for (Card card : h.cards()) c.add(card);
        return c;
    }
}
