package com.example.socialnetwork.Dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.Friends.UserProfileActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Shops.ShopsActivity;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.DialogListAdapter;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.example.socialnetwork.Utils.UserListAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class DialogActivity extends AppCompatActivity {
    private static final String TAG = "ShopsActivity";
    private static final int ACTIVITY_NO = 4;
    private Context mContext = DialogActivity.this;
    ListView listView;
    ArrayList<String> dialog_Id;
    ArrayList<String> dialog_Photo;
    ArrayList<String> dialog_Name;
    ArrayList<String> dialog_Date;
    ArrayList<String> dialog_Text;
    DialogListAdapter adapter;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);
        setupBottomNavigationView();

        addDialogs();
    }

    private void addDialogs() {
        db = new SQLiteHandler(getApplicationContext());
        listView = findViewById(R.id.dialogs_list);
        String tag_string_req = "req_login";
        final String myid = db.getUserDetails().get("main_id");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/get_user_dialogs/?id=" + myid), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SHops data Response: " + response.toString());
                try {
                    final JSONObject jObj = new JSONObject(response);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            db = new SQLiteHandler(getApplicationContext());
                            JSONArray following = null;
                            String dialogid = null;
                            Log.d("TAG", "itemClick: position = " + position + ", id = " + id + ", " + parent.getAdapter().getItem(position));
                            try {
                                following = jObj.getJSONArray("dialog_id");
                                dialogid = following.getString(Integer.parseInt(Long.toString(id)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String pos = Integer.toString(position);
                            db.getDialog(pos);
                            Intent ProfileUser = new Intent(mContext, dialogWithUser.class);
                            ProfileUser.putExtra("uid", dialogid);
                            startActivity(ProfileUser);
                        }
                    });


                    boolean error = false;
                    Log.d(TAG, jObj.toString());
                    if (!error) {
                        int dialogscount = jObj.getInt("counter");

                        if (dialogscount == 0) {

                        }
                        else {

                            dialog_Id  = new ArrayList<>();
                            dialog_Photo  = new ArrayList<>();
                            dialog_Name  = new ArrayList<>();
                            dialog_Date  = new ArrayList<>();
                            dialog_Text  = new ArrayList<>();
                            for (int x = 0; x < dialogscount; x++) {
                                JSONArray dialogsId = jObj.getJSONArray("dialog_id");
                                JSONArray dialogsName = jObj.getJSONArray("mes_name");
                                JSONArray dialogsLastname = jObj.getJSONArray("mes_lastname");
                                JSONArray dialogsAvatar = jObj.getJSONArray("mes_avatar");
                                JSONArray dialogsDate =jObj.getJSONArray("mes_date");
                                JSONArray dialogsText =jObj.getJSONArray("mes_text");

                                String dialogId = dialogsId.getString(x);
                                String dialogName = dialogsName.getString(x) + " " + dialogsLastname.getString(x);
                                String dialogAvatar = dialogsAvatar.getString(x);
                                String dialogDate = dialogsDate.getString(x);
                                String dialogText = dialogsText.getString(x);
                                String photoDefault = ("http://berna-diplom.norox.com.ua/img/no_photo.jpg");

                                String count = Integer.toString(x);

                                db.addDialog(dialogId, count);

                                if(!dialogAvatar.equals("")) {
                                    dialog_Id.add(dialogId);
                                    dialog_Photo.add("http://berna-diplom.norox.com.ua/img/"+dialogAvatar);
                                    dialog_Name.add(dialogName);
                                    dialog_Date.add(dialogDate);
                                    dialog_Text.add(dialogText);
                                }
                                else {
                                    dialog_Id.add(dialogId);
                                    dialog_Photo.add(photoDefault);
                                    dialog_Name.add(dialogName);
                                    dialog_Date.add(dialogDate);
                                    dialog_Text.add(dialogText);
                                }
                                Log.d(TAG, "dialogList = " + dialog_Id);
                                Log.d(TAG, "dialogPhoto = " + dialog_Photo);
                                Log.d(TAG, "dialogName = " + dialog_Name);
                                Log.d(TAG, "dialogDate = " + dialog_Date);
                            }
                            setupListDialogs(dialog_Photo, dialog_Name, dialog_Date, dialog_Text);
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

    private void setupListDialogs(ArrayList<String> dialog_Name, ArrayList<String> dialog_Photo,
                                  ArrayList<String> dialog_Date, ArrayList<String> dialog_Text) {
        adapter = new DialogListAdapter(mContext,R.layout.layout_dialogs_view,dialog_Name, dialog_Photo, dialog_Date, dialog_Text);
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
}
