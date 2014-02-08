package com.tanisca.thelaststick.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.analytics.tracking.android.EasyTracker;
import com.tanisca.thelaststick.R;
import com.tanisca.thelaststick.model.GameMode;
import com.tanisca.thelaststick.tool.SavedData;
import com.tanisca.thelaststick.tool.StatManager;

/**
 * Ecran d'accueil
 * 
 * @see SystemUiHider
 */
public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise les singletons
        SavedData.init(this);
        StatManager.init(this);

        setContentView(R.layout.activity_menu);
        managePlayButtons();
        manageOptionButton();
        manageChievosButton();
        manageStatsButton();
        manageRulesButton();
        manageExitButton();

        // AchievementController.getInstance(this).reset();
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    /**
     * Gestion de l'interactivité du bouton New Game
     */
    private void managePlayButtons() {
        Button bt = (Button) findViewById(R.id.mode_classic_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardActivity.GameTypeMode = GameMode.CLASSIC;
                Intent intent = new Intent(MenuActivity.this,
                        MenuPlayerActivity.class);
                startActivity(intent);
            }
        });

        bt = (Button) findViewById(R.id.mode_original_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardActivity.GameTypeMode = GameMode.ORIGINAL;
                Intent intent = new Intent(MenuActivity.this,
                        MenuPlayerActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Gestion de l'interactivité du bouton Option
     */
    private void manageRulesButton() {
        Button bt = (Button) findViewById(R.id.rulesButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,
                        RulesActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Gestion de l'interactivité du bouton Stats
     */
    private void manageStatsButton() {
        Button bt = (Button) findViewById(R.id.statsButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,
                        StatsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Gestion de l'interactivité du bouton Chievos
     */
    private void manageChievosButton() {
        Button bt = (Button) findViewById(R.id.chievosButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,
                        AchievementsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Gestion de l'interactivité du bouton Option
     */
    private void manageOptionButton() {
        Button bt = (Button) findViewById(R.id.optionButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,
                        OptionActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Gestion de l'interactivité du bouton Quitter
     */
    private void manageExitButton() {
        // Button bt = (Button) findViewById(R.id.quitAppButton);
        // bt.setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // moveTaskToBack(true);
        // }
        // });
    }

}
