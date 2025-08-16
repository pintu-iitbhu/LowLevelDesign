# BlackjackGame (Interview-Ready LLD)

This module implements a clean, object-oriented Blackjack game suitable for Low-Level Design interviews.

## Highlights
- **Card model**: `Card`, `Rank`, `Suit`, `Hand` in `com.rcpky.blackjack.model`
- **Shoe (deck)**: `Shoe` in `com.rcpky.blackjack.shoe`
- **Rules**: `TableRules` in `com.rcpky.blackjack.rules` (dealer hits/stands on soft-17, 3:2 payout, DAS, surrender)
- **Participants**: `Player`, `Dealer` in `com.rcpky.blackjack.player`
- **Strategy**: `Strategy` interface + `BasicHeuristicStrategy` in `com.rcpky.blackjack.strategy`
- **Engine**: `GameEngine` in `com.rcpky.blackjack.game` to play a round and settle results
- **Console app**: `com.rcpky.blackjack.app.Main`
- **Tests**: `HandValueTest`, `DealerRuleTest`

## Package Overview
- `model/`
  - `Card`, `Rank`, `Suit`: immutable card definition
  - `Hand`: hand value calc (soft/hard), blackjack, bust, split eligibility; supports `clear()`, `markSurrendered()`
- `shoe/`
  - `Shoe`: N-deck shoe with shuffled cards; `deal()`
- `rules/`
  - `TableRules`: core table toggles and dealer policy. `dealerShouldHit(Hand)` encapsulates dealer logic.
- `player/`
  - `Player`: bankroll mgmt, supports multiple hands (for splits)
  - `Dealer`: has a `Hand`, resets via `newHand()`
- `strategy/`
  - `Strategy`: pluggable player policy, returns `Action`
  - `BasicHeuristicStrategy`: simple but sensible defaults
- `game/`
  - `Action`, `RoundOutcome`, `RoundResult`
  - `GameEngine`: wires shoe, rules, players, and strategy to run a complete round, including split/double/surrender and settlement

## How a Round Works
1. Initial deal (2 cards each; dealer upcard at index 0)
2. Natural blackjack check (3:2 payout when only player has it; push on both)
3. Player decisions by `Strategy` per hand (split creates two hands and duplicates bet)
4. Dealer plays via `TableRules` (S17/H17 configurable)
5. Settlement: win/lose/push per hand, including double and surrender (½ loss)

## Extensibility (common interview asks)
- **Insurance**: Add `Action.INSURE`, store insurance bet per hand, settle when dealer blackjack.
- **Multiple players**: Extend `GameEngine.playRound()` to iterate a list of players.
- **Table limits**: Validate bets before play.
- **Advanced strategy**: Implement full basic strategy matrix or count-based strategies.
- **Persistence/Stats**: Track hands played, EV, streaks; export to CSV.
- **REST/GUI**: Expose `GameEngine` via Spring REST or a JavaFX/CLI interface.

## Build & Run
Your environment may have SSL issues fetching test deps. You can:
- Build jar (skipping tests):
  - `./mvnw -pl BlackjackGame -am -DskipTests package`
- Run console app:
  - `java -cp BlackjackGame/target/BlackjackGame-*.jar com.rcpky.blackjack.app.Main`

Or, if tests work in your environment:
- `./mvnw -pl BlackjackGame -am test`

## Design Rationale (talking points)
- **Single Responsibility**: `Hand` only manages cards/state; `Shoe` only deals; `TableRules` only rules.
- **Open/Closed**: `Strategy` interface allows adding strategies without modifying the engine.
- **Encapsulation**: `Hand.cards()` exposes unmodifiable list; mutations via methods only.
- **Determinism for testing**: `Shoe` can be adapted to accept a seeded `Random` or a fixed deck for deterministic tests.
- **Error Handling**: Guardrails on shoe decks; safe defaults for actions (e.g., treat invalid double/split as hit/stand fallback).

## Files of Interest
- `src/main/java/com/rcpky/blackjack/game/GameEngine.java`
- `src/main/java/com/rcpky/blackjack/rules/TableRules.java`
- `src/main/java/com/rcpky/blackjack/strategy/BasicHeuristicStrategy.java`

## Next Ideas
- Add `Insurance` and corresponding tests.
- Add a proper CLI loop to play multiple rounds and choose actions manually.
- Use dependency injection to pass a deterministic shoe in tests.
