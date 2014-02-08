package com.tanisca.thelaststick.model;

import com.tanisca.thelaststick.R;

public enum Player {

    BOTH(0, 0), ONE(1, R.string.name_player1), TWO(2, R.string.name_player2), AI(
            9, R.string.name_ai);

    private int value;
    private int name;

    Player(int value, int name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public int getName() {
        return name;
    }

    public boolean canPlay(Player player) {
        return (player == BOTH || this == player);
    }
}
