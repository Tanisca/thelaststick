package com.tanisca.thelaststick.model;

import java.util.HashSet;

import android.app.Activity;

import com.tanisca.thelaststick.GameForOnePlayer;

public class AchievementController {

    private Activity                     activity;
    private static AchievementController Instance;
    private AchievementManager           manager;

    private AchievementController(Activity activity) {
        this.activity = activity;
        this.manager = AchievementManager.getInstance(activity);
    }

    public static AchievementController getInstance(Activity activity) {
        if (Instance == null) {
            Instance = new AchievementController(activity);
        }
        return Instance;
    }

    public HashSet<Achievement> process(GameForOnePlayer game) {
        boolean classic = game.isClassicGame();
        boolean win = game.getWinner() == Player.ONE;
        String difficulty = game.getAI().getDifficulty().name().substring(0, 1);
        // String date = new SimpleDateFormat("dmy", Locale.US).format(new
        // Date());

        HashSet<Achievement> unlocked = new HashSet<Achievement>();

        // Gestion des P
        for (Achievement achievement : Achievement.values()) {
            if (achievement.name().startsWith("P")) {
                unlocked.addAll(this.manager.incr(achievement));
            }
        }

        // Gestion des W
        if (classic && win) {
            String name;
            for (Achievement achievement : Achievement.values()) {
                name = achievement.name();
                if (name.startsWith("W") && name.endsWith(difficulty + "C")) {
                    unlocked.addAll(this.manager.incr(achievement));
                }
            }
        }
        else if (!classic && win) {
            String name;
            for (Achievement achievement : Achievement.values()) {
                name = achievement.name();
                if (name.startsWith("W") && name.endsWith(difficulty + "O")) {
                    unlocked.addAll(this.manager.incr(achievement));
                }
            }
        }

        // Gestion des D

        // Gestion des R

        // Gestion des _

        return unlocked;
    }

    public void reset() {
        for (Achievement achievement : Achievement.values()) {
            this.manager.reset(achievement);
        }
    }

}
