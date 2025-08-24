package com.rcpky.pieces;

import com.rcpky.game.Box;
import com.rcpky.game.ChessBoard;
import lombok.Getter;


public class Bishop extends  Piece {
    public Bishop(boolean isWhite) {
        super("B", isWhite);
    }

    @Override
    public boolean canMove(ChessBoard board, Box start, Box end) {
        int dx = Math.abs(start.getRow() - end.getRow());
        int dy = Math.abs(start.getCol() - end.getCol());
        if (dx == dy &&
                (end.getPiece() == null || end.getPiece().isWhite() != this.isWhite())) {
            return !ChessBoard.isPathBlocked(board, start, end);
        }
        return false;
    }
}
