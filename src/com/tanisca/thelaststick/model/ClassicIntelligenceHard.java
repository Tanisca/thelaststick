package com.tanisca.thelaststick.model;

public class ClassicIntelligenceHard extends AbstractClassicIntelligence {

    public ClassicIntelligenceHard()
    {
        this.difficulty = Difficulty.HARD;
    }
    
    public int getMove()
	{
		int sticks = this.getGame().getRemainingSticks();
		
		if (sticks < 2) {
			return 1;
		}
		
		int move = (sticks - 1) % 4;
		if (move == 0) {
			return this.getRandomMove(sticks);
		}
		return move;
	}

}
