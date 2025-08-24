package com.rcpky.controller;

import com.rcpky.game.ChessBoard;
import com.rcpky.game.Move;

public class ChessGameView {
    public void showBoard(ChessBoard board) {
        board.printBoard();
    }
    public void showMove(Move move) {
        System.out.println(move.getPieceMoved().getName() + " " + move.getStart().getRow() + move.getStart().getCol() + " " + move.getEnd().getRow() + move.getEnd().getCol());
    }
}
