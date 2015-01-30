package com.kplus.android.config;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.kenny.snackbar.SnackBar;
import com.kenny.snackbar.SnackBarItem;
import com.kplus.android.k_plusandroidapp.R;
import com.kplus.android.models.WinkelLocatie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vasco on 21-1-2015.
 */
public class BaseFunctions
{
    private static List<WinkelLocatie> winkelLijst = new ArrayList<WinkelLocatie>(Arrays.asList(
            new WinkelLocatie("Hanzehoge school Groningen", 90, 0, 90, 0)
    ));


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

    public static WinkelLocatie inStoreRange(Location currentLocation)
    {
        for(WinkelLocatie winkel : winkelLijst)
        {
            if(winkel.userInRange(currentLocation)){ return winkel; }
        }
        return null;
    }
}
