package com.tanisca.thelaststick.model;


public class ClassicIntelligenceEasy extends AbstractClassicIntelligence {

    public ClassicIntelligenceEasy()
    {
        this.difficulty = Difficulty.EASY;
    }
    
    public int getMove()
	{
		int sticks = this.getGame().getRemainingSticks();
		if (sticks < 3) {
			return 1;
		}
		
		if (sticks < 5) {
			return sticks - 1;
		}
		
		return this.getRandomMove(sticks);
	}
}
