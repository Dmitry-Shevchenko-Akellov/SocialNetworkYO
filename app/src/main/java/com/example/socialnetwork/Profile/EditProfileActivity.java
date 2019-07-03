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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.Add.AddActivity;
import com.example.socialnetwork.Dialog.DialogActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity{
    private static final String TAG = "EditProfileActivity";
    private SQLiteHandler db;
    private static final int ACTIVITY_NO = 0;
    private Context mContext = EditProfileActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        db = new SQLiteHandler(getApplicationContext());
        setupToolbar();
        setupBottomNavigationView();
        setupProfileData();

        Button upload = (Button)findViewById(R.id.editProfile2);
        Button update = (Button)findViewById(R.id.editProfile3);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, AddActivity.class);
                startActivity(intent);            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
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
                        String email = user.getString("sys_email");

                        EditText usernameBox = (EditText)findViewById(R.id.setName);
                        EditText display_nameBox = (EditText)findViewById(R.id.setSurname);
                        ImageView profilePhoto = (ImageView)findViewById(R.id.profilePhoto2);
                        EditText profileMail = (EditText)findViewById(R.id.setMail);

                        usernameBox.setText(name);
                        display_nameBox.setText(lastname);
                        profileMail.setText(email);
                        if (user.getString("sys_avatar").equals("")) {
                            Picasso.get().load("http://berna-diplom.norox.com.ua/img/no_photo.jpg").into(profilePhoto);
                        }else{
                            Picasso.get().load("http://berna-diplom.norox.com.ua/img/"+user.getString("sys_avatar")).into(profilePhoto);
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

    private void updateProfile() {
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "req_login";
        final String myid = db.getUserDetails().get("main_id");
        Log.d("","myid = " + myid);
        EditText usernameBox = (EditText)findViewById(R.id.setName);
        EditText display_nameBox = (EditText)findViewById(R.id.setSurname);
        EditText profileMail = (EditText)findViewById(R.id.setMail);
        String name = usernameBox.getText().toString().trim();
        String surname = display_nameBox.getText().toString().trim();
        String email = profileMail.getText().toString().trim();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/update_profile/?id=" + myid +"&"+name+"="+surname+"&email="+email),
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile data Response: "+ myid + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, jObj.toString());
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
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
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
    }
}
