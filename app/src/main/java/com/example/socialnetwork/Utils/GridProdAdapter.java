package com.example.socialnetwork.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialnetwork.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GridProdAdapter extends ArrayAdapter<String> {
    private static final String TAG = "UserListAdapter";
    private Context mContext;
    private int layoutResource;
    private ArrayList<String> following;
    private ArrayList<String> image;
    private ArrayList<String> status;
    private ArrayList<String> cost;

    public GridProdAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects, ArrayList<String> object1, ArrayList<String> object2, ArrayList<String> object3) {
        super(context, resource, objects);
        mContext = context;
        layoutResource = resource;
        this.image = objects;
        this.following = object1;
        this.status = object2;
        this.cost = object3;
        Log.d("TAG", "SUSUka = " + following);
        Log.d("TAG", "SUSUka = " + image);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null) {
            convertView = LayoutInflater.from(mContext).inflate(layoutResource, parent, false);
        }

        String user = getItem(position);
        Log.d("TAG", "SUSUka = " + user);
        Log.d("TAG", "SUSUka = " + following.get(position));
        Log.d("TAG", "SUSUka = " + image.get(position));
        TextView userName = convertView.findViewById(R.id.prodName);
        TextView userStatus = convertView.findViewById(R.id.prodstatus);
        final ImageView profileImage = convertView.findViewById(R.id.prodImg);
        TextView prodStatus = convertView.findViewById(R.id.cost);
        userName.setText(following.get(position));
        Picasso.get().load(image.get(position)).into(profileImage);
        userStatus.setText(status.get(position));
        prodStatus.setText(cost.get(position));



        //GlideImageLoader.loadImageWithTransition(mContext,url,gridImage,progressBar);

        return convertView;
    }
}
