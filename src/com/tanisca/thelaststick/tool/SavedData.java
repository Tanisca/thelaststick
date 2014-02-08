package com.tanisca.thelaststick.tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tanisca.thelaststick.R;

public class SavedData {

    private Activity          activity;
    private SharedPreferences sharedPrefs;
    private static SavedData  instance;

    private SavedData(Activity activity) {
        this.activity = activity;
        this.sharedPrefs = activity.getSharedPreferences(
                activity.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
    }

    public static SavedData init(Activity activity) {
        instance = new SavedData(activity);
        return instance;
    }

    public static SavedData getInstance(Activity activity) {
        if(instance == null)
        {
            init(activity);
        }
        return instance;
    }

    public void save(int key, int value) {
        Editor editor = this.sharedPrefs.edit();
        editor.putInt(this.activity.getString(key), value);
        editor.commit();
    }

    public void save(String skey, int value) {
        Editor editor = this.sharedPrefs.edit();
        editor.putInt(skey, value);
        editor.commit();
    }

    public int getInt(int key, int defaultValue) {
        return this.sharedPrefs.getInt(this.activity.getString(key),
                defaultValue);
    }

    public int getInt(String key) {
        return this.sharedPrefs.getInt(key, 0);
    }
    
    public String getString(String key) {
        return this.sharedPrefs.getString(key, "undefined");
    }

}
