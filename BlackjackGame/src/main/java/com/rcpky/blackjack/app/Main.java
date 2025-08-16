package com.rcpky.blackjack.app;

import com.rcpky.blackjack.game.GameEngine;
import com.rcpky.blackjack.game.RoundResult;
import com.rcpky.blackjack.player.Player;
import com.rcpky.blackjack.rules.TableRules;
import com.rcpky.blackjack.shoe.Shoe;
import com.rcpky.blackjack.strategy.BasicHeuristicStrategy;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Shoe shoe = new Shoe(4);
        TableRules rules = TableRules.defaultVegas();
        GameEngine engine = new GameEngine(shoe, rules);
        Player player = new Player("You", 1000);
        var strategy = new BasicHeuristicStrategy();
        double bet = 10;
        List<RoundResult> results = engine.playRound(player, bet, strategy);
        System.out.println("After round, bankroll: " + player.getBankroll());
        for (RoundResult r : results) {
            System.out.println("Hand #" + r.handIndex() + " => " + r.outcome() + ", payout=" + r.payout());
        }
    }
}
