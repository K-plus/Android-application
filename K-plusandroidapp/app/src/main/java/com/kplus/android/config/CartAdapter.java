package com.kplus.android.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.android.activities.MainActivity;
import com.kplus.android.k_plusandroidapp.R;
import com.kplus.android.models.jsonobjects.CartLineResponse;
import com.kplus.android.models.jsonobjects.CartResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by bas on 23-1-15.
 */
public class CartAdapter extends BaseAdapter
{

    private final String EURO_STRING = "€";
    private final String QTY_STRING = "Aantal: ";

    private ArrayList<CartLineResponse> groceryList;
    private CartResponse cart;
    private MainActivity activity;
    private HashMap<String, Integer> scannedProducts;

    public CartAdapter(MainActivity activity)
    {
        this.activity = activity;
        this.groceryList = new ArrayList<CartLineResponse>();
        scannedProducts = resetScannedProducts();
        //scannedProducts = new HashMap<String, Integer>();
    }

    public CartResponse getCart()
    {
        return cart;
    }

    public void setCart(CartResponse cart)
    {
        this.cart = cart;
        this.groceryList = cart.getCartLines();
    }

    public Map<String, Integer> getScannedProducts()
    {
        return scannedProducts;
    }

    public CartLineResponse getCartlineWithProductId(int productId)
    {
        for (CartLineResponse cartLine : groceryList)
        {
            if (cartLine.getProduct().getId() == productId)
            {
                return cartLine;
            }
        }
        return null;
    }

    public void updateCartLine(CartLineResponse cartLine, int update)
    {
        if (scannedProducts.containsKey(Integer.toString(cartLine.getProduct().getId())))
        {
            scannedProducts.put(Integer.toString(cartLine.getProduct().getId()), scannedProducts.get(Integer.toString(cartLine.getProduct().getId())) + update);
        }
        else if (update > 0)
        {
            scannedProducts.put(Integer.toString(cartLine.getProduct().getId()), update);
        }
        saveScannedProducts();
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return groceryList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return groceryList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return groceryList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {

            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout, parent, false);

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.qty = (TextView) convertView.findViewById(R.id.qty);
            holder.scanned = (TextView) convertView.findViewById(R.id.scanned);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        CartLineResponse cartLine = groceryList.get(position);
        holder.name.setText(cartLine.getProduct().getName());
        holder.price.setText(EURO_STRING + Double.toString(((double) cartLine.getProduct().getPrice()) / 100));
        holder.qty.setText(QTY_STRING + Integer.toString(cartLine.getQty()));
        Integer scannedInt = new Integer(0);
        if (scannedProducts != null)
        {
            if (scannedProducts.size() > 0)
            {
                scannedInt = scannedProducts.get(Integer.toString(cartLine.getProduct().getId()));
            }
        }
        if (scannedInt == null)
        {
            scannedInt = new Integer(0);
        }
        holder.scanned.setText("Scanned: " + scannedInt.intValue());

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.row_layout);

        if (cartLine.getQty() == scannedInt.intValue())
        {
            layout.setBackgroundColor(activity.getResources().getColor(R.color.DarkGreen));
        }
        else
        {
            layout.setBackgroundColor(activity.getResources().getColor(R.color.blood_dark));
        }

        return convertView;
    }

    private void saveScannedProducts()
    {
        SharedPreferences keyValues = activity.getApplicationContext().getSharedPreferences(SessionManager.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor keyValuesEditor = keyValues.edit();

        JSONObject jsonMap = new JSONObject(scannedProducts);
        keyValuesEditor.putString("HashMap", jsonMap.toString());

        keyValuesEditor.commit();
    }

    private HashMap<String, Integer> resetScannedProducts()
    {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        SharedPreferences keyValues = activity.getApplicationContext().getSharedPreferences(SessionManager.PREF_NAME, Context.MODE_PRIVATE);

        try
        {
            String jsonMapString = keyValues.getString("HashMap", null);
            JSONObject jsonObject = new JSONObject(jsonMapString);
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext())
            {
                String key = keysItr.next();
                Integer value = (Integer) jsonObject.get(key);
                map.put(key, value);
            }
            return map;
        }
        catch (Exception e)
        {
            BaseFunctions.handleException(activity, e);
        }
        return map;
    }

    private class ViewHolder
    {
        public TextView name;
        public TextView price;
        public TextView qty;
        public TextView scanned;

        public ViewHolder()
        {
            name = new TextView(activity.getApplicationContext());
            price = new TextView(activity.getApplicationContext());
            qty = new TextView(activity.getApplicationContext());
            scanned = new TextView(activity.getApplicationContext());
        }
    }
}