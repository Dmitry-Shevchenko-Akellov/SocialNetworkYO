package com.example.socialnetwork.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Shops.ProductsList;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.GridViewAdapter;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.example.socialnetwork.Utils.UserWallAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WallList extends AppCompatActivity {
    private static final String TAG = "WallAct";
    private static final int ACTIVITY_NO = 0;
    private Context mContext = WallList.this;
    private SQLiteHandler db;
    GridView gridView;
    ArrayList<String> wallName;
    ArrayList<String> wallPhoto;
    ArrayList<String> wallDescr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_wall);
        db = new SQLiteHandler(getApplicationContext());
        final String userid = getIntent().getStringExtra("uid");
        setupBottomNavigationView();
        if (userid.equals(db.getUserDetails().get("main_id"))) {
            setupProfileData();
        }
        else {
            setupProfileData(userid);
        }
    }
    private void setupProfileData() {
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "req_login";
        final String myid = db.getUserDetails().get("main_id");
        Log.d("","myid = " + myid);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/get_user_info/?" + myid + "=" + myid + "&act=wall"), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile data Response: "+ myid + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    Log.d(TAG, jObj.toString());
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("my_info");
                        int wallcount = user.getInt("wall_c");
                        String wall_id = null;

                        wallName = new ArrayList<>();
                        wallDescr = new ArrayList<>();
                        wallPhoto = new ArrayList<>();
                        if (wallcount == 0) {
                            //setupImageGrid(mediaUrls);
                        }
                        else {
                            for (int x = 0; x < wallcount; x++) {
                                String count = Integer.toString(x);
                                JSONObject wall = jObj.getJSONObject(count);
                                wall_id = wall.getString("wall_id");
                                String wallname = wall.getString("wall_title");
                                String walldesc = wall.getString("wall_descr");
                                if (!wall.getString("wall_back_type").equals("img")) {
                                    wallPhoto.add("http://berna-diplom.norox.com.ua/img/no_photo.jpg");
                                    wallName.add(wallname);
                                    wallDescr.add(walldesc);
                                }
                                else {
                                    wallPhoto.add(wall.getString("wall_back_set"));
                                    wallName.add(wallname);
                                    wallDescr.add(walldesc);
                                }
                                Log.d(TAG, "MediaUrl = " + wallPhoto);
                            }
                            setupImageGrid(wallName, wallDescr, wallPhoto);
                        }
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void setupProfileData(final String user_id__) {
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/get_user_info/?" + user_id__ + "=" + user_id__ + "&act=wall"), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile data Response: "+ user_id__ + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    Log.d(TAG, jObj.toString());
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("my_info");
                        int wallcount = user.getInt("wall_c");
                        String wall_id = null;

                        wallName = new ArrayList<>();
                        wallDescr = new ArrayList<>();
                        wallPhoto = new ArrayList<>();
                        if (wallcount == 0) {
                            //setupImageGrid(mediaUrls);
                        }
                        else {
                            for (int x = 0; x < wallcount; x++) {
                                String count = Integer.toString(x);
                                JSONObject wall = jObj.getJSONObject(count);
                                wall_id = wall.getString("wall_id");
                                String wallname = wall.getString("wall_title");
                                String walldesc = wall.getString("wall_descr");
                                if (!wall.getString("wall_back_type").equals("img")) {
                                    wallPhoto.add("http://berna-diplom.norox.com.ua/img/no_photo.jpg");
                                    wallName.add(wallname);
                                    wallDescr.add(walldesc);
                                }
                                else {
                                    wallPhoto.add(wall.getString("wall_back_set"));
                                    wallName.add(wallname);
                                    wallDescr.add(walldesc);
                                }
                                Log.d(TAG, "MediaUrl = " + wallPhoto);
                            }
                            setupImageGrid(wallName, wallDescr, wallPhoto);
                        }
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void setupImageGrid(ArrayList<String> wallName, ArrayList<String> wallDescr, ArrayList<String> wallPhoto) {
        gridView = findViewById(R.id.wallProfile);
        UserWallAdapter viewAdapter = new UserWallAdapter(WallList.this,R.layout.layout_wall_view, wallName, wallDescr, wallPhoto);
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
