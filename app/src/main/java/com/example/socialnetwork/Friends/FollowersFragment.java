package com.example.socialnetwork.Friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.Profile.ProfileActivity;
import com.example.socialnetwork.Profile.WallList;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.example.socialnetwork.Utils.UserListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FollowersFragment extends Fragment {
    final String TAG = "FollowersFragment";
    Context mContext;
    GridView listView;
    ArrayList<String> followerList;
    ArrayList<String> followerPhoto;
    ArrayList<String> followerStatus;
    ArrayList<String> followerId;
    UserListAdapter adapter;
    private SQLiteHandler db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_followers,container,false);
        listView = view.findViewById(R.id.followersList);
        addUser();
        return view;

    }

    private void addUser() {
        db = new SQLiteHandler(getActivity());
        String tag_string_req = "req_login";
        final String myid = db.getUserDetails().get("main_id");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/get_user_info/?" + myid + "=" + myid + "&act=friends"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Following data Response: " + response.toString());
                try {
                    final JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            JSONObject following = null;
                            String userid = null;
                            Log.d("TAG", "itemClick: position = " + position + ", id = " + id + ", " + parent.getAdapter().getItem(position));
                            try {
                                following = jObj.getJSONObject("follower-"+ id);
                                userid = following.getString("user_followers_id");
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
                        int followingcount = jObj.getInt("user_followers_count");
                        String following_id = null;

                        if (followingcount == 0) {

                        }
                        else {
                            followerList  = new ArrayList<>();
                            followerPhoto  = new ArrayList<>();
                            followerStatus  = new ArrayList<>();
                            followerId  = new ArrayList<>();
                            for (int x = 0; x < followingcount; x++) {
                                String count = Integer.toString(x);
                                JSONObject following = jObj.getJSONObject("follower-"+count);
                                following_id = following.getString("user_followers_id");
                                db.addFollower(following_id, count);
                                String nick = (following.getString("user_followers_name") + " " + following.getString("user_followers_lastname"));
                                String photo = (following.getString("user_followers_avatar"));
                                String status = (following.getString("user_followers_status"));
                                String photoDefault = ("http://berna-diplom.norox.com.ua/img/no_photo.jpg");
                                Log.d(TAG, "User count: " + nick);
                                Log.d(TAG, "User count: " + photo);
                                if(!following.getString("user_followers_avatar").equals("")) {
                                    followerId.add(following_id);
                                    followerList.add(nick);
                                    followerStatus.add(status);
                                    followerPhoto.add("http://berna-diplom.norox.com.ua/img/"+photo);
                                }
                                else {
                                    followerId.add(following_id);
                                    followerList.add(nick);
                                    followerStatus.add(status);
                                    followerPhoto.add(photoDefault);
                                }
                                Log.d(TAG, "followerList = " + followerList);
                                Log.d(TAG, "followerPhoto = " + followerPhoto);
                            }
                            setupListFollowing(followerList, followerPhoto, followerStatus, followerId);
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

    private void setupListFollowing(ArrayList<String> followerList, ArrayList<String> followerPhoto, ArrayList<String> followerStatus,
                                    ArrayList<String> followerId) {
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/2;
        listView.setColumnWidth(imageWidth);
        adapter = new UserListAdapter(mContext,R.layout.layout_friends_view,followerList, followerPhoto, followerStatus, followerId);
        listView.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
