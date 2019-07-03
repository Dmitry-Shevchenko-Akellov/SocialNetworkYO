package com.example.socialnetwork.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.Friends.UserProfileActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.example.socialnetwork.Utils.UserListAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    private static final int ACTIVITY_NO = 3;
    private Context mContext = SearchActivity.this;
    private UserListAdapter listAdapter;
    private ArrayList<String> searchList;
    private ArrayList<String> searchPhoto;
    private ArrayList<String> searchStatus;
    private ArrayList<String> searchId;
    private ProgressBar progressBar;
    GridView listView;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = findViewById(R.id.searchList);
        EditText mSearch = findViewById(R.id.search);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        searchList = new ArrayList<>();
        listView.setAdapter(listAdapter);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim().toLowerCase();

                if (keyword.equals("")) {
                    listView.setAdapter(null);
                }
                else {
                    listView.setAdapter(null);
                    getSearch(keyword);
                }
            }
        });
        setupBottomNavigationView();
    }

    private void getSearch(String keyword) {
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "req_login";
        String myid = db.getUserDetails().get("main_id");
        Log.d(TAG, "User count: -" + keyword+"-");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/get_search/?"+myid+"="+keyword+"&data=json"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Search data Response: " + response);
                try {
                    final JSONObject jObj = new JSONObject(response);
                    boolean error = false;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            JSONObject following = null;
                            String userid = null;
                            Log.d("TAG", "itemClick: position = " + position + ", id = " + id + ", " + parent.getAdapter().getItem(position));
                            try {
                                following = jObj.getJSONObject("friend-"+ id);
                                userid = following.getString("unique_id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //UID!!!!!!!!!!!!!!!!!!!!!!!!!
                            Log.d("TAG", "Follower = " + following);
                            Intent ProfileUser = new Intent(mContext, UserProfileActivity.class);
                            ProfileUser.putExtra("uid", userid);
                            startActivity(ProfileUser);
                        }
                    });
                    Log.d(TAG, jObj.toString());
                    if (!error) {
                        int searchcount = jObj.getInt("count");

                        if (searchcount == 0) {

                        }
                        else {
                            searchList  = new ArrayList<>();
                            searchPhoto  = new ArrayList<>();
                            searchStatus  = new ArrayList<>();
                            searchId  = new ArrayList<>();
                            for (int x = 0; x <= searchcount; x++) {
                                String count = Integer.toString(x);
                                JSONObject user = jObj.getJSONObject("friend-"+count);
                                String search_Id = user.getString("unique_id");
                                String nick = (user.getString("main_name") + " " + user.getString("main_lastname"));
                                String photo = (user.getString("sys_avatar"));
                                String status = (user.getString("b_rank"));
                                String photoDefault = ("http://berna-diplom.norox.com.ua/img/no_photo.jpg");
                                Log.d(TAG, "User count: " + nick);
                                Log.d(TAG, "User count: " + photo);
                                if(!user.getString("sys_avatar").equals("")) {
                                    searchId.add(search_Id);
                                    searchList.add(nick);
                                    searchStatus.add(status);
                                    searchPhoto.add("http://berna-diplom.norox.com.ua/img/"+photo);
                                }
                                else {
                                    searchId.add(search_Id);
                                    searchList.add(nick);
                                    searchPhoto.add(photoDefault);
                                    searchStatus.add(status);
                                }
                                Log.d(TAG, "followingList = " + searchList);
                                Log.d(TAG, "followingPhoto = " + searchPhoto);
                            }
                        }
                        setupListFollowing(searchList, searchPhoto, searchStatus, searchId);
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
        Log.d(TAG, "User count: -" + strReq+"-");

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void setupListFollowing(ArrayList<String> searchList, ArrayList<String> searchPhoto, ArrayList<String> searchStatus,
                                    ArrayList<String> searchId) {
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/2;
        listView.setColumnWidth(imageWidth);
        listAdapter = new UserListAdapter(mContext,R.layout.layout_friends_view,searchList, searchPhoto, searchStatus, searchId);
        listView.setAdapter(listAdapter);

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
