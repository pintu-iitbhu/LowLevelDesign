package com.rcpky.pieces;

import com.rcpky.game.Box;
import com.rcpky.game.ChessBoard;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Piece {
    public static final String RESET = "\u001B[0m";
    public static final String WHITE = "\u001B[97m";
    public static final String RED = "\u001B[91m";
    public static final String GREEN = "\u001B[92m";
    protected boolean isWhite;
    protected boolean isKilled=false;
    protected String name;

    public  Piece(String name, boolean isWhite) {
        this.isWhite = isWhite;
        if (isWhite){
            this.name = GREEN + String.format("%-3s",name) + RESET;
        }else {
            this.name = RED + String.format("%-3s",name) + RESET;
        }

    }

    public abstract boolean canMove(ChessBoard board,Box start, Box end);


}
