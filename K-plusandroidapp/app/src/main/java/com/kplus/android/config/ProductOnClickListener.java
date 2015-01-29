package com.kplus.android.config;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.NumberPicker;
import com.kenny.snackbar.SnackBar;
import com.kplus.android.activities.MainActivity;
import com.kplus.android.k_plusandroidapp.R;
import com.kplus.android.models.jsonobjects.CartLineResponse;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by bas on 29-1-15.
 */
public class ProductOnClickListener implements AdapterView.OnItemClickListener {

    MainActivity activity;
    CartAdapter adapter;

    public ProductOnClickListener(MainActivity activity, CartAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final CartLineResponse cartLine = (CartLineResponse)adapter.getItem(position);
        final Dialog d = new Dialog(activity);
        d.setTitle(cartLine.getProduct().getName());
        d.setContentView(R.layout.picker_layout);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setValue(cartLine.getQty());
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                CartLineResponse cartLineResponse = adapter.getCartlineWithProductId(cartLine.getProduct().getId());
                cartLineResponse.setQty(np.getValue());

                final CartLineResponse finalLine = cartLineResponse;
                d.dismiss();

                if(cartLineResponse != null) {

                    RequestParams params = new RequestParams();
                    params.put("id", cartLine.getProduct().getId());
                    params.put("qty", cartLine.getQty());

                    APIClient.post("/cart/product/update", params, new JsonHttpResponseHandler()
                    {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            adapter.updateCartLine(finalLine, np.getValue() - finalLine.getQty());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                            BaseFunctions.Log(activity.TAG, "FailedHandler: " + statusCode);
                            try {
                                BaseFunctions.Log(activity.TAG, "Failed to update product [Error: " + response.getJSONObject("error").getString("message") + "]");
                                SnackBar.show(activity, BaseFunctions.getErrorSnackBar(activity, response.getJSONObject("error").getString("message")));
                            } catch (Exception e) {
                                BaseFunctions.handleException(activity, e);
                            }
                        }
                    });

                }

            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }
}
