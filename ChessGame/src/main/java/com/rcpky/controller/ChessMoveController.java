package com.rcpky.controller;

import com.rcpky.game.Box;
import com.rcpky.game.ChessBoard;
import com.rcpky.pieces.Piece;

public class ChessMoveController {

    public boolean validateMove(Piece piece, ChessBoard board, Box start, Box end) {
        return piece != null && piece.canMove(board, start, end);
    }
}
