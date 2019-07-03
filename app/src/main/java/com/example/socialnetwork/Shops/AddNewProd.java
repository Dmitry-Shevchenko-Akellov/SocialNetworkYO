package com.example.socialnetwork.Shops;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.socialnetwork.Utils.SQLiteHandler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AddNewProd extends AppCompatActivity {
    private static final String TAG = "AddProdActivity";
    private static final int ACTIVITY_NO = 4;
    private Context mContext = AddNewProd.this;
    private String upload_URL = "http://berna-diplom.norox.com.ua/progs/mobile_add_prod.php";
    private Button btnImgProd;
    private Button btnUProd;
    private ImageView imageViewProd;
    private ImageView imageView1Prod;
    private EditText EditTextProd;
    private EditText EditText1Prod;
    private EditText EditText2Prod;
    private String unique_id_;
    private final int GALLERY = 1;
    private String upload_URL_toProd = "http://berna-diplom.norox.com.ua/progs/mobile_add_prod.php";
    JSONObject jsonObject;
    RequestQueue rQueue;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_prod);
        unique_id_ = getIntent().getStringExtra("uid");
        setupToolbar();
        setupBottomNavigationView();
        requestMultiplePermissions();

        btnImgProd = findViewById(R.id.getImageProd);
        imageViewProd = (ImageView) findViewById(R.id.imagePreViewProd);
        imageView1Prod = (ImageView) findViewById(R.id.setting_back);

        imageView1Prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addshopIntent = new Intent(AddNewProd.this, UserShopActivity.class);
                startActivity(addshopIntent);
            }
        });

        btnImgProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imageViewProd.setImageBitmap(bitmap);
                    goToUpload(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddNewProd.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void goToUpload (final Bitmap bitmap) {
        btnUProd = findViewById(R.id.uploadProd);
        btnUProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap img = bitmap;
                uploadImage(img);
            }
        });
    }

    private void uploadImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            EditTextProd = (EditText)findViewById(R.id.nameProdGet);
            EditText1Prod = (EditText)findViewById(R.id.descrProdGet);
            EditText2Prod = (EditText)findViewById(R.id.costForProd);

            String name = EditTextProd.getText().toString().trim();
            String descr = EditText1Prod.getText().toString().trim();
            String cost = EditText2Prod.getText().toString().trim();
            jsonObject.put("name", name);
            jsonObject.put("descr", descr);
            jsonObject.put("id", unique_id_);
            jsonObject.put("cost", cost);
            jsonObject.put("image", encodedImage);
            Log.e("name ", name);
            Log.e("descr ", descr);
            Log.e("unique_id_ ", unique_id_);
            Log.e("cost ", cost);
            Log.e("encodedImage ", encodedImage);
            Log.e("JSONObject UID!!!!! ", unique_id_);
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL_toProd, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("aaaaaaa", jsonObject.toString());
                        rQueue.getCache().clear();
                        Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("aaaaaaa", volleyError.toString());

            }
        });

        rQueue = Volley.newRequestQueue(AddNewProd.this);
        rQueue.add(jsonObjectRequest);

        Intent ShopUser = new Intent(mContext, ShopsActivity.class);
        startActivity(ShopUser);

    }
    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


    }
}
