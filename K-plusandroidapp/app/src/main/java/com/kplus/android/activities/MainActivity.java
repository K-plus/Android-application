package com.kplus.android.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenny.snackbar.SnackBar;
import com.kplus.android.config.*;
import com.kplus.android.k_plusandroidapp.R;
import com.kplus.android.models.jsonobjects.CartLineResponse;
import com.kplus.android.models.jsonobjects.CartResponse;
import com.kplus.android.models.jsonobjects.ProductResponse;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends ListActivity
{
    public final String TAG = "MainActivity";
    private SessionManager session;
    private MainActivity activity = this;
    private CartAdapter adapter;
    private final int SCAN_BARCODE = 1;
    private final int SCAN_ADD = 2;
    private final int SCAN_SUB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        adapter = new CartAdapter(activity);

        setListAdapter(adapter);

        getListView().setOnItemClickListener(new ProductOnClickListener(activity, adapter));

        loadShoppingList();
    }

    private void loadShoppingList()
    {
        
        BaseFunctions.Log(TAG, "Called loadShoppingList() method");

        RequestParams params = new RequestParams();

        APIClient.get("/cart", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //BaseFunctions.Log(TAG, "Successhandler: " + response.toString());

                //Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                ArrayList<ProductResponse> products = new ArrayList<ProductResponse>();
                try {

                    ObjectMapper mapper = new ObjectMapper();
                    CartResponse cart = mapper.readValue(response.getJSONObject("data").toString(), CartResponse.class);


                    if(cart != null) {
                        adapter.setCart(cart);
                        adapter.notifyDataSetChanged();
                    }

                    /*
                    JSONArray cartLines = response.getJSONObject("data").getJSONArray("cart_lines");
                    for (int i = 0; i < cartLines.length(); i++) {
                        JSONObject cartLine = (JSONObject) cartLines.get(i);
                        ProductResponse product = mapper.readValue(cartLine.getJSONObject("product").toString(), ProductResponse.class);
                        products.add(product);
                    }*/
                } catch (Exception e) {
                    BaseFunctions.handleException(activity, e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                BaseFunctions.Log(TAG, "FailedHandler: " + statusCode);
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show();
                try {
                    BaseFunctions.Log(TAG, "Failed to get shippinglist [Error: " + response.getJSONObject("error").getString("message") + "]");
                    SnackBar.show(activity, BaseFunctions.getErrorSnackBar(activity, response.getJSONObject("error").getString("message")));
                } catch (Exception e) {
                    BaseFunctions.handleException(activity, e);
                }

            }
        });
    }

    private void scanBarcode(int addToChart)
    {
        Intent intent = new Intent(this, QRScanActivity.class);
        startActivityForResult(intent, SCAN_BARCODE + addToChart);
        /*
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
        }*/
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        int productID = Integer.parseInt(intent.getStringExtra("sym0"));
        if (requestCode == SCAN_ADD && resultCode == RESULT_OK)
        {
            BaseFunctions.Log(TAG, "Adding " + productID + " to shoppinglist");
            //Toast.makeText(getApplicationContext(), "Adding productId: " + productID, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
            builder1.setMessage("Product ID: " + productID);
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder1.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

            CartLineResponse cartLine = adapter.getCartlineWithProductId(productID);
            adapter.updateCartLine(cartLine, 1);

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
            BaseFunctions.Log(TAG, "Removing " + productID + " from shoppinglist");
            CartLineResponse cartLine = adapter.getCartlineWithProductId(productID);
            adapter.updateCartLine(cartLine, -1);
            /*
            final CartLineResponse cartLine = adapter.getCartlineWithProductId(productID);
            if(cartLine != null) {

                RequestParams params = new RequestParams();
                params.put("id", productID);
                params.put("qty", cartLine.getQty()-1);

                APIClient.put("/cart/product/update", params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        adapter.updateCartLine(cartLine);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                        BaseFunctions.Log(TAG, "FailedHandler: " + statusCode);
                        try {
                            BaseFunctions.Log(TAG, "Failed to update product [Error: " + response.getJSONObject("error").getString("message") + "]");
                            SnackBar.show(activity, BaseFunctions.getErrorSnackBar(activity, response.getJSONObject("error").getString("message")));
                        } catch (Exception e) {
                            BaseFunctions.handleException(activity, e);
                        }
                    }
                });

            }*/


        }


        /*
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
        */
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
