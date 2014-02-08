package com.tanisca.thelaststick.model;

import com.tanisca.thelaststick.Game;
import com.tanisca.thelaststick.R;

public enum Difficulty {

    EASY(0, ClassicIntelligenceEasy.class, OriginalIntelligenceEasy.class,
            null, R.string.stats_classic_easy_games,
            R.string.stats_classic_easy_wins,
            R.string.stats_original_easy_games,
            R.string.stats_original_easy_wins),
    NORMAL(1,
            ClassicIntelligenceNormal.class, OriginalIntelligenceNormal.class,
            EASY, R.string.stats_classic_normal_games,
            R.string.stats_classic_normal_wins,
            R.string.stats_original_normal_games,
            R.string.stats_original_normal_wins),
    HARD(2,
            ClassicIntelligenceHard.class, OriginalIntelligenceHard.class,
            NORMAL, R.string.stats_classic_hard_games,
            R.string.stats_classic_hard_wins,
            R.string.stats_original_hard_games,
            R.string.stats_original_hard_wins);

    private final int                                   value;
    private final Class<? extends AbstractIntelligence> classicClass;
    private final Class<? extends AbstractIntelligence> originalClass;
    private final Difficulty                            stackDifficulty;
    private final int                                   statsClassicPlayedGames;
    private final int                                   statsClassicWinGames;
    private final int                                   statsOriginalPlayedGames;
    private final int                                   statsOriginalWinGames;

    Difficulty(int value, Class<? extends AbstractIntelligence> classicClass,
            Class<? extends AbstractIntelligence> originalClass,
            Difficulty statsStack, int statsClassicPlayedGames,
            int statsClassicWinGames, int statsOriginalPlayedGames,
            int statsOriginalWinGames) {
        this.value = value;
        this.classicClass = classicClass;;
        this.originalClass = originalClass;
        this.stackDifficulty = statsStack;
        this.statsClassicPlayedGames = statsClassicPlayedGames;
        this.statsClassicWinGames = statsClassicWinGames;
        this.statsOriginalPlayedGames = statsOriginalPlayedGames;
        this.statsOriginalWinGames = statsOriginalWinGames;
    }

    public int getValue() {
        return this.value;
    }

    public static Difficulty fromValue(int value) {
        for (Difficulty item : values()) {
            if (value == item.value) {
                return item;
            }
        }
        return null;
    }

    public AbstractIntelligence getIntelligence(Game game) {
        Class<? extends AbstractIntelligence> clazz = game.getMode() == GameMode.CLASSIC ? this.classicClass
                : this.originalClass;
        try {
            AbstractIntelligence intelligence = clazz.newInstance();
            intelligence.setGame(game);
            return intelligence;
        }
        catch (InstantiationException e) {
            return null;
        }
        catch (IllegalAccessException e) {
            return null;
        }
    }

    public Difficulty getStackDifficulty() {
        return stackDifficulty;
    }

    public int getStatsPlayedGames(GameMode mode) {
        return mode == GameMode.ORIGINAL ? statsOriginalPlayedGames
                : statsClassicPlayedGames;
    }

    public int getStatsWinGames(GameMode mode) {
        return mode == GameMode.ORIGINAL ? statsOriginalWinGames
                : statsClassicWinGames;
    }
}
