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
    private static final String KEY_LAST_SESSION_NUMBER = "KEY_LAST_SESSION_NUMBER";

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

    public void setSessionNumber(int sessionNumber, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences
                .edit()
                .putInt(KEY_LAST_SESSION_NUMBER, sessionNumber)
                .apply();
    }

    public int getLastSessionNumber(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(KEY_LAST_SESSION_NUMBER, 0);
    }

}
