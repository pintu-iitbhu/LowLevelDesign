package com.rcpky.blackjack2;

import java.util.*;

public class BlackjackGame {
    public BlackjackPlayer player;
    public Dealer dealer;
    public Shoe shoe;
    public BlackjackController controller;
    public BlackjackGameView view;

    public BlackjackGame(BlackjackPlayer player, Dealer dealer, int numDecks) {
        this.player = player;
        this.dealer = dealer;
        this.shoe = new Shoe(numDecks);
        this.controller = new BlackjackController();
        this.view = new BlackjackGameView();
    }

    public void start() {
        Hand pHand = new Hand(shoe.dealCard(), shoe.dealCard());
        Hand dHand = new Hand(shoe.dealCard(), shoe.dealCard());
        player.setHand(pHand);
        dealer.setHand(dHand);
        view.showHands(pHand, dHand, true);
    }

    public void playAction(String action) {
        if (!controller.validateAction(action)) {
            throw new IllegalArgumentException("Invalid action: " + action);
        }
        if ("hit".equalsIgnoreCase(action)) {
            hit();
            view.showHands(player.getHand(), dealer.getHand(), true);
            if (player.getHand().getScore() > 21) {
                settle(-1, "Player busts.");
            }
        } else {
            stand();
        }
    }

    private void hit() {
        player.getHand().addCard(shoe.dealCard());
    }

    private void stand() {
        // Dealer reveals and hits until ≥17
        while (dealer.getHand().getScore() < 17) {
            dealer.getHand().addCard(shoe.dealCard());
        }
        view.showHands(player.getHand(), dealer.getHand(), false);
        compareAndSettle();
    }

    private void compareAndSettle() {
        int p = player.getHand().getScore();
        int d = dealer.getHand().getScore();
        boolean playerBlackjack = (p == 21 && player.getHand().getCards().size() == 2);
        if (playerBlackjack) {
            settle(1.5, "Blackjack!");
        } else if (d > 21) {
            settle(1, "Dealer busts, player wins.");
        } else if (p > d) {
            settle(1, "Player beats dealer.");
        } else if (p < d) {
            settle(-1, "Dealer beats player.");
        } else {
            settle(0, "Push.");
        }
    }

    private void settle(double multiplier, String message) {
        player.payout(multiplier);
        view.showResult(message, multiplier * player.getBet());
    }
}
