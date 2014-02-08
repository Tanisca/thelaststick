package com.tanisca.thelaststick.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.tanisca.thelaststick.tool.SavedData;

public class AchievementManager {

    private Activity                                   activity;
    private static AchievementManager                  Instance;
    private HashMap<Achievement, AchievementLocalized> localizedAchievements;
    private ArrayList<Achievement>                     unlockedAchievements;
    private SavedData                                  savedData;
    public final String                                SAVED_DATA_PREFIX = "achievement_score_";

    private AchievementManager(Activity activity) {
        this.savedData = SavedData.getInstance(activity);
        this.activity = activity;
        int achievementsCount = Achievement.values().length;
        this.localizedAchievements = new HashMap<Achievement, AchievementLocalized>(
                achievementsCount);
        this.unlockedAchievements = new ArrayList<Achievement>(
                achievementsCount);
        this.localizeAchievements();
    }

    private void localizeAchievements() {
        for (Achievement achievement : Achievement.values()) {
            int score = this.savedData.getInt(SAVED_DATA_PREFIX
                    + achievement.name());
            String info = this.activity.getString(achievement.getInfos());
            String[] infos = info.split("\\|");
            AchievementLocalized localizedAchievement = new AchievementLocalized(
                    achievement, infos[0], infos[1], score);
            this.localizedAchievements.put(achievement, localizedAchievement);
            if (localizedAchievement.isUnlocked()) {
                this.unlockedAchievements.add(achievement);
            }
        }
    }

    /**
     * Incremente le score de l'achievement<br>
     * Si l'achievement est deja unlock: retourne une liste vide<br>
     * Sinon increment le score<br>
     * Si le nouveau score permet d'unlock l'achievement: unlock l'achievement
     * et retourne une liste composée de l'achievement et eventuellement de sa
     * stack<br>
     * Sinon retourne une liste vide
     * 
     * @param achievement
     * @return
     */
    public HashSet<Achievement> incr(Achievement achievement) {
        HashSet<Achievement> unlocked = new HashSet<Achievement>();
        String key = SAVED_DATA_PREFIX + achievement.name();
        //if (achievement == Achievement.W1EC) {
            Log.i("AManagerXXX", "TAG1("+achievement.name()+"):" + this.savedData.getInt(key));
            if (!this.isAchievementUnlocked(achievement)) {
                //Log.i("AManagerXXX", "TAG2:" + this.savedData.getInt(key));
                this.savedData.save(key, this.savedData.getInt(key) + 1);
                //Log.i("AManagerXXX", "TAG3:" + this.savedData.getInt(key));
                List<Achievement> stack = achievement.getStack();
                for (Achievement sub : stack) {
                    if (!this.isAchievementUnlocked(sub)) {
                        key = SAVED_DATA_PREFIX + sub.name();
                        this.savedData
                                .save(key, this.savedData.getInt(key) + 1);
                        if (this.isAchievementShouldBeUnlocked(sub)) {
                            this.unlockedAchievements.add(sub);
                            unlocked.add(sub);
                        }
                    }
                }

                if (this.isAchievementShouldBeUnlocked(achievement)) {
                    //Log.i("AManagerXXX", "TAG4:" + this.savedData.getInt(key));
                    this.unlockedAchievements.add(achievement);
                    unlocked.add(achievement);
                }
                //Log.i("AManagerXXX", "TAG5:" + this.savedData.getInt(key));
            }
        //}
        return unlocked;
    }

    private boolean isAchievementShouldBeUnlocked(Achievement achievement) {
        String key = SAVED_DATA_PREFIX + achievement.name();
        return this.savedData.getInt(key) >= achievement.getReach();
    }

    public AchievementLocalized getLocalizedAchievement(Achievement achievement) {
        return this.localizedAchievements.get(achievement);
    }

    public boolean isAchievementUnlocked(Achievement achievement) {
        return this.unlockedAchievements.contains(achievement);
    }

    public static AchievementManager getInstance(Activity activity) {
        if (Instance == null) {
            Instance = new AchievementManager(activity);
        }
        return Instance;
    }

    public int getUnlockedAchievementsCount() {
        return this.unlockedAchievements.size();
    }

    public int getAchievementsCount() {
        return Achievement.values().length;
    }

    public void reset(Achievement achievement) {
        String key = SAVED_DATA_PREFIX + achievement.name();
        this.savedData.save(key, 0);
        Log.i("AchievementManager", "Reset de " + achievement.name() + " ("
                + this.savedData.getInt(key) + ")");
        if(isAchievementUnlocked(achievement))
        {
            this.unlockedAchievements.remove(achievement);
            Log.i("AchievementManager", "ResetCombo de " + achievement.name() + " ("
                    + this.savedData.getInt(key) + ")");
        }
    }

}
