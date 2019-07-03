package com.example.socialnetwork.Friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.socialnetwork.Dialog.DialogActivity;
import com.example.socialnetwork.Profile.ProfileActivity;
import com.example.socialnetwork.Profile.WallList;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.AppController;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.example.socialnetwork.Utils.SectionPagerAdapter;

import java.util.Objects;

public class FriendsActivity extends AppCompatActivity {

    private static final String TAG = "FriendsActivity";
    private static final int ACTIVITY_NO = 1;
    private Context mContext = FriendsActivity.this;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        setupBottomNavigationView();
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        db.deleteFollowingErs();
        setupViewPager();


    }

    private void setupViewPager() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(FriendsActivity.this.getSupportFragmentManager());
        adapter.addFragment(new FollowingFragment());
        adapter.addFragment(new FollowersFragment());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(getString(R.string.node_following));
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(getString(R.string.node_followers));
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
