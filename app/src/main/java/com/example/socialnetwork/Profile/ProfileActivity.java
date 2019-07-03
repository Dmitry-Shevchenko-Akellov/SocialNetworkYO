package com.example.socialnetwork.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.socialnetwork.Dialog.DialogActivity;
import com.example.socialnetwork.Friends.FollowersFragment;
import com.example.socialnetwork.Friends.FollowingFragment;
import com.example.socialnetwork.Friends.FriendsActivity;
import com.example.socialnetwork.Login.LoginActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Shops.ShopsActivity;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.GridViewAdapter;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.example.socialnetwork.Utils.SessionManager;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class ProfileActivity extends AppCompatActivity{

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NO = 0;
    private static final int NUM_GRID_COLUMNS = 3;
    private Context mContext = ProfileActivity.this;
    private ArrayList<String> mediaUrls;
    private SQLiteHandler db;
    SessionManager session;

    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_exp_my);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        setupToolbar();
        setupBottomNavigationView();
        setupProfileData();
        Button signout = (Button)findViewById(R.id.signout);
        Button editProfile = (Button)findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        LinearLayout box = (LinearLayout)findViewById(R.id.boxForPost);
        LinearLayout boxFollowers = (LinearLayout)findViewById(R.id.boxForFollowers);
        LinearLayout boxFollowing = (LinearLayout)findViewById(R.id.boxForFollowing);
        LinearLayout boxShop = (LinearLayout)findViewById(R.id.boxForShop);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, WallList.class);
                intent.putExtra("uid", db.getUserDetails().get("main_id"));
                startActivity(intent);
                finish();
            }
        });
        boxShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ShopsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        boxFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FriendsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        boxFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FriendsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLogin(false);
                db.deleteUsers();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
                        String name = user.getString("main_name");
                        String lastname = user.getString("main_lastname");
                        String description = user.getString("main_status");
                        String followingcount = user.getString("following_c");
                        String followerscount = user.getString("followers_c");
                        String shopscount = user.getString("shops_c");
                        int wallcount = user.getInt("wall_c");
                        String wall_id = null;



                        TextView wallcountBox = (TextView)findViewById(R.id.postCount);
                        TextView shopscountBox = (TextView)findViewById(R.id.shopCount);
                        TextView followingcountBox = (TextView)findViewById(R.id.followingCount);
                        TextView followerscountBox = (TextView)findViewById(R.id.followersCount);
                        TextView descriptionBox = (TextView)findViewById(R.id.status);
                        TextView usernameBox = (TextView)findViewById(R.id.myName);
                        TextView display_nameBox = (TextView)findViewById(R.id.myLastName);
                        ImageView profilePhoto = (ImageView)findViewById(R.id.profilePhoto);

                        if (user.getString("sys_avatar").equals("")) {
                            Picasso.get().load("http://berna-diplom.norox.com.ua/img/no_photo.jpg").into(profilePhoto);
                        }else{
                            Picasso.get().load("http://berna-diplom.norox.com.ua/img/"+user.getString("sys_avatar")).into(profilePhoto);
                        }
                        followingcountBox.setText(followingcount);
                        followerscountBox.setText(followerscount);
                        shopscountBox.setText(shopscount);
                        wallcountBox.setText(Integer.toString(wallcount));
                        usernameBox.setText(name);
                        display_nameBox.setText(lastname);
                        descriptionBox.setText(description);
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.dialogs :

                        Intent settingIntent = new Intent(ProfileActivity.this, DialogActivity.class);
                        startActivity(settingIntent);
                        break;
                }
                return false;
            }
        });
    }
    public void editProfileClicked(View view){
        Intent intent = new Intent(ProfileActivity.this,SettingsActivity.class);
        intent.putExtra("formProfile",true);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

}
