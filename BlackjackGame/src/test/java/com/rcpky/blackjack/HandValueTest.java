package com.rcpky.blackjack;

import com.rcpky.blackjack.model.Card;
import com.rcpky.blackjack.model.Hand;
import com.rcpky.blackjack.model.Rank;
import com.rcpky.blackjack.model.Suit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HandValueTest {
    @Test
    void testSoftAndHardAces() {
        Hand h = new Hand();
        h.add(new Card(Rank.ACE, Suit.CLUBS));
        h.add(new Card(Rank.SIX, Suit.HEARTS));
        assertTrue(h.isSoft());
        assertEquals(17, h.bestValue());
        h.add(new Card(Rank.NINE, Suit.SPADES));
        assertFalse(h.isSoft());
        assertEquals(16, h.bestValue());
    }

    @Test
    void testBlackjack() {
        Hand h = new Hand();
        h.add(new Card(Rank.ACE, Suit.CLUBS));
        h.add(new Card(Rank.KING, Suit.HEARTS));
        assertTrue(h.isBlackjack());
    }
}
