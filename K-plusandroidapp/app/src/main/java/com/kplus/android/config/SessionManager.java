package com.kplus.android.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.kplus.android.activities.LoginActivity;

/**
 * Created by Vasco on 21-1-2015.
 */
public class SessionManager
{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private int PRIVATE_MODE = 1;

    private String PREF_NAME = "com.kplus.android.v1";
    private String IS_LOGIN = "IsLoggedIn";
    private String KEY_FIRSTNAME = "firstname";
    private String KEY_LASTNAME = "lastname";
    private String KEY_EMAIL = "email";
    private String KEY_TOKEN = "token";

    public SessionManager(Context context)
    {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email, String firstname, String lastname)
    {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_FIRSTNAME, firstname);
        editor.putString(KEY_LASTNAME, lastname);
        editor.commit();
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

    public boolean isLoggedIn()             { return pref.getBoolean(IS_LOGIN, false); }

    public String getEmail()                { return pref.getString(KEY_EMAIL, null); }
    public String getFirstname()            { return pref.getString(KEY_FIRSTNAME, null); }
    public String getLastname()             { return pref.getString(KEY_LASTNAME, null); }
    public String getToken()                { return pref.getString(KEY_TOKEN, null); }

    public void setToken(String s)          { editor.putString(KEY_TOKEN, s); editor.commit(); }
}