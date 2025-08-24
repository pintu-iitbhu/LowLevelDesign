package com.rcpky.pieces;

import com.rcpky.game.Box;
import com.rcpky.game.ChessBoard;

public class King extends Piece {
    public King(boolean isWhite) {
        super("K", isWhite);
    }

    @Override
    public boolean canMove(ChessBoard board, Box start, Box end) {
        int dx = Math.abs(start.getRow() - end.getRow());
        int dy = Math.abs(start.getCol() - end.getCol());
        if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1)) {
            if (end.getPiece() == null || end.getPiece().isWhite() != this.isWhite())
                return true;
        }
        return false;
    }
}
