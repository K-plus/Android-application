package com.kplus.android.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.kplus.android.activities.LoginActivity;

import java.io.Serializable;

/**
 * Created by Vasco on 21-1-2015.
 */
public class SessionManager implements Serializable
{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public static int PRIVATE_MODE = 1;

    public static String PREF_NAME = "com.kplus.android.v1";
    private String IS_LOGIN = "IsLoggedIn";
    private String KEY_NAME = "name";
    private String KEY_EMAIL = "email";
    private String KEY_PASSWORD = "pass";

    public SessionManager(Context context)
    {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email, String name, String pass)
    {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PASSWORD, pass);
        editor.commit();

        APIClient.setSession(this);
    }

    public void checkLogin()
    {
        if (!this.isLoggedIn())
        {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public void logoutUser()
    {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public boolean isLoggedIn() {
        boolean isLoggedIn = pref.getBoolean(IS_LOGIN, false);

        if(isLoggedIn)
            APIClient.setSession(this);

        return isLoggedIn;
    }

    public String getEmail()                { return pref.getString(KEY_EMAIL, null); }
    public String getName()                 { return pref.getString(KEY_NAME, null); }
    public String getPass()                 { return pref.getString(KEY_PASSWORD, null); }
}