package com.tanisca.thelaststick.model;

import java.util.ArrayList;
import java.util.Collections;

public class OriginalIntelligenceNormal extends AbstractOriginalIntelligence {

    public OriginalIntelligenceNormal() {
        this.difficulty = Difficulty.NORMAL;
    }

    /**
     * Joue au hasard parmi les coups non fatal à vue 2 coups
     */
    public int getMove() {
        int sticks = this.getGame().getRemainingSticks();

        // Coup gagnant avec le 1 ou coup fatal inévitable
        if (sticks < 3) {
            return 1;
        }

        // Coup gagnant avec les autres pions
        ArrayList<Integer> pawns = this.getGame().getPawns().get(Player.AI);
        for (Integer pawn : pawns) {
            if (sticks - pawn == 1) {
                return pawn;
            }
        }

        // Coup non gagnant mais non perdant à vue 1 coup
        ArrayList<Integer> playerPawns = this.getGame().getPawns()
                .get(Player.ONE);
        ArrayList<Integer> nonFatalPawns = new ArrayList<Integer>(3);
        for (Integer pawn : pawns) {
            if (pawn < sticks) {
                int leftSticksForPlayerOne = sticks - pawn;
                boolean nonFatalPawn = true;
                for (Integer playerPawn : playerPawns) {
                    if (leftSticksForPlayerOne - playerPawn == 1) {
                        nonFatalPawn = false;
                    }
                }
                if (nonFatalPawn) {
                    nonFatalPawns.add(pawn);
                }
            }
        }

        // Si tous les coups sont fatals à vue 1 coup, on choisi au hasard
        if (nonFatalPawns.isEmpty()) {
            return this.getRandomMove(sticks);
        }

        // Parmi les coups non fatals, on choisi au hasard
        Collections.shuffle(nonFatalPawns);
        return nonFatalPawns.get(0);
    }
}
