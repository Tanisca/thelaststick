package com.tanisca.thelaststick;

import java.util.HashSet;

import com.tanisca.thelaststick.activity.BoardActivity;
import com.tanisca.thelaststick.model.Achievement;
import com.tanisca.thelaststick.model.GameMode;
import com.tanisca.thelaststick.model.Player;

public class GameForTwoPlayers extends Game {

	public GameForTwoPlayers(BoardActivity activity, GameMode mode, int sticks) {
	    super(activity, sticks, mode, Player.TWO);
		this.playerTurn = Player.BOTH; 
	}

    @Override
    public void playAI() {
        // Ne fait rien dans cette implementation
        // Pas d'IA en mode 2 joueurs
    }

    @Override
    protected HashSet<Achievement> manageStats() {
        // Ne fait rien dans cette implementation
        // Pas d'achievement multijoueur pour linstant
        // Pas de stats en mode 2 joueurs
        return null;
    }	

}
