package com.example.socialnetwork.Add;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialnetwork.Profile.ProfileActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Shops.AddShopActivity;
import com.example.socialnetwork.Shops.ShopsActivity;
import com.example.socialnetwork.Utils.GlideImageLoader;
import com.example.socialnetwork.Utils.MediaFilesScanner;
import com.example.socialnetwork.Utils.SQLiteHandler;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NextScreenActivity extends AppCompatActivity {

    private Context mContext = NextScreenActivity.this;
    Activity mActivity = NextScreenActivity.this;
    private static final String TAG = "NEXT_SCREEN_ACTIVITY";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String path;
    private final int GALLERY = 1;
    private long image_count,video_count;
    private String upload_URL = "http://berna-diplom.norox.com.ua/progs/upload_new_avatar_mobile.php";

    JSONObject jsonObject;
    private ImageView imageView;
    private TextView sharePost;
    RequestQueue rQueue;
    private SQLiteHandler db;
    private Button btnU;
    private Button btnImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_screen);

        imageView = (ImageView)findViewById(R.id.preview_image);

        Intent intent = getIntent();
        //path = intent.getStringExtra(getString(R.string.filePath));
        //Uri uri = Uri.parse(path);
        //Log.e("imgname Here", uri.toString());
        requestMultiplePermissions();
        goBack();
        setupToolbar();
        btnImg = findViewById(R.id.getIm);
        btnImg.setOnClickListener(new View.OnClickListener() {
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
                    Bitmap bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imageView.setImageBitmap(bitmap);
                    goToUpload(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(NextScreenActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void goToUpload (final Bitmap bitmap) {
        btnU = (Button)findViewById(R.id.upload2);
        btnU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap img = bitmap;
                uploadImage(img);
            }
        });
    }

    private void uploadImage(Bitmap bitmap){

        db = new SQLiteHandler(getApplicationContext());
        String myid = db.getUserDetails().get("main_id");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            jsonObject.put("name", imgname);
            jsonObject.put("id", myid);
            jsonObject.put("image", encodedImage);
            Log.e("imgname Here", imgname);
            Log.e("myid Here", myid);
            Log.e("encodedImage Here", encodedImage);
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("aaaaaaabbbbb ", jsonObject.toString());
                        rQueue.getCache().clear();
                        Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Eror ===", volleyError.toString());

            }
        });

        rQueue = Volley.newRequestQueue(NextScreenActivity.this);
        rQueue.add(jsonObjectRequest);
        Log.e("aaabbbb === ", jsonObjectRequest.toString());
        Intent ShopUser = new Intent(mContext, ProfileActivity.class);
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
    private void goBack(){
        ImageView back_view = (ImageView) findViewById(R.id.share_back);
        back_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }


}
