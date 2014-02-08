package com.tanisca.thelaststick.model;

import java.util.ArrayList;

public enum GameMode {

    CLASSIC(0, new int[] {}, new int[] { 1, 2, 3 }),
    ORIGINAL(2, new int[] { 2, 3, 4, 5, 6 }, new int[] { 1 });

    private ArrayList<Integer> fixedPawns;
    private ArrayList<Integer> availablePawns;
    private int                pawnsToDetermine;

    private GameMode(int pawnsToDetermine, int[] availablePawns,
            int[] fixedPawns) {
        this.pawnsToDetermine = pawnsToDetermine;

        this.availablePawns = new ArrayList<Integer>(availablePawns.length);
        for (int pawn : availablePawns) {
            this.availablePawns.add(pawn);
        }

        this.fixedPawns = new ArrayList<Integer>(3);
        for (int pawn : fixedPawns) {
            this.fixedPawns.add(pawn);
        }
    }

    public ArrayList<Integer> getFixedPawns() {
        return this.fixedPawns;
    }
    
    public ArrayList<Integer> getAvailablePawns() {
        return this.availablePawns;
    }

    public int getPawnsToDetermine() {
        return pawnsToDetermine;
    }
}
