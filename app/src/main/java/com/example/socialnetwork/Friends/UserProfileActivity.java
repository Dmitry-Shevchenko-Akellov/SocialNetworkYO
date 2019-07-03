package com.example.socialnetwork.Friends;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.Dialog.DialogActivity;
import com.example.socialnetwork.Profile.WallList;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";
    private static final int ACTIVITY_NO = 0;
    private Context mContext = UserProfileActivity.this;
    private ArrayList<String> mediaUrls;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String userid = getIntent().getStringExtra("uid");
        Log.d("TAG", "User id = " + userid);
        setContentView(R.layout.profile_exp);
        setupToolbar();
        setupBottomNavigationView();
        db = new SQLiteHandler(getApplicationContext());

        Button addFriends = (Button)findViewById(R.id.follow);
        Button addDialog = (Button)findViewById(R.id.message);
        String ifId = db.getUserFollowing(userid).get("main_id");
        if(ifId.equals("1")) {
            addFriends.setText("U n f o l l o w");
            setupDataProfile(userid);
        }
        else {
            addFriends.setText("F o l l o w");
            setupDataProfile(userid);
        }
        LinearLayout box = (LinearLayout)findViewById(R.id.boxPost);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, WallList.class);
                intent.putExtra("uid", userid);
                startActivity(intent);
                finish();
            }
        });
        addDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog(userid);
            }
        });

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button f = (Button)v;
                String follow = f.getText().toString().toUpperCase();
                Log.d("TAG", "User id = " + follow);
                if(follow.equals("F O L L O W")) {
                    addFriends(userid);
                }
                else if(follow.equals("U N F O L L O W")) {
                    deleteFriends(userid);
                }
            }
        });
    }

    private void setupDataProfile(String userid) {
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/get_user_info/?" + userid + "=" + userid + "&act=wall"), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile data Response: " + response);

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



                        TextView wallcountBox = (TextView)findViewById(R.id.postCount);
                        TextView shopscountBox = (TextView)findViewById(R.id.shopCount);
                        TextView followingcountBox = (TextView)findViewById(R.id.followingCount);
                        TextView followerscountBox = (TextView)findViewById(R.id.followersCount);
                        TextView descriptionBox = (TextView)findViewById(R.id.status);
                        TextView usernameBox = (TextView)findViewById(R.id.myName);
                        TextView display_nameBox = (TextView)findViewById(R.id.myLastName);
                        ImageView profilePhoto = (ImageView)findViewById(R.id.profilePhoto);

                        mediaUrls = new ArrayList<>();
                        if (wallcount == 0) {
                            //setupImageGrid(mediaUrls);
                        }
                        else {
                            for (int x = 0; x < wallcount; x++) {
                                String count = Integer.toString(x);
                                JSONObject wall = jObj.getJSONObject(count);
                                if (wall.getString("wall_back_set").equals("")) {
                                    mediaUrls.add("http://berna-diplom.norox.com.ua/img/no_photo.jpg");
                                }
                                else {
                                    mediaUrls.add(wall.getString("wall_back_set"));
                                }
                                Log.d(TAG, "MediaUrl = " + mediaUrls);
                            }
                        }
                        if (user.getString("sys_avatar").equals("")) {
                            Picasso.get().load("http://berna-diplom.norox.com.ua/img/no_photo.jpg").into(profilePhoto);
                        }else{
                            Picasso.get().load("http://berna-diplom.norox.com.ua/img/" + user.getString("sys_avatar")).into(profilePhoto);
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
        Log.e(TAG, "Login Error: " + userid);
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

    private void addDialog(String userid) {
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "addDialog";
        final String myid = db.getUserDetails().get("main_id");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/create_dialog/?" + myid + "=" + userid+"&text=Hi!"), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Log.d(TAG, "Add dialog " + strReq);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        finish();
        Intent ProfileUser = new Intent(mContext, DialogActivity.class);
        startActivity(ProfileUser);
    }

    private void addFriends(String userid) {
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "addFriends";
        final String myid = db.getUserDetails().get("main_id");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/add_friend/?" + myid + "=" + userid), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Log.d(TAG, "Add friends " + strReq);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        //reload(userid);
        finish();
        Intent ProfileUser = new Intent(mContext, FriendsActivity.class);
        startActivity(ProfileUser);
    }

    private void deleteFriends(String userid) {
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "deleteFriends";
        final String myid = db.getUserDetails().get("main_id");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/delete_friend/?" + myid + "=" + userid), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Log.d(TAG, "Delete friends " + strReq);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        db.deleteFollowing(userid);
        Log.d("TAG", "User id = " + userid);
        Button addFriends = (Button)findViewById(R.id.follow);
        if(db.getUserFollowing(userid).get("main_id").equals("1")) {
            addFriends.setText("U N F O L L O W");
        }
        else {
            addFriends.setText("F O L L O W");
        }
        Log.d("TAG", "User id = " + userid);
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

    }
}
