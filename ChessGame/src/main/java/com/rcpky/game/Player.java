package com.rcpky.game;

import lombok.Getter;

@Getter
public class Player {
    String name;
    boolean isWhiteSide;

    public Player(String name, boolean white) {
        this.name = name;
        this.isWhiteSide = white;
    }

}
