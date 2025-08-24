package com.rcpky.pieces;

import com.rcpky.game.Box;
import com.rcpky.game.ChessBoard;

public class Pawn extends  Piece {
    public Pawn(boolean isWhite) {
        super("P", isWhite);
    }

    @Override
    public boolean canMove(ChessBoard board, Box start, Box end) {
        int dir = isWhite() ? 1 : -1;
        int startRow = isWhite() ? 1 : 6;
        int dx = end.getRow() - start.getRow();
        int dy = Math.abs(end.getCol() - start.getCol());
        // Move forward
        if (dy == 0 && start.getPiece() != null && end.getPiece() == null) {
            if (start.getRow() + dir == end.getRow()) return true;
            if (start.getRow() == startRow && start.getRow() + 2*dir == end.getRow()
                    && board.getBox(start.getRow() + dir, start.getCol()).getPiece() == null)
                return true;
        }
        // Capture diagonally
        if (dx == dir && dy == 1 && end.getPiece() != null && end.getPiece().isWhite() != this.isWhite()) {
            return true;
        }
        return false;
    }
}
