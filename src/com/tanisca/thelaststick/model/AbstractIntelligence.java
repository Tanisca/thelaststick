package com.tanisca.thelaststick.model;

import com.tanisca.thelaststick.Game;
import com.tanisca.thelaststick.R;
import com.tanisca.thelaststick.tool.SavedData;

public abstract class AbstractIntelligence {

	protected Game game;
	protected Difficulty difficulty;
	
	public static AbstractIntelligence getInstance(Game game)
	{
		Difficulty difficulty = Difficulty.fromValue(SavedData.getInstance(game.getActivity())
				.getInt(R.string.saved_difficulty, Difficulty.EASY.getValue()));
		
		return difficulty.getIntelligence(game);
	}

	public void play() {
		int move = this.getMove();
		this.game.removeSticks(Player.AI, move);
	}

	public abstract int getMove();

	public abstract int getRandomMove(int sticks);

	public Game getGame() {
        return this.game;
    }
    
    public Difficulty getDifficulty() {
        return this.difficulty;
    }

	public void setGame(Game game) {
		this.game = game;
	}

}
