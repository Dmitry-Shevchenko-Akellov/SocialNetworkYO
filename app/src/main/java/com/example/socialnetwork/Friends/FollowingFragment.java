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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.Dialog.DialogActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.example.socialnetwork.Utils.UserListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FollowingFragment extends Fragment {
    final String TAG = "FollowingFragment";
    Context mContext;
    GridView listView;
    ArrayList<String> followingList;
    ArrayList<String> followingStatus;
    ArrayList<String> followingPhoto;
    ArrayList<String> followingId;
    UserListAdapter adapter;
    private SQLiteHandler db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_following,container,false);
        listView = view.findViewById(R.id.followingList);
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
                                following = jObj.getJSONObject(Long.toString(id));
                                userid = following.getString("user_friends_id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //UID!!!!!!!!!!!!!!!!!!!!!!!!!
                            Log.d("TAG", "Frieds = " + following);
                            Intent ProfileUser = new Intent(mContext, UserProfileActivity.class);
                            ProfileUser.putExtra("uid", userid);
                            startActivity(ProfileUser);
                        }
                    });
                    Log.d(TAG, jObj.toString());
                    if (!error) {
                        int followingcount = jObj.getInt("user_friends_count");
                        String follower_id = null;

                        if (followingcount == 0) {

                        }
                        else {
                            followingList  = new ArrayList<>();
                            followingPhoto  = new ArrayList<>();
                            followingStatus  = new ArrayList<>();
                            followingId  = new ArrayList<>();
                            for (int x = 0; x < followingcount; x++) {
                                String count = Integer.toString(x);
                                JSONObject follower = jObj.getJSONObject(count);
                                follower_id = follower.getString("user_friends_id");
                                db.addFollowing(follower_id, count);
                                String nick = (follower.getString("user_friends_name") + " " + follower.getString("user_friends_lastname"));
                                String photo = (follower.getString("user_friends_avatar"));
                                String status = (follower.getString("user_friends_status"));
                                String photoDefault = ("http://berna-diplom.norox.com.ua/img/no_photo.jpg");
                                Log.d(TAG, "User count: " + nick);
                                Log.d(TAG, "User count: " + photo);
                                if(!follower.getString("user_friends_avatar").equals("")) {
                                    followingList.add(nick);
                                    followingStatus.add(status);
                                    followingPhoto.add("http://berna-diplom.norox.com.ua/img/"+photo);
                                    followingId.add(follower_id);
                                }
                                else {
                                    followingList.add(nick);
                                    followingStatus.add(status);
                                    followingPhoto.add(photoDefault);
                                    followingId.add(follower_id);
                                }
                                Log.d(TAG, "followingList = " + followingList);
                                Log.d(TAG, "followingPhoto = " + followingPhoto);
                            }
                            setupListFollowing(followingList, followingPhoto, followingStatus, followingId);
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

    private void setupListFollowing(ArrayList<String> followingList, ArrayList<String> followingPhoto, ArrayList<String> followingStatus,
                                    ArrayList<String> followingId) {
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/2;
        listView.setColumnWidth(imageWidth);
        adapter = new UserListAdapter(mContext,R.layout.layout_friends_view,followingList, followingPhoto, followingStatus, followingId);
        listView.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }



}
