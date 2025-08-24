package com.rcpky.pieces;

import com.rcpky.game.Box;
import com.rcpky.game.ChessBoard;

public class Knight extends Piece {
    public Knight(boolean isWhite) {
        super("Kn", isWhite);
    }

    @Override
    public boolean canMove(ChessBoard board, Box start, Box end) {
        int dx = Math.abs(start.getRow() - end.getRow());
        int dy = Math.abs(start.getCol() - end.getCol());
        if (((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) &&
                (end.getPiece() == null || end.getPiece().isWhite() != this.isWhite()))
            return true;
        return false;

    }
}
