package com.tanisca.thelaststick.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.google.analytics.tracking.android.EasyTracker;
import com.tanisca.thelaststick.R;
import com.tanisca.thelaststick.model.Difficulty;
import com.tanisca.thelaststick.tool.SavedData;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class OptionActivity extends Activity {

	// private static final String TAG = "OptionActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option);
		this.manageSaveButton();
		this.manageCancelButton();

		Difficulty difficulty = Difficulty.fromValue(SavedData.getInstance(this)
				.getInt(R.string.saved_difficulty, Difficulty.EASY.getValue()));

		Spinner spinner = (Spinner) findViewById(R.id.spinnerDifficulty);
		spinner.setSelection(difficulty.getValue());
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
	private void manageSaveButton() {
	    final Activity activity = this;
		Button bt = (Button) findViewById(R.id.saveButton);
		bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Spinner spinner = (Spinner) findViewById(R.id.spinnerDifficulty);
				Difficulty difficulty = Difficulty.fromValue(spinner
						.getSelectedItemPosition());

				SavedData.getInstance(activity).save(R.string.saved_difficulty,
						difficulty.getValue());

				finish();
			}
		});
	}

	/**
	 * Gestion de l'interactivité du bouton Option
	 */
	private void manageCancelButton() {
		Button bt = (Button) findViewById(R.id.cancelButton);
		bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
