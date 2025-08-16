package com.rcpky.blackjack2;

import java.util.*;

public abstract class Player {
    protected double balance;
    protected AccountStatus status;
    protected Person person;
    protected Hand hand;

    public Player() {
        this.status = AccountStatus.ACTIVE;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }

    public double getBalance() {
        return balance;
    }

    public abstract boolean resetPassword();
}
