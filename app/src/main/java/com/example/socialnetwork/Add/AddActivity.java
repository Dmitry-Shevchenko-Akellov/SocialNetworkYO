package com.example.socialnetwork.Add;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.CheckPermissions;
import com.example.socialnetwork.Utils.DirectoryScanner;
import com.example.socialnetwork.Utils.SectionPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AddActivity";
    public TabLayout tabLayout;
    private Context mContext;

    private final int requestCode = 3;
    private final String[] permissionList =  {
            Manifest.permission.READ_EXTERNAL_STORAGE
            ,Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.CAMERA
    };
//api/add_wall/?"+user+"="+user+"&"+title+"="+descr+"&"+color_type+"="+color+"&cards="+cards
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mContext = AddActivity.this;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(AddActivity.this));


        if(Build.VERSION.SDK_INT >= 23&&!CheckPermissions.hasPermissions(mContext,permissionList)) {

            //Toast.makeText(mContext, "Please give the required permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,permissionList, requestCode);
        }else {
            new DirectoryScanner().execute(Environment.getExternalStorageDirectory().getAbsolutePath());
            setupViewPager();
            Log.d(TAG,"Scanning Files");
        }


    }


    private void setupViewPager() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment()); //index 0
        adapter.addFragment(new PhotoFragment());  //index 1
        adapter.addFragment(new VideoFragment()); //index 2
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(getString(R.string.GalleryFragment));
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(getString(R.string.PhotoFragment));
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText(getString(R.string.VideoFragment));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case 3 :
                if(CheckPermissions.hasPermissions(mContext,permissionList)){
                    new DirectoryScanner().execute(Environment.getExternalStorageDirectory().getAbsolutePath());
                    setupViewPager();
                    Log.d(TAG,"Scanning Files");
                }else {
                    finish();
                }
                break;
        }
    }


}
