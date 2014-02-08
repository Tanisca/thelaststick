package com.tanisca.thelaststick.model;

import java.util.ArrayList;
import java.util.Collections;

public class OriginalIntelligenceHard extends AbstractOriginalIntelligence {

    public OriginalIntelligenceHard() {
        this.difficulty = Difficulty.HARD;
    }

    /**
     * Joue au hasard parmi les coups non fatals à vue 2 coups (voire à vue 4
     * coups dans certains cas)
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

        // Si l'IA n'a pas de 2, ne pas laisser la possiblité au joueur de
        // laisser 3 batons
        boolean avoidPlayerLeft3sticks = !pawns.contains(Integer.valueOf(2));

        // Coup non gagnant mais non perdant à vue 1 coup
        ArrayList<Integer> playerPawns = this.getGame().getPawns()
                .get(Player.ONE);
        ArrayList<Integer> nonFatalPawns = new ArrayList<Integer>(3);
        ArrayList<Integer> nonFatalPawns3 = new ArrayList<Integer>(3);
        for (Integer pawn : pawns) {
            if (pawn < sticks) {
                int leftSticksForPlayerOne = sticks - pawn;
                boolean nonFatalPawn = true;
                boolean nonFatalPawn3 = true;
                for (Integer playerPawn : playerPawns) {
                    if (leftSticksForPlayerOne - playerPawn == 1) {
                        nonFatalPawn = false;
                    }
                    if (leftSticksForPlayerOne - playerPawn == 3) {
                        nonFatalPawn3 = false;
                    }
                }
                if (nonFatalPawn) {
                    nonFatalPawns.add(pawn);
                }
                if (avoidPlayerLeft3sticks && nonFatalPawn && nonFatalPawn3) {
                    nonFatalPawns3.add(pawn);
                }
            }
        }

        // S'il y a des coups qui evitent le 1 et le 3 (si l'IA n'a pas de 2)
        if (!nonFatalPawns3.isEmpty()) {
            nonFatalPawns = nonFatalPawns3;
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
