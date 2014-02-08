package com.tanisca.thelaststick.model;

import java.util.ArrayList;
import java.util.Collections;

public abstract class AbstractOriginalIntelligence extends AbstractIntelligence {

    /**
     * Joue au hasard en evitant de laisser 2 batons sur le plateau
     * @param sticks nombre de baton, doit être strictement supérieur à y
     */
    public int getRandomMove(int sticks) {
        
        // Recherche de tous les coups faisable et non fatals
        ArrayList<Integer> pawns = this.getGame().getPawns().get(Player.AI);
        ArrayList<Integer> validPawns = new ArrayList<Integer>(pawns.size());
        for (Integer pawn : pawns) {
            if (pawn < sticks && sticks - pawn != 2) {
                validPawns.add(pawn);
            }
        }
        
        // S'il n'y a pas de coup non fatal, on joue le coup fatal
        if(validPawns.isEmpty())
        {
            return 1;
        }
        
        Collections.shuffle(validPawns);
        return validPawns.get(0);
    }

}
