package com.tanisca.thelaststick.model;

public class ClassicIntelligenceNormal extends AbstractClassicIntelligence {

    public ClassicIntelligenceNormal()
    {
        this.difficulty = Difficulty.NORMAL;
    }
    
    public int getMove()
	{
		int sticks = this.getGame().getRemainingSticks();
		if (sticks < 3) {
			return 1;
		}
		
		if (sticks < 9) {
			int move = (sticks - 1) % 4;
			if (move == 0) {
				return this.getRandomMove(sticks);
			}
			return move;
		}
		
		return this.getRandomMove(sticks);
	}
}
