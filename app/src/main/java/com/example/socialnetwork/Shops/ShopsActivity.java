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
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.Dialog.DialogActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.example.socialnetwork.Utils.UserListAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ShopsActivity extends AppCompatActivity {

    private static final String TAG = "ShopsActivity";
    private static final int ACTIVITY_NO = 4;
    private Context mContext = ShopsActivity.this;
    GridView listView;
    ArrayList<String> shopsList;
    ArrayList<String> shopsPhoto;
    ArrayList<String> shopsDescr;
    ArrayList<String> shopsId;
    UserListAdapter adapter;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        listView = findViewById(R.id.shopsList);
        setupToolbar();
        setupBottomNavigationView();
        addShops();
    }

    private void addShops() {
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "req_login";
        final String myid = db.getUserDetails().get("main_id");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/get_user_shops/?id=" + myid), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SHops data Response: " + response.toString());
                try {
                    final JSONObject jObj = new JSONObject(response);
                    boolean error = false;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            JSONObject following = null;
                            String userid = null;
                            Log.d("TAG", "itemClick: position = " + position + ", id = " + id + ", " + parent.getAdapter().getItem(position));
                            try {
                                following = jObj.getJSONObject("shop-"+ id);
                                userid = following.getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //UID!!!!!!!!!!!!!!!!!!!!!!!!!
                            Log.d("TAG", "Follower = " + following);
                            Intent ShopUser = new Intent(mContext, UserShopActivity.class);
                            ShopUser.putExtra("uid", userid);
                            startActivity(ShopUser);
                        }
                    });
                    Log.d(TAG, jObj.toString());
                    if (!error) {
                        int shopscount = jObj.getInt("count");
                        String shops_id = null;

                        if (shopscount == 0) {

                        }
                        else {
                            shopsList  = new ArrayList<>();
                            shopsPhoto  = new ArrayList<>();
                            shopsDescr  = new ArrayList<>();
                            shopsId  = new ArrayList<>();
                            for (int x = 0; x < shopscount; x++) {
                                String count = Integer.toString(x);
                                JSONObject shops = jObj.getJSONObject("shop-"+count);
                                shops_id = shops.getString("id");
                                db.addShop(shops_id, count);
                                String nick = (shops.getString("name"));
                                String photo = (shops.getString("logo"));
                                String descr = (shops.getString("descr"));
                                String photoDefault = ("http://berna-diplom.norox.com.ua/img/shops_logo/no_photo.jpg");
                                Log.d(TAG, "User count: " + nick);
                                Log.d(TAG, "User count: " + photo);
                                if(!shops.getString("logo").equals("")) {
                                    shopsList.add(nick);
                                    shopsDescr.add(descr);
                                    shopsPhoto.add("http://berna-diplom.norox.com.ua/img/shops_logo/"+photo);
                                    shopsId.add(shops_id);
                                }
                                else {
                                    shopsList.add(nick);
                                    shopsDescr.add(descr);
                                    shopsPhoto.add(photoDefault);
                                    shopsId.add(shops_id);
                                }
                                Log.d(TAG, "shopsList = " + shopsList);
                                Log.d(TAG, "shopsPhoto = " + shopsPhoto);
                            }
                            setupListShops(shopsList, shopsPhoto, shopsDescr, shopsId);
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
    private void setupListShops(ArrayList<String> shopsList, ArrayList<String> shopsPhoto, ArrayList<String> shopsDescr, ArrayList<String> shopsId) {
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/2;
        listView.setColumnWidth(imageWidth);
        adapter = new UserListAdapter(mContext,R.layout.layout_friends_view,shopsList, shopsPhoto, shopsDescr, shopsId);
        listView.setAdapter(adapter);

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
    private void setupToolbar() {
        db = new SQLiteHandler(getApplicationContext());
        final String myid = db.getUserDetails().get("main_id");
        Toolbar toolbar = (Toolbar) findViewById(R.id.shops_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.addShop :
                        Intent addshopIntent = new Intent(ShopsActivity.this, AddShopActivity.class);
                        addshopIntent.putExtra("uid", myid);
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
