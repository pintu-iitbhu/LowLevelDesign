package com.rcpky.blackjack2;

import java.util.*;

public class Card {
    private final Suit suit;
    private final int faceValue; // 1=Ace, 11=Jack, 12=Queen, 13=King

    public Card(Suit suit, int faceValue) {
        this.suit = suit;
        this.faceValue = faceValue;
    }

    public int getValue() {
        return (faceValue > 10) ? 10 : faceValue; // J/Q/K=10, Ace=1
    }

    public Suit getSuit() {
        return suit;
    }

    public int getFaceValue() {
        return faceValue;
    }

    @Override
    public String toString() {
        String name;
        switch (faceValue) {
            case 1:  name = "Ace";   break;
            case 11: name = "Jack";  break;
            case 12: name = "Queen"; break;
            case 13: name = "King";  break;
            default: name = String.valueOf(faceValue);
        }
        return name + " of " + suit;
    }
}