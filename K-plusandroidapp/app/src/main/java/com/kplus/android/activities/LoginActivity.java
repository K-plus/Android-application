package com.kplus.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenny.snackbar.SnackBar;
import com.kplus.android.config.APIClient;
import com.kplus.android.config.BaseFunctions;
import com.kplus.android.config.SessionManager;
import com.kplus.android.k_plusandroidapp.R;
import com.kplus.android.models.jsonobjects.UserResponse;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends Activity
{
    private String TAG = "LoginActivity";
    private LoginActivity activity = this;
    private SessionManager session;

    //View elements injections
    @InjectView(R.id.et_Email)
    EditText mEmailAddress;
    @InjectView(R.id.et_Password)
    EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);

        session = new SessionManager(getApplicationContext());

        if ( session.isLoggedIn() )
        {
            Intent startMain = new Intent(this, MainActivity.class);
                startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LoginActivity.this.startActivity(startMain);
        }
    }

    public void login(View v)
    {
        BaseFunctions.Log(TAG, "Called Login method");

        RequestParams params = new RequestParams();
            params.put("email", mEmailAddress.getText().toString());
            params.put("password", mPassword.getText().toString());

        APIClient.post("/customer/login", params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                try
                {
                    ObjectMapper mapper = new ObjectMapper();
                    UserResponse user = mapper.readValue(response.getJSONObject("data").toString(), UserResponse.class);

                    BaseFunctions.Log(TAG, "User: " + user.getEmail() + " Succesfully logged in");

                    session.createLoginSession(user.getEmail(), user.getName());

                    SnackBar.show(activity, getResources().getString(R.string.success_login));

                    Intent startMain = new Intent(activity, MainActivity.class);
                    startActivity(startMain);
                }
                catch(Exception e){BaseFunctions.handleException(activity, e);}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response)
            {
                try
                {
                    BaseFunctions.Log(TAG, "Failed to login [Error: " + response.getJSONObject("error").getString("message") + "]");
                    SnackBar.show(activity, BaseFunctions.getErrorSnackBar(activity, response.getJSONObject("error").getString("message")));
                }
                catch(Exception e){BaseFunctions.handleException(activity, e);}
            }
        });
    }
}
