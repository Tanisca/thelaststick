package com.tanisca.thelaststick;

import java.util.HashSet;

import android.os.Handler;
import android.util.Log;

import com.tanisca.thelaststick.activity.BoardActivity;
import com.tanisca.thelaststick.model.Achievement;
import com.tanisca.thelaststick.model.AchievementController;
import com.tanisca.thelaststick.model.GameMode;
import com.tanisca.thelaststick.model.Player;
import com.tanisca.thelaststick.tool.StatManager;

public class GameForOnePlayer extends Game {

    private final StatManager statManager;
    private final AchievementController achievementController;

	public GameForOnePlayer(BoardActivity activity, GameMode mode, int sticks) {
	    super(activity, sticks, mode, Player.AI);
		this.playerTurn = Player.ONE; 
        this.statManager = StatManager.getInstance();
        this.achievementController = AchievementController.getInstance(activity);
	}

    @Override
    public void playAI() {

        if (this.getPlayerTurn() == Player.AI) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    askIntelligenceToPlay();
                }
            }, 400+Math.round(Math.random()*1500));
        }
    }

    @Override
    protected HashSet<Achievement> manageStats() {
        if(this.isDone())
        {
            HashSet<Achievement> unlocked = this.achievementController.process(this);
            for(Achievement a : unlocked)
            {
                Log.i("GameForOnePlayer", "unlocked: " + a.name());
            }
            if(unlocked.isEmpty())
            {
                Log.i("GameForOnePlayer", "nothing unlocked!");
            }
            this.statManager.incr(this.getAI().getDifficulty().getStatsPlayedGames(this.getMode()));
            if(this.getWinner() == Player.ONE)
            {
                this.statManager.incr(this.getAI().getDifficulty().getStatsWinGames(this.getMode()));
            }
            return unlocked;
        }
        return null;
    }

}
