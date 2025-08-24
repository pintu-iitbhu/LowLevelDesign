package com.rcpky.pieces;

import com.rcpky.game.Box;
import com.rcpky.game.ChessBoard;

public class Queen extends  Piece {
    public Queen(boolean isWhite) {
        super("Q", isWhite);
    }

    @Override
    public boolean canMove(ChessBoard board, Box start, Box end) {
        int dx = Math.abs(start.getRow() - end.getRow());
        int dy = Math.abs(start.getCol() - end.getCol());
        if ((dx == dy || dx == 0 || dy == 0) &&
                (end.getPiece() == null || end.getPiece().isWhite() != this.isWhite())) {
            // check path is not blocked
            return !ChessBoard.isPathBlocked(board, start, end);
        }
        return false;
    }
}
