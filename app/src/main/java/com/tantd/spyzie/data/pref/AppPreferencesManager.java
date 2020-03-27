package com.tantd.spyzie.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.tantd.spyzie.SpyzieApplication;

/**
 * Created by tantd on 2/7/2020.
 */
public class AppPreferencesManager {

    private static final String PREFS_FILE_NAME = "app_prefs";

    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";

    private final SharedPreferences mPrefs;

    private static AppPreferencesManager instance;

    public static AppPreferencesManager getInstance() {
        if (instance == null) {
            instance = new AppPreferencesManager();
        }
        return instance;
    }

    private AppPreferencesManager() {
        mPrefs = SpyzieApplication.getInstance().getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, "");
    }
}
