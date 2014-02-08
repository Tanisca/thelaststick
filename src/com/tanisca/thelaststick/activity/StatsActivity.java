package com.tanisca.thelaststick.activity;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.analytics.tracking.android.EasyTracker;
import com.tanisca.thelaststick.R;
import com.tanisca.thelaststick.model.Difficulty;
import com.tanisca.thelaststick.model.GameMode;
import com.tanisca.thelaststick.tool.StatManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class StatsActivity extends Activity {

    // private static final String TAG = "StatsActivity";
    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        this.manageMenuButton();
        this.fillStatsTable(GameMode.CLASSIC, R.id.statsTableClassic);
        this.fillStatsTable(GameMode.ORIGINAL, R.id.statsTableOriginal);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        View gestureOverlayView = findViewById(R.id.swipePanel);

        final GestureDetector gestureDetector = new GestureDetector(this,
                new CustomGestureDetector());
        gestureOverlayView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    private Activity getActivity() {
        return this;
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

    @SuppressWarnings("deprecation")
    private void fillStatsTable(GameMode mode, int statsTable) {
        final StatManager sm = StatManager.getInstance();

        HashMap<Difficulty, Integer> playedGames = new HashMap<Difficulty, Integer>();
        HashMap<Difficulty, Integer> winGames = new HashMap<Difficulty, Integer>();
        HashMap<Difficulty, Integer> lossGames = new HashMap<Difficulty, Integer>();
        Integer apg = 0;
        Integer awg = 0;
        for (Difficulty d : Difficulty.values()) {
            int pg = sm.get(d.getStatsPlayedGames(mode));
            int wg = sm.get(d.getStatsWinGames(mode));
            int lg = pg - wg;
            apg += pg;
            awg += wg;
            playedGames.put(d, pg);
            winGames.put(d, wg);
            lossGames.put(d, lg);
        }

        int rowIndex = 0;
        String[] difficultyLabels = getResources().getStringArray(
                R.array.intelligenceItems);
        TableLayout table = (TableLayout) findViewById(statsTable);
        for (String label : difficultyLabels) {
            TableRow row = new TableRow(this);
            table.addView(row, ++rowIndex);

            addHeaderCell(label, row);
            addValueCell(playedGames, null, rowIndex, row);
            addValueCell(winGames, playedGames, rowIndex, row);
            addValueCell(lossGames, playedGames, rowIndex, row);
        }

        // Total line
        TableRow row = new TableRow(this);
        table.addView(row, ++rowIndex);

        TextView tv = new TextView(this);
        tv.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.stats_cell));
        tv.setText(R.string.statstable_total);
        tv.setTextAppearance(this, R.style.StatsTextStyle);
        row.addView(tv);

        addTotalCell(apg, null, row);
        addTotalCell(awg, apg, row);
        addTotalCell(Integer.valueOf(apg - awg), apg, row);

    }

    @SuppressWarnings("deprecation")
    private void addHeaderCell(String label, TableRow row) {
        TextView tv = new TextView(this);
        tv.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.stats_cell));
        tv.setTextAppearance(this, R.style.StatsTextStyle);
        tv.setText(label);
        row.addView(tv);
    }

    @SuppressWarnings("deprecation")
    private void addValueCell(HashMap<Difficulty, Integer> games,
            HashMap<Difficulty, Integer> fullGames, int rowIndex, TableRow row) {
        TextView tv;
        tv = new TextView(this);
        tv.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.stats_value_cell));
        tv.setGravity(Gravity.CENTER);
        tv.setTextAppearance(this, R.style.StatsTextStyle);
        tv.setTextColor(getResources().getColor(R.color.black));
        int cellGames = games.get(Difficulty.fromValue(rowIndex - 1));
        int totalGames = 0;
        if (fullGames != null) {
            totalGames = fullGames.get(Difficulty.fromValue(rowIndex - 1));
        }

        if (totalGames != 0) {
            tv.setText(cellGames + " ("
                    + Math.round(cellGames * 100 / (float) totalGames) + "%)");
        }
        else {
            tv.setText(cellGames + "");
        }
        row.addView(tv);
    }

    @SuppressWarnings("deprecation")
    private void addTotalCell(Integer cellValue, Integer totalValue,
            TableRow row) {
        TextView tv;
        tv = new TextView(this);
        tv.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.stats_cell));
        tv.setGravity(Gravity.CENTER);
        tv.setTextAppearance(this, R.style.StatsTextStyle);
        if (totalValue == null || totalValue == 0) {
            tv.setText(cellValue.toString());
        }
        else {
            tv.setText(cellValue + " ("
                    + Math.round(cellValue * 100 / (float) totalValue) + "%)");
        }
        row.addView(tv);
    }

    private void showNext() {
        viewFlipper.setInAnimation(getActivity(),
                R.anim.in_from_right);
        viewFlipper.setOutAnimation(getActivity(),
                R.anim.out_to_left);
        viewFlipper.showNext();
    }

    /**
     * Gestion de l'interactivité du bouton Menu
     */
    private void manageMenuButton() {
        View bt = findViewById(R.id.menuButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        bt = findViewById(R.id.swipePanel);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNext();
            }
        });

        final StatsActivity self = this;
        bt = findViewById(R.id.resetStatButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(self)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.reset_stats)
                        .setMessage(R.string.reset_stats_sure)
                        .setPositiveButton(R.string.reset_stats,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which) {

                                        StatManager.getInstance().reset();
                                        finish();
                                        startActivity(getIntent());
                                    }

                                }).setNegativeButton(R.string.cancel, null)
                        .show();

            }
        });
    }

    private class CustomGestureDetector extends
            GestureDetector.SimpleOnGestureListener {
        private static final String TAG                      = "StatsActivity.CustomGestureDetector";

        private static final int    SWIPE_MIN_DISTANCE       = 120;
        private static final int    SWIPE_MAX_OFF_PATH       = 250;
        private static final int    SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            Log.d(TAG, "############################## ONFLING " + velocityX);
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    showNext();
                }
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    viewFlipper.setInAnimation(getActivity(),
                            R.anim.in_from_left);
                    viewFlipper.setOutAnimation(getActivity(),
                            R.anim.out_to_right);
                    viewFlipper.showPrevious();
                }
            }
            catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }

            return true;
        }
    }

}
