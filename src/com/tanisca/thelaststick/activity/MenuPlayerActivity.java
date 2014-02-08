package com.tanisca.thelaststick.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.analytics.tracking.android.EasyTracker;
import com.tanisca.thelaststick.R;
import com.tanisca.thelaststick.tool.SavedData;
import com.tanisca.thelaststick.tool.StatManager;

/**
 * Menu pour choisir le nombre de joueurs: 1p vs. 1p / 1p vs. CPU
 * 
 * @see SystemUiHider
 */
public class MenuPlayerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialise les singletons
		SavedData.init(this);
		StatManager.init(this);

		setContentView(R.layout.activity_menu_player);
		manageAIButton();
        managePlayerButton();
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
	private void manageAIButton() {
		Button bt = (Button) findViewById(R.id.AIButton);
		bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                BoardActivity.TwoPlayerMode = false;
				Intent intent = new Intent(MenuPlayerActivity.this,
						BoardActivity.class);
				finish();
				startActivity(intent);
			}
		});
	}

	/**
	 * Gestion de l'interactivité du bouton Option
	 */
	private void managePlayerButton() {
		Button bt = (Button) findViewById(R.id.PlayerButton);
		bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                BoardActivity.TwoPlayerMode = true;
                Intent intent = new Intent(MenuPlayerActivity.this,
                        BoardActivity.class);
                finish();
                startActivity(intent);
			}
		});
	}

}
