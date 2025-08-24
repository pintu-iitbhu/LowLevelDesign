# Chess Game Implementation

A Java-based command-line chess game implementing standard chess rules.

## Features

- Two-player chess game (White vs Black)
- Standard chess board (8x8)
- Basic piece movements for all chess pieces
- Turn-based gameplay
- Move validation
- Game status tracking
- Move history

## Implementation Details

### Project Structure

```
src/main/java/com/rcpky/
├── ChessApp.java           # Main application entry point
├── controller/             # Game controllers and view
│   ├── ChessGameView.java  # Handles game display
│   └── ChessMoveController.java # Handles move validation
├── enums/                  # Enumerations for game states
│   ├── GameStatus.java
│   └── MoveType.java
├── game/                   # Core game logic
│   ├── Box.java           # Represents a square on the board
│   ├── ChessBoard.java    # Chess board implementation
│   ├── ChessGame.java     # Main game class
│   ├── Move.java          # Move representation
│   └── Player.java        # Player information
└── pieces/                # Chess piece implementations
    ├── Bishop.java
    ├── King.java
    ├── Knight.java
    ├── Pawn.java
    ├── Piece.java         # Abstract base class
    ├── Queen.java
    └── Rook.java
```

### Missing/Incomplete Features

1. **Special Moves**:
   - Castling
   - En passant
   - Pawn promotion

2. **Game End Conditions**:
   - Proper check detection
   - Checkmate detection
   - Stalemate detection
   - Threefold repetition rule
   - 50-move rule (partially implemented)
   - Insufficient material

3. **Input/Output**:
   - Better error handling for invalid moves
   - Save/load game state
   - Move notation (algebraic notation)

4. **Game Flow**:
   - Player timer/clock
   - Game result announcement
   - Move undo/redo

## Running the Game

1. Ensure you have Java 8 or higher installed
2. Compile the project:
   ```bash
   javac -d target/classes src/main/java/com/rcpky/*.java src/main/java/com/rcpky/**/*.java
   ```
3. Run the game:
   ```bash
   java -cp target/classes com.rcpky.ChessApp
   ```

## How to Play

1. Players take turns entering moves in the format: `[source] [destination]` (e.g., `e2 e4`)
2. Enter `resign` to forfeit the game
3. The game currently ends after 50 moves (for demonstration purposes)

## Implementation Notes

- The code follows object-oriented design principles
- Each piece type has its own class extending the base `Piece` class
- The game state is maintained in the `ChessGame` class
- Move validation is handled by the `ChessMoveController`
- The current implementation is text-based but designed to be easily extendable to a GUI
