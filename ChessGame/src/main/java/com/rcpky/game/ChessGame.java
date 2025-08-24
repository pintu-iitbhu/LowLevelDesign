package com.rcpky.game;

import com.rcpky.controller.ChessGameView;
import com.rcpky.controller.ChessMoveController;
import com.rcpky.enums.GameStatus;
import com.rcpky.enums.MoveType;
import com.rcpky.pieces.Piece;
import lombok.Getter;

import java.util.*;

@Getter
public class ChessGame {
    Queue<Player> playersQueue;
    ChessBoard board;
    Player currentPlayer;
    GameStatus gameStatus = GameStatus.INACTIVE;
    ChessMoveController gameController;
    ChessGameView gameView;
    List<Move> moveHistory;
    private final String gameId;

    public ChessGame(Player white, Player black) {
        playersQueue = new LinkedList<>();
        playersQueue.add(white);
        playersQueue.add(black);
        this.board = new ChessBoard(8, 8);
        this.currentPlayer = playersQueue.peek();
        this.gameController = new ChessMoveController();
        this.gameView = new ChessGameView();
        board.resetBoard();
        this.gameId = UUID.randomUUID().toString();
        this.moveHistory = new ArrayList<>();

    }

    public void play() {
        Scanner sc = new Scanner(System.in);
        gameView.showBoard(board);
        while (gameStatus == GameStatus.ACTIVE) {
            System.out.println(currentPlayer.getName() + " to move (format: e2 e4, or 'resign'):");
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("resign")) {
                gameStatus = currentPlayer.isWhiteSide() ? GameStatus.BLACK_WIN : GameStatus.WHITE_WIN;
                System.out.println(currentPlayer.getName() + " resigns! " + (gameStatus == GameStatus.WHITE_WIN ? "White" : "Black") + " wins.");
                break;
            }
            if (!input.matches("[a-h][1-8] [a-h][1-8]")) {
                System.out.println("Invalid input format!");
                continue;
            }
            int sx = input.charAt(1) - '1', sy = input.charAt(0) - 'a';
            int ex = input.charAt(4) - '1', ey = input.charAt(3) - 'a';
            Box start = board.getBox(sx, sy), end = board.getBox(ex, ey);
            Piece piece = start.getPiece();
            if (piece == null) {
                System.out.println("No piece at source!");
                continue;
            }
            if (piece.isWhite() != currentPlayer.isWhiteSide()) {
                System.out.println("Not your piece!");
                continue;
            }
            if (!gameController.validateMove(piece, board, start, end)) {
                System.out.println("Invalid move for " + piece.getName());
                continue;
            }
            Piece captured = end.getPiece();
            end.setPiece(piece);
            start.setPiece(null);
            Move move = new Move(start, end, piece, captured, currentPlayer, MoveType.NORMAL);
            moveHistory.add(move);
            gameView.showMove(move);
            gameView.showBoard(board);

            // TODO: Add checkmate, stalemate, and draw logic
            // For demo: end after 50 moves or resignation
            if (moveHistory.size() > 50) { gameStatus = GameStatus.DRAW; System.out.println("Draw by 50 moves."); break; }
            // Switch turn
            playersQueue.add(playersQueue.remove());
            currentPlayer = playersQueue.peek();

        }
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
