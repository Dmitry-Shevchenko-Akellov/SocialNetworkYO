package com.example.socialnetwork.Shops;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private static final String TAG = "ProductActivity";
    private static final int ACTIVITY_NO = 4;
    private Context mContext = ProductActivity.this;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String shops_id_ = getIntent().getStringExtra("uid_shop");
        final String product_id = getIntent().getStringExtra("uid_prod");
        Log.d("TAG", "Prod id = " + shops_id_);
        Log.d("TAG", "Prod id = " + product_id);
        setContentView(R.layout.activity_product);
        setupBottomNavigationView();

        setupDataProduct(shops_id_, product_id);
    }

    private void setupDataProduct(final String shops_id_, final String product_id) {
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "set_data_shop";
        final String myid = db.getUserDetails().get("main_id");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/get_shop_prod/?id=" + shops_id_), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SHops data Response: " + response.toString());
                try {
                    final JSONObject jObj = new JSONObject(response);
                    boolean error = false;
                    Log.d(TAG, jObj.toString());
                    if (!error) {
                        int prodcount = jObj.getInt("prod_count");
                        JSONObject prods = null;
                        if (prodcount == 0) {

                        }
                        else {
                            for (int x = 0; x < prodcount; x++) {
                                String count = Integer.toString(x);
                                JSONObject forShop = jObj.getJSONObject("prod-"+count);
                                if (forShop.getString("id").equals(product_id)) {
                                    prods = jObj.getJSONObject("prod-"+count);
                                }
                            }
                            String prodId = prods.getString("id");
                            String prodName = prods.getString("name");
                            String prodDescr = prods.getString("descr");
                            String prodCost = prods.getString("cost");
                            String prodPhoto = prods.getString("photo");


                            TextView prodDescrView = (TextView)findViewById(R.id.product_description);
                            TextView prodNameView = (TextView)findViewById(R.id.product_name);
                            ImageView prodLogoView = (ImageView)findViewById(R.id.grid_prod_view);
                            TextView buyButtonView = (TextView)findViewById(R.id.buy_cost);

                            prodNameView.setText(prodName);
                            prodDescrView.setText(prodDescr);
                            buyButtonView.setText(prodCost);
                            Log.e(TAG, "Photo = : " + prodPhoto);
                            if (prodPhoto.equals("")) {
                                Picasso.get().load("http://berna-diplom.norox.com.ua/img/no_photo.jpg").into(prodLogoView);
                            }else{
                                Picasso.get().load("http://berna-diplom.norox.com.ua/img/products"+prodPhoto).into(prodLogoView);
                            }
                        }

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(mContext.getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext.getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(mContext.getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void setupBottomNavigationView(){

        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);
    }
}
