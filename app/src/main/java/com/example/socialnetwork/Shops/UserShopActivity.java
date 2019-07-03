package com.example.socialnetwork.Shops;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.Friends.UserProfileActivity;
import com.example.socialnetwork.Profile.ProfileActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.GridViewAdapter;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class UserShopActivity extends AppCompatActivity {
    private static final String TAG = "UserShopActivity";
    private static final int ACTIVITY_NO = 4;
    private static final int NUM_GRID_COLUMNS = 2;
    private Context mContext = UserShopActivity.this;
    private ArrayList<String> mediaUrls;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String userid = getIntent().getStringExtra("uid");
        Log.d("TAG", "User id = " + userid);
        setContentView(R.layout.activity_shop);
        setupBottomNavigationView();
        setupToolbar(userid);
        LinearLayout boxProd = (LinearLayout)findViewById(R.id.prodsBox);

        boxProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ShopProd = new Intent(mContext, ProductsList.class);
                ShopProd.putExtra("uid_shop", userid);
                startActivity(ShopProd);
            }
        });

        setDataShop(userid);
        setGridProduct(userid);
    }

    private void setDataShop(final String userid) {
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "set_data_shop";
        final String myid = db.getUserDetails().get("main_id");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/get_user_shops/?id=" + myid), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SHops data Response: " + response.toString());
                try {
                    final JSONObject jObj = new JSONObject(response);
                    boolean error = false;
                    Log.d(TAG, jObj.toString());
                    if (!error) {
                        int shopscount = jObj.getInt("count");
                        JSONObject shops = null;
                        if (shopscount == 0) {

                        }
                        else {
                            for (int x = 0; x < shopscount; x++) {
                                String count = Integer.toString(x);
                                JSONObject forShop = jObj.getJSONObject("shop-"+count);
                                if (forShop.getString("id").equals(userid)) {
                                    shops = jObj.getJSONObject("shop-"+count);
                                }
                            }
                            String shops_id = shops.getString("id");
                            String nick = (shops.getString("name"));
                            String descr = (shops.getString("descr"));
                            String photo = (shops.getString("logo"));
                            String photoDefault = ("http://berna-diplom.norox.com.ua/img/shops_logo/no_photo.jpg");



                            TextView shopDescr = (TextView)findViewById(R.id.shop_description);
                            TextView shopName = (TextView)findViewById(R.id.shop_name);
                            ImageView shopLogo = (ImageView)findViewById(R.id.shop_photo);
                            shopDescr.setText(descr);
                            shopName.setText(nick);
                            if (photo.equals("")) {
                                Picasso.get().load("http://berna-diplom.norox.com.ua/img/no_photo.jpg").into(shopLogo);
                            }else{
                                Picasso.get().load("http://berna-diplom.norox.com.ua/img/shops_logo/"+photo).into(shopLogo);
                            }

                            //setupListShops(shopsList, shopsPhoto);
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

    private void setGridProduct(final String shopid) {
        db.deleteProduct();
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "set_data_shop";
        final String myid = db.getUserDetails().get("main_id");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/get_shop_prod/?id=" + shopid), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SHops data Response: " + response.toString());
                try {
                    final JSONObject jObj = new JSONObject(response);
                    boolean error = false;
                    Log.d(TAG, jObj.toString());
                    if (!error) {
                        int prodcount = jObj.getInt("prod_count");
                        String countForView = Integer.toString(prodcount);
                        TextView shopProductCount = (TextView)findViewById(R.id.sh_prod);
                        shopProductCount.setText(countForView);
                        mediaUrls = new ArrayList<>();
                        if (prodcount == 0) {
                            //setupProdGrid(mediaUrls);
                        }
                        else {
                            for (int x = 0; x < prodcount; x++) {
                                String count = Integer.toString(x);
                                JSONObject prod = jObj.getJSONObject("prod-"+ x);
                                String photo = (prod.getString("photo"));
                                String prod_id = prod.getString("id");
                                db.addProduct(prod_id, count);
                                if (photo.equals("") || photo.equals(" ")) {
                                    mediaUrls.add("http://berna-diplom.norox.com.ua/img/no_photo.jpg");
                                }
                                else {
                                    mediaUrls.add("products" + photo);
                                }
                                Log.d(TAG, "MediaUrl = " + mediaUrls);
                            }
                            //setupProdGrid(mediaUrls);
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

    private void setupToolbar(final String shopId) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.shops_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.addShop :
                        Intent addshopIntent = new Intent(UserShopActivity.this, AddNewProd.class);
                        addshopIntent.putExtra("uid", shopId);
                        startActivity(addshopIntent);
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.shop_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
