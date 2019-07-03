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
import android.widget.GridView;
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
import com.example.socialnetwork.Utils.GridProdAdapter;
import com.example.socialnetwork.Utils.GridViewAdapter;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsList extends AppCompatActivity {
    private static final String TAG = "UserShopActivity";
    private static final int ACTIVITY_NO = 4;
    private static final int NUM_GRID_COLUMNS = 2;
    private Context mContext = ProductsList.this;
    private ArrayList<String> mediaUrls;
    private SQLiteHandler db;
    private GridView gridView;
    ArrayList<String> prodsName;
    ArrayList<String> prodsPhoto;
    ArrayList<String> prodsDescr;
    ArrayList<String> prodsCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String userid = getIntent().getStringExtra("uid_shop");
        Log.d("TAG", "User id = " + userid);
        setContentView(R.layout.products_wall);
        setupBottomNavigationView();

        setupDataProduct(userid);
    }

    private void setupDataProduct(final String shops_id_) {
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
                        if (prodcount == 0) {

                        }
                        else {
                            prodsName  = new ArrayList<>();
                            prodsPhoto  = new ArrayList<>();
                            prodsDescr  = new ArrayList<>();
                            prodsCost  = new ArrayList<>();
                            for (int x = 0; x < prodcount; x++) {
                                String count = Integer.toString(x);
                                JSONObject forShop = jObj.getJSONObject("prod-"+count);
                                String prodId = forShop.getString("id");
                                String prodName = forShop.getString("name");
                                String prodDescr = forShop.getString("descr");
                                String prodCost = forShop.getString("cost");
                                String prodPhoto = forShop.getString("photo");

                                prodsPhoto.add("http://berna-diplom.norox.com.ua/img/products"+prodPhoto);
                                prodsName.add(prodName);
                                prodsDescr.add(prodDescr);
                                prodsCost.add(prodCost);
                            }
                            setupProdGrid(prodsPhoto, prodsName, prodsDescr, prodsCost);
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

    private void setupProdGrid(ArrayList<String> prodPhoto,ArrayList<String> prodName,ArrayList<String> prodsDescr,ArrayList<String> prodsCost) {
        gridView = findViewById(R.id.prodsList);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);
        GridProdAdapter viewAdapter = new GridProdAdapter(ProductsList.this,R.layout.layout_prods_view,prodsPhoto,prodsName,prodsDescr,prodsCost);
        gridView.setAdapter(viewAdapter);
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
