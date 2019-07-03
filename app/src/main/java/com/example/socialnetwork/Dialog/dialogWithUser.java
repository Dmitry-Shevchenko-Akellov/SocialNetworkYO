package com.example.socialnetwork.Dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.Friends.FriendsActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.MessageListAdapter;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

public class dialogWithUser extends AppCompatActivity {
    private Context mContext = dialogWithUser.this;
    private static final String TAG = "dialogWithUser";
    private static final int ACTIVITY_NO = 4;
    private SQLiteHandler db;
    private EditText inputMessage;
    ArrayList<String> dialog_Photo_you;
    ArrayList<String> dialog_Name_you;
    ArrayList<String> dialog_Date_you;
    ArrayList<String> dialog_Text_you;
    ArrayList<String> dialog_id_from;
    MessageListAdapter adapter;
    ListView listView;
    private final int FIVE_SECONDS = 1000;
    Handler handler = new Handler();
    String dialogiddd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogiddd = getIntent().getStringExtra("uid");
        setContentView(R.layout.activity_dialog_users);
        setupBottomNavigationView();

        addDialogs(dialogiddd);
        Log.d("TAG", "Dialog id = " + dialogiddd);

        scheduleSendLocation();
    }
    public void scheduleSendLocation() {
        handler.postDelayed(new Runnable() {
            public void run() {
                addDialogs(dialogiddd);
                handler.postDelayed(this, FIVE_SECONDS);
            }
        }, FIVE_SECONDS);
    }
    private void addMessage(String userid, String dialogid) {
        String tag_string_req = "addMessage";

        inputMessage = (EditText) findViewById(R.id.inputMessage);
        String message = inputMessage.getText().toString().trim();
        String newMess = null;
        try {
            newMess = URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(!message.equals("")) {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    ("http://berna-diplom.norox.com.ua/api/add_message/?"+dialogid+"="+newMess+"&id="+userid), new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            Log.d(TAG, "Add message " + strReq);
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            inputMessage.setText("");
            //reload();
        }
        else {

        }
    }
    private void addDialogs(final String dialogUserid) {
        db = new SQLiteHandler(getApplicationContext());
        String tag_string_req = "req_login";
        final String myid = db.getUserDetails().get("main_id");

        ImageButton sentMessage = (ImageButton)findViewById(R.id.sentMessage);
        sentMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage(myid, dialogUserid);
            }
        });

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ("http://berna-diplom.norox.com.ua/api/get_messages/?" + myid + "=" + dialogUserid), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SHops data Response: " + response.toString());
                try {
                    final JSONObject jObj = new JSONObject(response);
                    boolean error = false;
                    Log.d(TAG, jObj.toString());
                    if (!error) {
                        int dialogscount = jObj.getInt("counter");

                        if (dialogscount == 0) {

                        }
                        else {

                            dialog_Photo_you  = new ArrayList<>();
                            dialog_Name_you = new ArrayList<>();
                            dialog_Date_you  = new ArrayList<>();
                            dialog_Text_you  = new ArrayList<>();
                            dialog_id_from  = new ArrayList<>();
                            String photoDefault = ("http://berna-diplom.norox.com.ua/img/no_photo.jpg");
                            JSONArray dialogsId = jObj.getJSONArray("from_id");
                            JSONArray dialogsText = jObj.getJSONArray("text");
                            JSONArray dialogsDate = jObj.getJSONArray("date");
                            JSONArray dialogsAvatar = jObj.getJSONArray("avatar");
                            JSONArray dialogsName = jObj.getJSONArray("name");
                            JSONArray dialogsLastname = jObj.getJSONArray("lastname");

                            for (int x = 0; x < dialogscount; x++) {
                                if(dialogsAvatar.getString(x).equals("")) {
                                    dialog_id_from.add(dialogsId.getString(x));
                                    dialog_Name_you.add(dialogsName.getString(x) + " " + dialogsLastname.getString(x));
                                    dialog_Text_you.add(dialogsText.getString(x));
                                    dialog_Date_you.add(dialogsDate.getString(x));
                                    dialog_Photo_you.add(photoDefault);
                                }
                                else {
                                    dialog_id_from.add(dialogsId.getString(x));
                                    dialog_Name_you.add(dialogsName.getString(x) + " " + dialogsLastname.getString(x));
                                    dialog_Text_you.add(dialogsText.getString(x));
                                    dialog_Date_you.add(dialogsDate.getString(x));
                                    dialog_Photo_you.add("http://berna-diplom.norox.com.ua/img/"+dialogsAvatar.getString(x));
                                }
                            }
                            Log.d(TAG, "dialogList = " + dialog_id_from);
                            Log.d(TAG, "dialogList = " + dialog_Photo_you);
                            Log.d(TAG, "dialogPhoto = " + dialog_Text_you);
                            Log.d(TAG, "dialogName = " + dialog_Date_you);
                            Log.d(TAG, "dialogDate = " + dialog_Name_you);

                            setupMessageList(dialog_id_from, dialog_Photo_you, dialog_Text_you, dialog_Date_you, dialog_Name_you);
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

    private void setupMessageList(ArrayList<String> dialog_id_from, ArrayList<String> dialog_photo_you, ArrayList<String> dialog_text_you,
                                  ArrayList<String> dialog_date_you, ArrayList<String> dialog_name_you) {

        listView = findViewById(R.id.messages_view);
        adapter = new MessageListAdapter(this, R.layout._message_my, R.layout._their_message, dialog_id_from, dialog_photo_you, dialog_text_you, dialog_date_you,
                dialog_name_you);
        listView.setAdapter(adapter);
        listView.setSelection(listView.getAdapter().getCount()-1);
    }
    public void reload() {
        finish();
        startActivity(getIntent());
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
