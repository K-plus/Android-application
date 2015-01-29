package com.kplus.android.config;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.kenny.snackbar.SnackBar;
import com.kenny.snackbar.SnackBarItem;
import com.kplus.android.k_plusandroidapp.R;

/**
 * Created by Vasco on 21-1-2015.
 */
public class BaseFunctions
{
    public static void Log(String TAG, String message)
    {
        if (Variables.DEBUG)
        {
            Log.d(TAG, message);
        }
    }

    public static SnackBarItem getErrorSnackBar(Context context, String errorMessage)
    {
        return new SnackBarItem.Builder()
                .setMessage(errorMessage)
                .setActionMessage(context.getResources().getString(R.string.snackbar_error))
                .setActionMessageColor(context.getResources().getColor(R.color.Tomato))
                .setDuration(3000)
                .build();
    }

    public static void handleException(final Activity activity, Exception exception)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                SnackBar.show(activity, getErrorSnackBar(activity, "Een fout heeft zich voorgedaan probeer het alsjeblieft overnieuw!"));
            }
        });
    }
}
