package com.tanisca.thelaststick.model;

import java.util.ArrayList;

public class OriginalIntelligenceEasy extends AbstractOriginalIntelligence {

    public OriginalIntelligenceEasy()
    {
        this.difficulty = Difficulty.EASY;
    }
    
    /**
     * Si un coup est gagnant parmi ses pions, il le joue.
     */
    public int getMove()
	{
        int sticks = this.getGame().getRemainingSticks();
        if (sticks < 3) {
            return 1;
        }

        ArrayList<Integer> pawns = this.getGame().getPawns().get(Player.AI);
        for (Integer pawn : pawns) {
            if (sticks - pawn == 1) {
                return pawn;
            }
        }
        
        return this.getRandomMove(sticks);
	}
}
