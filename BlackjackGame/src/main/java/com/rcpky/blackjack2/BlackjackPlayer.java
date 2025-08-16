package com.rcpky.blackjack2;

import java.util.*;

public class BlackjackPlayer extends Player {
    private double bet;

    public BlackjackPlayer(double balance) {
        super();
        this.balance = balance;
    }

    public void placeBet(double amount) {
        if (status != AccountStatus.ACTIVE)
            throw new IllegalStateException("Account not active.");
        if (amount <= 0 || amount > balance)
            throw new IllegalArgumentException("Invalid bet amount.");
        bet = amount;
        balance -= amount;
    }

    public double getBet() {
        return bet;
    }

    // Handles payout (win=+1, blackjack=+1.5, push=0, loss=-1)
    public void payout(double multiplier) {
        balance += bet * (1 + multiplier);
        System.out.println("Balance updated: $" + balance);
        bet = 0;
    }

    @Override
    public boolean resetPassword() {
        // stub: imagine email reset sent
        return true;
    }
}
