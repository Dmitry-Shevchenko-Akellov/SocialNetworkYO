package com.example.socialnetwork.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.socialnetwork.Add.AddActivity;
import com.example.socialnetwork.Friends.FriendsActivity;
import com.example.socialnetwork.Profile.ProfileActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Search.SearchActivity;
import com.example.socialnetwork.Shops.ShopsActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.home:
                        if(callingActivity.getClass()!=ProfileActivity.class) {
                            Intent intent1 = new Intent(context, ProfileActivity.class);//ACTIVITY_NUM = 0
                            callingActivity.finish();
                            context.startActivity(intent1);
                            callingActivity.overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.friends:
                        if(callingActivity.getClass()!= FriendsActivity.class) {
                            Intent intent2 = new Intent(context, FriendsActivity.class);//ACTIVITY_NUM = 1
                            callingActivity.finish();
                            //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent2);
                            callingActivity.overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.add:
                        if(callingActivity.getClass()!= AddActivity.class) {
                            Intent intent3 = new Intent(context, AddActivity.class);//ACTIVITY_NUM = 2
                            callingActivity.finish();
                            context.startActivity(intent3);
                            callingActivity.overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.search:
                        if(callingActivity.getClass()!= SearchActivity.class) {
                            Intent intent4 = new Intent(context, SearchActivity.class);//ACTIVITY_NUM = 3
                            callingActivity.finish();
                            //intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent4);
                            callingActivity.overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.shops:
                        if(callingActivity.getClass()!= ShopsActivity.class) {
                            Intent intent5 = new Intent(context, ShopsActivity.class);//ACTIVITY_NUM = 4
                            callingActivity.finish();
                            //intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent5);
                            callingActivity.overridePendingTransition(0, 0);
                        }
                        break;
                }
                return false;
            }
        });
    }
}
