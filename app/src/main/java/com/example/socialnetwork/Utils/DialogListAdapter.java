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

import java.util.ArrayList;

public class DialogListAdapter extends ArrayAdapter<String> {
        private static final String TAG = "UserListAdapter";
        private Context mContext;
        private int layoutResource;
        private ArrayList<String> following;
        private ArrayList<String> image;
        private ArrayList<String> date;
        private ArrayList<String> text;

        public DialogListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects,
                                 ArrayList<String> object1, ArrayList<String> object2, ArrayList<String> object3) {
            super(context, resource, objects);
            mContext = context;
            layoutResource = resource;
            this.image = objects;
            this.following = object1;
            this.date = object2;
            this.text = object3;
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
            TextView dialogName = convertView.findViewById(R.id.dialog_username);
            final ImageView dialogImage = convertView.findViewById(R.id.dialog_image);
            TextView dateMessage = convertView.findViewById(R.id.dateOfMessage);
            TextView textMessage = convertView.findViewById(R.id.textMessage);
            dialogName.setText(following.get(position));
            textMessage.setText(text.get(position));
            Picasso.get().load(image.get(position)).into(dialogImage);
            dateMessage.setText(date.get(position));


            //GlideImageLoader.loadImageWithTransition(mContext,url,gridImage,progressBar);

            return convertView;
        }
}
