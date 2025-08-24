package com.rcpky.pieces;

import com.rcpky.game.Box;
import com.rcpky.game.ChessBoard;

public class Rook extends  Piece {

    public Rook(boolean isWhite) {
        super("R", isWhite);
    }

    @Override
    public boolean canMove(ChessBoard board, Box start, Box end) {
        int dx = start.getRow() - end.getRow();
        int dy = start.getCol() - end.getCol();
        if ((dx == 0 || dy == 0) &&
                (end.getPiece() == null || end.getPiece().isWhite() != this.isWhite())) {
            return !ChessBoard.isPathBlocked(board, start, end);
        }
        return false;
    }
}
