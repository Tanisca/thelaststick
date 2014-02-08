package com.tanisca.thelaststick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import android.app.Activity;

import com.tanisca.thelaststick.activity.BoardActivity;
import com.tanisca.thelaststick.model.AbstractIntelligence;
import com.tanisca.thelaststick.model.Achievement;
import com.tanisca.thelaststick.model.GameMode;
import com.tanisca.thelaststick.model.Player;

public abstract class Game {

    // private static final String TAG = "Game";
    private BoardActivity                         activity;
    private int                                   initialSticks;
    private int                                   remainingSticks;
    private HashMap<Player, Integer>              lastMoves;
    private Player                                player2;
    private boolean                               done;
    private AbstractIntelligence                  ai;
    protected Player                              playerTurn;
    protected HashMap<Player, ArrayList<Integer>> pawns;
    private GameMode                              mode;
    private HashSet<Achievement>                  unlockedAchievements;

    public Game(BoardActivity activity, int sticks, GameMode mode,
            Player player2) {
        this.activity = activity;
        this.remainingSticks = sticks;
        this.initialSticks = sticks;
        this.player2 = player2;
        this.lastMoves = new HashMap<Player, Integer>();
        this.lastMoves.put(Player.ONE, 0);
        this.lastMoves.put(this.player2, 0);
        this.done = false;
        this.mode = mode;
        this.playerTurn = Player.BOTH;
        this.initializePawns();
        this.ai = AbstractIntelligence.getInstance(this);
    }

    private void initializePawns() {
        ArrayList<Player> players = new ArrayList<Player>(2);
        players.add(Player.ONE);
        players.add(this.player2);

        this.pawns = new HashMap<Player, ArrayList<Integer>>();
        for (Player player : players) {

            HashSet<Integer> playerPawns = new HashSet<Integer>(3);

            // Ajout des pions fixes (mode classique et original)
            for (int pawn : this.mode.getFixedPawns()) {
                playerPawns.add(pawn);
            }

            // Ajout des pions à determiner (mode original seulement)
            int deteminateCount = this.mode.getPawnsToDetermine();
            if (deteminateCount > 0) {
                Collections.shuffle(this.mode.getAvailablePawns());
                for (int i = 0; i < deteminateCount; ++i) {
                    playerPawns.add(this.mode.getAvailablePawns().get(i));
                }
            }

            // Stocke les pions
            ArrayList<Integer> sortedPlayerPawns = new ArrayList<Integer>();
            sortedPlayerPawns.addAll(playerPawns);
            Collections.sort(sortedPlayerPawns);
            this.pawns.put(player, sortedPlayerPawns);
        }

    }

    public int getLastMove(Player player) {
        return this.lastMoves.get(player);
    }

    public Player getPlayer2() {
        return this.player2;
    }

    public Player getWinner() {
        if (this.done) {
            return this.getNextPlayer();
        }
        return null;
    }

    public Player getNextPlayer() {
        return (this.playerTurn == Player.ONE) ? this.player2 : Player.ONE;
    }

    public boolean isOnePlayerGame() {
        return this.player2 == Player.AI;
    }

    public boolean isTwoPlayerGame() {
        return this.player2 == Player.TWO;
    }

    public boolean removeSticks(Player player, int sticks) {

        if (this.isIllegalMove(player, sticks)) {
            return false;
        }

        // Force playerTurn when BOTH is on
        this.playerTurn = player;

        // Save the last move
        this.lastMoves.put(player, sticks);

        // Apply the move to the board
        this.remainingSticks -= sticks;

        // Is the game over?
        if (this.remainingSticks == 0) {
            this.done = true;
            this.unlockedAchievements = this.manageStats();
        }
        // Not over yet? Next please!
        else {
            this.playerTurn = getNextPlayer();
        }

        return true;
    }

    private boolean isIllegalMove(Player player, int sticks) {
        if (this.done) {
            return true;
        }

        if (!player.canPlay(this.playerTurn)) {
            return true;
        }

        if (sticks > this.remainingSticks) {
            return true;
        }
        return false;
    }

    public void askIntelligenceToPlay() {
        this.ai.play();
        this.activity.refreshUI();
    }

    public int getRemainingSticks() {
        return this.remainingSticks;
    }

    public int getInitialStickCount() {
        return this.initialSticks;
    }

    public boolean isDone() {
        return this.done;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public AbstractIntelligence getAI() {
        return this.ai;
    }

    public HashMap<Player, ArrayList<Integer>> getPawns() {
        return pawns;
    }

    protected abstract HashSet<Achievement> manageStats();

    public abstract void playAI();

    public boolean isClassicGame() {
        return this.mode == GameMode.CLASSIC;
    }

    public GameMode getMode() {
        return mode;
    }

    public HashSet<Achievement> getUnlockedAchievements() {
        return unlockedAchievements;
    }

}
