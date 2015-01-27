package com.kplus.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kplus.android.config.APIClient;
import com.kplus.android.config.BaseFunctions;
import com.kplus.android.config.SessionManager;
import com.kplus.android.k_plusandroidapp.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

public class MainActivity extends Activity
{
    private final String TAG = "MainActivity";
    private SessionManager session;
    private MainActivity activity = this;
    private final int SCAN_BARCODE = 1;
    private final int SCAN_ADD = 2;
    private final int SCAN_SUB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        loadShoppingList();
    }

    private void loadShoppingList()
    {
        //Code om iets met de cart te doen :p en op te halen.
        //The floor is yours Bas
        
        BaseFunctions.Log(TAG, "Called loadShoppingList() method");

        RequestParams params = new RequestParams();
            params.put("email", session.getEmail());

        APIClient.post("/cart", params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response)
            {
            }
        });
    }

    private void scanBarcode(int addToChart)
    {
        try
        {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, SCAN_BARCODE + addToChart);
        }
        catch (Exception e)
        {
            //Als hij niet geinstalleerd is open de markt om hem te installeren
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == SCAN_ADD && resultCode == RESULT_OK)
        {
            String productID = intent.getStringExtra("SCAN_RESULT");
            BaseFunctions.Log(TAG, "Adding " + productID + " to shoppinglist");

//            APIClient.post("/cart/product/" + productID, null, new JsonHttpResponseHandler()
//            {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
//                {
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, String errorMessage, Throwable throwable)
//                {
//                }
//            });
        }
        else if (requestCode == SCAN_SUB && resultCode == RESULT_OK)
        {
            String productID = intent.getStringExtra("SCAN_RESULT");
            BaseFunctions.Log(TAG, "Removing " + productID + " from shoppinglist");

//            APIClient.post("/cart/product/" + productID, null, new JsonHttpResponseHandler()
//            {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
//                {
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, String errorMessage, Throwable throwable)
//                {
//                }
//            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_addscanproduct)
        {
            scanBarcode(1);
            return true;
        }
        if(id == R.id.action_subscanproduct)
        {
            scanBarcode(-1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
