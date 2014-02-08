package com.tanisca.thelaststick.model;

import java.util.Arrays;
import java.util.List;

import com.tanisca.thelaststick.R;

/**
 * C = CLASSIC
 * D = PLAY ON DIFFERENT DAYS
 * E = EASY
 * H = HARD
 * N = NORMAL
 * O = ORIGINAL
 * P = PLAY
 * R = WIN IN A ROW
 */
public enum Achievement {

    W1EC(1, R.string.achievement_W1EC),
    W1NC(1, R.string.achievement_W1NC, W1EC),
    W1HC(1, R.string.achievement_W1HC, W1NC, W1EC),
    W1EO(1, R.string.achievement_W1EO),
    W1NO(1, R.string.achievement_W1NO, W1EO),
    W1HO(1, R.string.achievement_W1HO, W1NO, W1EO),
    W10EC(10, R.string.achievement_W10EC),
    W10NC(10, R.string.achievement_W10NC, W10EC),
    W10HC(10, R.string.achievement_W10HC, W10NC, W10EC),
    W10EO(10, R.string.achievement_W10EO),
    W10NO(10, R.string.achievement_W10NO, W10EO),
    W10HO(10, R.string.achievement_W10HO, W10NO, W10EO),
    W25EC(25, R.string.achievement_W25EC),
    W25NC(25, R.string.achievement_W25NC, W25EC),
    W25HC(25, R.string.achievement_W25HC, W25NC, W25EC),
    W25EO(25, R.string.achievement_W25EO),
    W25NO(25, R.string.achievement_W25NO, W25EO),
    W25HO(25, R.string.achievement_W25HO, W25NO, W25EO),
    P25(25, R.string.achievement_P25),
    P50(50, R.string.achievement_P50),
    P100(100, R.string.achievement_P100);
    /*R5HC(5, R.string.achievement_R5HC),
    R3HO(3, R.string.achievement_R3HO),
    D3(3, R.string.achievement_D3),
    D7(3, R.string.achievement_D7),
    _LOSS(1, R.string.achievement__LOSS);*/

    /**
     * Le score à atteindre
     */
    private int reach;
    
    /**
     * Ressource title|description
     */
    private int infos;
    
    /**
     * Les achievements qui sont cumulés dans celui-ci
     */
    private Achievement[] stack;

    private Achievement(int reach, int infos, Achievement... stack) {
        this.reach = reach;
        this.infos = infos;
        this.stack = stack;
    }

    public int getReach()
    {
        return this.reach;
    }
    
    public List<Achievement> getStack()
    {
        return Arrays.asList(this.stack);
    }

    public int getInfos() {
        return this.infos;
    }
}
