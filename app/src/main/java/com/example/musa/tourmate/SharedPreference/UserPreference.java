package com.example.musa.tourmate.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Musa on 5/18/2018.
 */

public class UserPreference {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String IS_CELCIOUS = "isCelcious";

    public UserPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void setLogInStatus(boolean status)
    {
        editor.putBoolean(IS_LOGGED_IN,status);
        editor.commit();
    }

    public boolean getLogInStatus()
    {
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }

    public void setTempUnit(boolean status)
    {
        editor.putBoolean(IS_CELCIOUS,status);
        editor.commit();
    }

    public boolean getTempUnit()
    {
        return sharedPreferences.getBoolean(IS_CELCIOUS,false);
    }
}
