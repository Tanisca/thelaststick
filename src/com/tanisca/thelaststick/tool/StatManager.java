package com.tanisca.thelaststick.tool;

import com.tanisca.thelaststick.model.Difficulty;
import com.tanisca.thelaststick.model.GameMode;

import android.app.Activity;

public class StatManager {

    private static StatManager instance;
    private Activity           activity;

    private StatManager(Activity activity) {
        this.activity = activity;
    }

    public static StatManager init(Activity activity) {
        instance = new StatManager(activity);
        return instance;
    }

    public static StatManager getInstance() {
        return getInstance(false);
    }

    public static StatManager getInstance(boolean forceInit) {
        if (instance == null) {
            instance = new StatManager(null);
        }
        return instance;
    }

    public void incr(int key) {
        String skey = this.activity.getResources().getString(key);
        SavedData.getInstance(this.activity).save(skey, this.get(skey) + 1);
    }

    public void reset(int key) {
        String skey = this.activity.getResources().getString(key);
        SavedData.getInstance(this.activity).save(skey, 0);
    }

    public int get(String skey) {
        return SavedData.getInstance(this.activity).getInt(skey);
    }

    public int get(int key) {
        return SavedData.getInstance(this.activity).getInt(
                this.activity.getResources().getString(key));
    }

    public void reset() {
        for (GameMode mode : GameMode.values()) {
            for (Difficulty d : Difficulty.values()) {
                this.reset(d.getStatsPlayedGames(mode));
                this.reset(d.getStatsWinGames(mode));
            }
        }
    }

}
