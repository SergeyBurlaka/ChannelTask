package com.example.kostya.channeltask.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kostya on 03.11.16.
 */

public class PrefManager {
    private static PrefManager sPrefManager;
    private static final String KEY_UNIQUE_USER_EMAIL = "KEY_UNIQUE_USER_EMAIL";

    private PrefManager() {

    }

    public  static PrefManager getPrefManager() {
        if(sPrefManager == null) {
            sPrefManager = new PrefManager();
        }
        return sPrefManager;
    }

    public void setUniqueUser(String uniqueUserMail, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences
                .edit()
                .putString(KEY_UNIQUE_USER_EMAIL, uniqueUserMail)
                .apply();
    }

    public String getUniqueUser(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(KEY_UNIQUE_USER_EMAIL, "");
    }

}
