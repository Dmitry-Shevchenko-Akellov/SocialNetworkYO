package com.example.socialnetwork.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialnetwork.Dialog.DialogActivity;
import com.example.socialnetwork.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserListAdapter extends ArrayAdapter<String> {
        private static final String TAG = "UserListAdapter";
        private Context context;
        private int layoutResource;
        private ArrayList<String> following;
        private ArrayList<String> image;
        private ArrayList<String> id;
        private ArrayList<String> status;

        public UserListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects, ArrayList<String> object1,
                               ArrayList<String> object2, ArrayList<String> object3) {
            super(context, resource, objects);
            this.context = context;
            layoutResource = resource;
            this.following = objects;
            this.image = object1;
            this.status = object2;
            this.id = object3;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView==null) {
                convertView = LayoutInflater.from(context).inflate(layoutResource, parent, false);
            }


            final String user = getItem(position);
            final TextView userName = convertView.findViewById(R.id.username);
            TextView userStatus = convertView.findViewById(R.id.status);
            final TextView userId = convertView.findViewById(R.id.id_for_calling_and_message);
            final ImageView profileImage = convertView.findViewById(R.id.profile_image);
            userName.setText(following.get(position));
            Picasso.get().load(image.get(position)).into(profileImage);
            userStatus.setText(status.get(position));
            userId.setText(id.get(position));

            CardView boxMess = (CardView)convertView.findViewById(R.id.messBox);
            boxMess.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent ProfileUser = new Intent(context, DialogActivity.class);
                    context.startActivity(ProfileUser);
                }
            });


            //GlideImageLoader.loadImageWithTransition(mContext,url,gridImage,progressBar);

            return convertView;
        }
}
