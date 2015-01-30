package com.kplus.android.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenny.snackbar.SnackBar;
import com.kplus.android.config.APIClient;
import com.kplus.android.config.BaseFunctions;
import com.kplus.android.config.CartAdapter;
import com.kplus.android.config.ProductOnClickListener;
import com.kplus.android.config.SessionManager;
import com.kplus.android.k_plusandroidapp.R;
import com.kplus.android.models.WinkelLocatie;
import com.kplus.android.models.jsonobjects.CartLineResponse;
import com.kplus.android.models.jsonobjects.CartResponse;
import com.kplus.android.models.jsonobjects.ProductResponse;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity implements NfcAdapter.CreateNdefMessageCallback
{
    public final String TAG = "MainActivity";
    private NfcAdapter nfcAdapter;
    private SessionManager session;
    private MainActivity activity = this;
    private CartAdapter adapter;
    private final int SCAN_BARCODE = 1;
    private final int SCAN_ADD = 2;
    private final int SCAN_SUB = 0;
    private String billToSend;

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
        checkIfNFCIsAvailable();

        checkIfInStoreRange();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
        {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            BaseFunctions.Log(TAG, "NFC Berichtontvangen: " + new String(message.getRecords()[0].getPayload()));
            SnackBar.show(this, new String(message.getRecords()[0].getPayload()));

        }
        else{BaseFunctions.Log(TAG, "Waiting for NDEF Message");}

    }

    private void loadShoppingList()
    {

        BaseFunctions.Log(TAG, "Called loadShoppingList() method");

        RequestParams params = new RequestParams();

        APIClient.get("/cart", params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                ArrayList<ProductResponse> products = new ArrayList<ProductResponse>();
                try
                {

                    ObjectMapper mapper = new ObjectMapper();
                    CartResponse cart = mapper.readValue(response.getJSONObject("data").toString(), CartResponse.class);


                    if (cart != null)
                    {
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
                }
                catch (Exception e)
                {
                    BaseFunctions.handleException(activity, e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response)
            {
                BaseFunctions.Log(TAG, "FailedHandler: " + statusCode);
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show();
                try
                {
                    BaseFunctions.Log(TAG, "Failed to get shippinglist [Error: " + response.getJSONObject("error").getString("message") + "]");
                    SnackBar.show(activity, BaseFunctions.getErrorSnackBar(activity, response.getJSONObject("error").getString("message")));
                }
                catch (Exception e)
                {
                    BaseFunctions.handleException(activity, e);
                }

            }
        });
    }

    private void scanBarcode(int addToChart)
    {
        Intent intent = new Intent(this, QRScanActivity.class);
        startActivityForResult(intent, SCAN_BARCODE + addToChart);
    }

    private void checkIfNFCIsAvailable()
    {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null)
        {
            SnackBar.show(this, "Sorry, je mnoet NFC hebben om af te rekenen!");
            return;
        }

        if (!nfcAdapter.isEnabled()){SnackBar.show(this, "Schakel NFC alsjeblieft in.");}
    }

    private void checkIfInStoreRange()
    {
        Location latestKnownLocation = getGPS();

        if(latestKnownLocation != null)
        {
            BaseFunctions.Log(TAG, "Latitude" + latestKnownLocation.getLatitude());
            BaseFunctions.Log(TAG, "Longitude" + latestKnownLocation.getLongitude());

            WinkelLocatie winkelInRange = BaseFunctions.inStoreRange(latestKnownLocation);

            if(winkelInRange != null)
            {
                BaseFunctions.Log(TAG, "Gebruiker is in range van de winkel: " + winkelInRange.getName());

                SnackBar.show(this, "KAAS VOOR 5 EURO KOOP T NU :D");
                //Todo ad laten zien hiero :D
            }
            else
            {
                BaseFunctions.Log(TAG, "Geen winkel in range!");
            }
        }
        else
        {
            BaseFunctions.Log(TAG, "latestKnownLocation is null");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        int productID = Integer.parseInt(intent.getStringExtra("sym0"));
        if (requestCode == SCAN_ADD && resultCode == RESULT_OK)
        {
            BaseFunctions.Log(TAG, "Adding " + productID + " to shoppinglist");

            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
            builder1.setMessage("Product ID: " + productID);
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                        }
                    }
            );
            builder1.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                        }
                    }
            );

            AlertDialog alert11 = builder1.create();
            alert11.show();

            CartLineResponse cartLine = adapter.getCartlineWithProductId(productID);
            adapter.updateCartLine(cartLine, 1);
        }
        else if (requestCode == SCAN_SUB && resultCode == RESULT_OK)
        {
            BaseFunctions.Log(TAG, "Removing " + productID + " from shoppinglist");
            CartLineResponse cartLine = adapter.getCartlineWithProductId(productID);
            adapter.updateCartLine(cartLine, -1);
        }
    }

    public void rekenAf(View view)
    {
        BaseFunctions.Log(TAG, "Afreken button ingedrukt!");

        //Todo zorgen dat alleen betaald wordt voor de gescande p;roducten of miss alleen button laten zien als alles goren is
        //ff met Bas overleggen

        billToSend = "";
        int priceToPay = 0;
        List<CartLineResponse> cartList = adapter.getCart().getCartLines();
        for(CartLineResponse product : cartList)
        {
            int totalPrice = product.getQty() * product.getProduct().getPrice();
            BaseFunctions.Log(TAG, product.getProduct().getName() + " kost totaal: " + product.getQty() + " * " + product.getProduct().getPrice() + " = " + totalPrice);
            priceToPay += totalPrice;
        }

        billToSend = String.valueOf(priceToPay);

        nfcAdapter.setNdefPushMessageCallback(this, this);

        SnackBar.show(this, "Houd uw mobiel tegen de kassa aan om de producten over te sturen");

        //Eventuele code om de shopping cart te legen
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent)
    {
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", billToSend.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

    private Location getGPS()
    {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location location = null;

        for (int i=providers.size()-1; i>=0; i--)
        {
            location = locationManager.getLastKnownLocation(providers.get(i));
            if (location != null) break;
        }

        if (location != null){ return location; }
        else{ return null; }
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
        if (id == R.id.action_addscanproduct)
        {
            scanBarcode(1);
            return true;
        }
        if (id == R.id.action_subscanproduct)
        {
            scanBarcode(-1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
