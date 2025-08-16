package com.rcpky.blackjack;

import com.rcpky.blackjack.model.Card;
import com.rcpky.blackjack.model.Hand;
import com.rcpky.blackjack.model.Rank;
import com.rcpky.blackjack.model.Suit;
import com.rcpky.blackjack.rules.TableRules;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DealerRuleTest {
    @Test
    void testDealerStandSoft17WhenConfigured() {
        TableRules rulesStandSoft17 = new TableRules(false, 1.5, true, true);
        Hand dealer = new Hand();
        dealer.add(new Card(Rank.ACE, Suit.CLUBS));
        dealer.add(new Card(Rank.SIX, Suit.DIAMONDS)); // soft 17
        assertFalse(rulesStandSoft17.dealerShouldHit(dealer));

        TableRules rulesHitSoft17 = new TableRules(true, 1.5, true, true);
        assertTrue(rulesHitSoft17.dealerShouldHit(dealer));
    }
}
