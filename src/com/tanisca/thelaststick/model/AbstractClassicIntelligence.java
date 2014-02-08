package com.tanisca.thelaststick.model;

public abstract class AbstractClassicIntelligence extends AbstractIntelligence {

    public int getRandomMove(int sticks) {
        return (int) (Math.random() * 3) + 1;
    }

}
