package com.kplus.android.config;

import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by Vasco on 21-1-2015.
 */
public class APIClient
{
    private static final String BASE_URL = Variables.API_URL;
    private static AsyncHttpClient client;
    private static String TAG = "APIClient";
    private static SessionManager sessionManager;

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
        if (Looper.myLooper() == null)
        {
            client = new SyncHttpClient();
        }
        else
        {
            client = new AsyncHttpClient();
        }

        setCredentials();
        BaseFunctions.Log(TAG, "Sending GET request to: " + BASE_URL + url);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
        if (Looper.myLooper() == null)
        {
            client = new SyncHttpClient();
        }
        else
        {
            client = new AsyncHttpClient();
        }

        setCredentials();
        BaseFunctions.Log(TAG, "Sending POST request to: " + BASE_URL + url);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
        if (Looper.myLooper() == null)
        {
            client = new SyncHttpClient();
        }
        else
        {
            client = new AsyncHttpClient();
        }

        setCredentials();
        BaseFunctions.Log(TAG, "Sending POST request to: " + BASE_URL + url);
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    private static void setCredentials()
    {
        if (sessionManager != null)
        {
            client.setBasicAuth(sessionManager.getEmail(), sessionManager.getPass());
        }
    }

    private static String getAbsoluteUrl(String relativeUrl)
    {
        return BASE_URL + relativeUrl;
    }

    public static void setSession(SessionManager session)
    {
        sessionManager = session;
    }
}
