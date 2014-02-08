package com.tanisca.thelaststick.model;

public class AchievementLocalized {

    private Achievement achievement;
    private String title;
    private String description;
    private int score;
    
    public AchievementLocalized(Achievement achievement, String title,
            String description, int score) {
        this.achievement = achievement;
        this.title = title;
        this.description = description;
        this.score = score;
    }
    
    public Achievement getAchievement() {
        return achievement;
    }
    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }   
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    
    public boolean isUnlocked()
    {
        return this.score >= this.achievement.getReach();
    }
    
    @Override
    public String toString() {
        return "AchievementLocalized[" + this.title + "]";
    }
    
}
