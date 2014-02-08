package com.tanisca.thelaststick.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.tanisca.thelaststick.R;
import com.tanisca.thelaststick.R.drawable;
import com.tanisca.thelaststick.model.Achievement;
import com.tanisca.thelaststick.model.AchievementLocalized;
import com.tanisca.thelaststick.model.AchievementManager;

/**
 * Ecrans des achievements
 * 
 * @see SystemUiHider
 */
public class AchievementsActivity extends Activity {

    private AchievementManager manager;
    private Drawable           lockedTextFrameBackground;
    private Drawable           unlockedTextFrameBackground;
    private Context            context;
    private int                textColorWhite;
    private int                textColorDisabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        this.manager = AchievementManager.getInstance(this);
        this.buildProgress();
        this.buildList();
    }

    private void buildProgress() {
        TextView progress = (TextView) findViewById(R.id.achievement_progress);
        String text = progress.getText().toString();
        int unlocked = this.manager.getUnlockedAchievementsCount();
        int total = this.manager.getAchievementsCount();
        int ratio = Math.round(unlocked * 100 / total);
        text += " (" + unlocked + "/" + total + ") " + ratio + "%";
        progress.setText(text);
    }

    private void buildList() {

        // Charge les ressources partagées
        this.lockedTextFrameBackground = getResources().getDrawable(
                drawable.locked_achievement);
        this.unlockedTextFrameBackground = getResources().getDrawable(
                drawable.unlocked_achievement);
        this.context = getApplicationContext();
        this.textColorWhite = getResources().getColor(R.color.white);
        this.textColorDisabled = getResources().getColor(
                R.color.disabledButtonText);
        LinearLayout container = (LinearLayout) findViewById(R.id.achievement_container);

        // Construction de la liste des achievements
        int i = -1;
        LinearLayout row = null;
        for (Achievement achievement : Achievement.values()) {
            if (++i % 2 == 0) {
                row = this.buildLeftCell(achievement, container);
            }
            else {
                this.buildRightCell(achievement, container, row);
            }
        }
        if (i % 2 == 0) {
            this.buildRightCell(null, container, row);
        }
    }

    @SuppressWarnings("deprecation")
    private LinearLayout buildLeftCell(Achievement achievement,
            LinearLayout container) {

        LinearLayout row = new LinearLayout(context);
        LayoutParams rowParams = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0, 0, 0, 5);
        row.setLayoutParams(rowParams);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setBaselineAligned(false);
        container.addView(row);

        RelativeLayout cell = new RelativeLayout(context);
        LayoutParams cellParams = new LayoutParams(0,
                LayoutParams.WRAP_CONTENT, 1.0f);
        cellParams.setMargins(0, 0, 5, 0);
        cell.setLayoutParams(cellParams);
        cell.setGravity(Gravity.CENTER_HORIZONTAL);
        if (manager.isAchievementUnlocked(achievement)) {
            cell.setBackgroundDrawable(this.unlockedTextFrameBackground);
        }
        else {
            cell.setBackgroundDrawable(this.lockedTextFrameBackground);
        }
        row.addView(cell);

        buildInnerCell(achievement, cell);

        return row;
    }

    @SuppressWarnings("deprecation")
    private void buildInnerCell(Achievement achievement, RelativeLayout cell) {
        AchievementLocalized localizedAchievement = this.manager
                .getLocalizedAchievement(achievement);

        TextView title = new TextView(context);
        title.setId(achievement.ordinal() + 1);
        LayoutParams titleParams = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(titleParams);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setTextAppearance(context, android.R.style.TextAppearance_Large);
        title.setShadowLayer(1.5f, 1f, 1f, R.color.black);
        title.setText(localizedAchievement.getTitle());
        title.setTextColor(textColorDisabled);
        title.setLines(1);
        cell.addView(title);

        TextView description = new TextView(context);
        RelativeLayout.LayoutParams descParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        descParams.addRule(RelativeLayout.BELOW, title.getId());
        description.setLayoutParams(descParams);
        description.setGravity(Gravity.CENTER_HORIZONTAL);
        description.setTextAppearance(context,
                android.R.style.TextAppearance_Small);
        description.setShadowLayer(1.5f, 1f, 1f, R.color.black);
        String text = localizedAchievement.getDescription();
        description.setText(text);
        description.setLines(2);
        description.setTextColor(textColorDisabled);
        if (this.manager.isAchievementUnlocked(achievement)) {
            title.setTextColor(textColorWhite);
            description.setTextColor(textColorWhite);
        }
        cell.addView(description, descParams);
    }

    @SuppressWarnings({ "deprecation" })
    private void buildRightCell(Achievement achievement, View container,
            LinearLayout row) {
        RelativeLayout cell = new RelativeLayout(getApplicationContext());
        LayoutParams cellParams = new LayoutParams(0,
                LayoutParams.WRAP_CONTENT, 1.0f);
        cellParams.setMargins(5, 0, 0, 0);
        cell.setLayoutParams(cellParams);
        cell.setGravity(Gravity.CENTER_HORIZONTAL);
        cell.setBackgroundDrawable(this.lockedTextFrameBackground);
        if (this.manager.isAchievementUnlocked(achievement)) {
            cell.setBackgroundDrawable(this.unlockedTextFrameBackground);
        }

        if (achievement == null) {
            cell.setVisibility(View.INVISIBLE);
        }
        else {
            buildInnerCell(achievement, cell);
        }

        row.addView(cell);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("AchievementsActivity", "restart activity");
        this.onCreate(null);
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
}
