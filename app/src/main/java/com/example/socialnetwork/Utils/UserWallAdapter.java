package com.example.socialnetwork.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialnetwork.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserWallAdapter extends ArrayAdapter<String> {
        private static final String TAG = "UserListAdapter";
        private Context mContext;
        private int layoutResource;
        private ArrayList<String> name;
        private ArrayList<String> image;
    private ArrayList<String> status;

        public UserWallAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects, ArrayList<String> object1, ArrayList<String> object2) {
            super(context, resource, objects);
            mContext = context;
            layoutResource = resource;
            this.name = objects;
            this.status = object1;
            this.image = object2;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView==null) {
                convertView = LayoutInflater.from(mContext).inflate(layoutResource, parent, false);
            }

            String user = getItem(position);
            TextView userName = convertView.findViewById(R.id.wallName);
            TextView userStatus = convertView.findViewById(R.id.wallstatus);
            final ImageView profileImage = convertView.findViewById(R.id.wallImg);
            userName.setText(name.get(position));
            Picasso.get().load(image.get(position)).into(profileImage);
            userStatus.setText(status.get(position));



            //GlideImageLoader.loadImageWithTransition(mContext,url,gridImage,progressBar);

            return convertView;
        }
}
