package com.rcpky.game;

import com.rcpky.pieces.Piece;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Box {
    private Piece piece;
    private int row;
    private int col;

    public Box(int row, int col) {
        this.row = row;
        this.col = col;
        this.piece = null;
    }

    public boolean isOccupied() {
        return piece != null;
    }

}
