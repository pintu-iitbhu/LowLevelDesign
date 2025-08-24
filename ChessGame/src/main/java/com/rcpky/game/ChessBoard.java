package com.rcpky.game;

import com.rcpky.pieces.*;

public class ChessBoard {
    Box[][] boxes;

    public ChessBoard(int row, int col) {
        boxes = new Box[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                boxes[i][j] = new Box(i, j);
            }
        }
    }

    public Box getBox(int row, int col){
        if (row < 0 || row >= boxes.length || col < 0 || col >= boxes[0].length) {
            return null;
        }
        return boxes[row][col];
    }

    public void resetBoard() {
        // Clear board
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                boxes[i][j].setPiece(null);
        // Place pawns
        for (int j = 0; j < 8; j++) {
            boxes[1][j].setPiece(new Pawn(true));
            boxes[6][j].setPiece(new Pawn(false));
        }
        // Place other pieces
        boxes[0][0].setPiece(new Rook(true));   boxes[0][7].setPiece(new Rook(true));
        boxes[7][0].setPiece(new Rook(false));  boxes[7][7].setPiece(new Rook(false));
        boxes[0][1].setPiece(new Knight(true)); boxes[0][6].setPiece(new Knight(true));
        boxes[7][1].setPiece(new Knight(false));boxes[7][6].setPiece(new Knight(false));
        boxes[0][2].setPiece(new Bishop(true)); boxes[0][5].setPiece(new Bishop(true));
        boxes[7][2].setPiece(new Bishop(false));boxes[7][5].setPiece(new Bishop(false));
        boxes[0][3].setPiece(new Queen(true));
        boxes[7][3].setPiece(new Queen(false));
        boxes[0][4].setPiece(new King(true));
        boxes[7][4].setPiece(new King(false));
    }

    public static boolean isPathBlocked(ChessBoard board, Box start, Box end){
        int dx = Integer.compare(end.getRow(), start.getRow());
        int dy = Integer.compare(end.getCol(), start.getCol());
        int x = start.getRow() + dx, y = start.getCol() + dy;
        while (x != end.getRow() || y != end.getCol()) {
            if (board.getBox(x, y).getPiece() != null)
                return true;
            x += dx; y += dy;
        }
        return false;
    }

    public void printBoard() {
        // Column headers
        System.out.print("   ");
        for (char c = 'A'; c <= 'H'; c++) {
            System.out.print(" " + String.format("%-3s", c));
        }
        System.out.println();

        for (int i = 0; i < boxes.length; i++) {
            // Row number on the left
            System.out.print((8 - i) + "  ");

            for (int j = 0; j < boxes[i].length; j++) {
                String cellContent;
                if (boxes[i][j] != null && boxes[i][j].isOccupied()) {
                    cellContent = boxes[i][j].getPiece().getName();  // e.g., P, R, Kn, B, Q, K
                } else {
                    cellContent = "-";
                }
                // Fixed width = 3 for alignment
                System.out.print(" "+String.format("%-3s", cellContent));
            }

            // Row number on the right
            System.out.println(" " + (8 - i));
        }

        // Bottom column headers
        System.out.print("   ");
        for (char c = 'A'; c <= 'H'; c++) {
            System.out.print(" " + String.format("%-3s", c));
        }
        System.out.println();
    }



}
