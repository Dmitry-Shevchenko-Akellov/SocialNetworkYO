package com.example.socialnetwork.Profile;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.socialnetwork.Login.LoginActivity;
import com.example.socialnetwork.Profile.ProfileActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.example.socialnetwork.Utils.SessionManager;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private static final int ACTIVITY_NO = 4;
    private static final String TAG = "SettingsActivity";
    private SessionManager session;
    private static final int EDIT_PROFILE_SETTING_POSITION = 0;
    private Context mContext = SettingsActivity.this;
    private ViewPager viewPager;
    private RelativeLayout relativeLayout;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        relativeLayout = (RelativeLayout)findViewById(R.id.rl_settings);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        session = new SessionManager(getApplicationContext());

        setupBottomNavigationView();
        setupSettingsList();
        actionGoBack();

    }


    private void setupSettingsList(){

        ListView listView = (ListView)findViewById(R.id.setting_list);
        ArrayList<String> settingList = new ArrayList<>();
        settingList.add(getString(R.string.EditProfile));
        settingList.add(getString(R.string.SignOut));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,settingList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Settings item selected: " + position);
                //Navigating to item_Settings
                setupViewPager(position);
            }
        });

    }

    private void setupViewPager(int position) {
        db = new SQLiteHandler(getApplicationContext());
        if (position == 0) {

        }
        else if (position == 1) {
            session.setLogin(false);
            db.deleteUsers();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        //  Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);

    }



    private void actionGoBack(){

        ImageView back = (ImageView)findViewById(R.id.setting_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().getBooleanExtra("formProfile",false)){ setupViewPager(EDIT_PROFILE_SETTING_POSITION);}
    }
}

