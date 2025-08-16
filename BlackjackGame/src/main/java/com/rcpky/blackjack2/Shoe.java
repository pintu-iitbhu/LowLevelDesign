package com.rcpky.blackjack2;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shoe {
    private List<Deck> decks;

    public Shoe(int numberOfDecks) {
        decks = new ArrayList<>();
        for (int i = 0; i < numberOfDecks; i++) {
            decks.add(new Deck());
        }
        shuffle();
    }

    public void shuffle() {
        // Merge all cards into a temp list, shuffle, then refill decks.
        List<Card> allCards = new ArrayList<>();
        for (Deck deck : decks) {
            allCards.addAll(deck.getCards());
            deck.getCards().clear(); // Empty each deck
        }
        Collections.shuffle(allCards);
        int deckIndex = 0;
        for (Card card : allCards) {
            decks.get(deckIndex).getCards().add(card);
            deckIndex = (deckIndex + 1) % decks.size(); // Round robin
        }
    }

    public Card dealCard() {
        // Find the first deck that has cards left
        for (Deck deck : decks) {
            if (!deck.getCards().isEmpty()) {
                return deck.getCards().remove(0);
            }
        }
        throw new IllegalStateException("No more cards in the shoe");
    }

    public List<Deck> getDecks() {
        return decks;
    }
}
