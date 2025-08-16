package com.rcpky.blackjack2;

import java.util.*;

public class BlackjackGameView {
    // Show full hands at any state (summary view)
    public void showHands(Hand player, Hand dealer, boolean hideDealerHole) {
        System.out.println("Player: " + player);
        if (hideDealerHole) {
            Card first = dealer.getCards().get(0);
            System.out.println("Dealer: [" + first + ", <hidden>]");
        } else {
            System.out.println("Dealer: " + dealer);
        }
    }

    // Show final round result and payout
    public void showResult(String result, double payout) {
        System.out.println(result + "  (payout: " + payout + ")");
    }

    // The following highlight methods are just there to show the working
    // of the BlackjackGame, these are called in the Driver class

    // Announce when a player places a bet
    public void announceBet(double amount, double balance) {
        System.out.println("Player places bet: $" + amount + "   [Current balance: $" + balance + "]");
    }

    // Announce the initial deal for the player
    public void announceDeal(Card card1, Card card2, int score) {
        System.out.println("Player is dealt: " + card1 + ", " + card2 + "  (Total: " + score + ")");
    }

    // Announce the dealer's up-card (initial deal)
    public void announceDealerDeal(Card upCard) {
        System.out.println("Dealer is dealt: " + upCard + " (up), <hidden> (down)");
    }

    // Announce player action
    public void announcePlayerAction(String action) {
        System.out.println("Player chooses to " + action.toUpperCase() + ".");
    }

    // Announce each card drawn by the player
    public void announcePlayerDraw(Card card, int score) {
        System.out.println("Player draws: " + card + "  (Total: " + score + ")");
    }

    // Announce player bust
    public void announcePlayerBust() {
        System.out.println("Player busts! You lose your bet.");
    }

    // Announce dealer action
    public void announceDealerAction(String action) {
        System.out.println("Dealer chooses to " + action.toUpperCase() + ".");
    }

    // Announce each card drawn by the dealer
    public void announceDealerDraw(Card card, int score) {
        System.out.println("Dealer draws: " + card + "  (Total: " + score + ")");
    }

    // Announce dealer stands
    public void announceDealerStands(int score) {
        System.out.println("Dealer stands. (Total: " + score + ")");
    }

    // Announce Blackjack!
    public void announceBlackjack() {
        System.out.println("Blackjack! Player wins with 21 on the deal!");
    }

    // Announce dealer bust
    public void announceDealerBust() {
        System.out.println("Dealer busts! Player wins.");
    }

    // Announce a tie
    public void announcePush() {
        System.out.println("Push! It’s a tie. Your bet is returned.");
    }

    // Announce player win
    public void announcePlayerWin() {
        System.out.println("Player wins this round!");
    }

    // Announce dealer win
    public void announceDealerWin() {
        System.out.println("Dealer wins this round!");
    }

    // Announce payout and updated balance
    public void announcePayout(double balance) {
        System.out.println("Balance updated: $" + balance);
    }

    // Announce forfeit and balance
    public void announceForfeit(double balance) {
        System.out.println("Player forfeits.");
        System.out.println("Balance updated: $" + balance);
    }
}
