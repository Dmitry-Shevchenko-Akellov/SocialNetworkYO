package com.example.socialnetwork.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.socialnetwork.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MessageListAdapter extends ArrayAdapter<String> {
        private static final String TAG = "MessageListAdapter";
        private Context mContext;
        private int layoutResource;
        private int layoutResource2;
        private ArrayList<String> userid;
        private ArrayList<String> username;
        private ArrayList<String> userimage;
        private ArrayList<String> userdate;
        private ArrayList<String> usertext;

    public MessageListAdapter(@NonNull Context context, int resource, int resource2, @NonNull ArrayList<String> objects,
                                  ArrayList<String> object1, ArrayList<String> object2, ArrayList<String> object3, ArrayList<String> object4) {
            super(context, resource, resource2, objects);
            mContext = context;
            layoutResource = resource;
            layoutResource2 = resource2;
            this.userid = objects;
            this.userimage = object1;
            this.usertext = object2;
            this.userdate = object3;
            this.username = object4;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            Log.d("TAG", "SUSUka name = " + username.get(position));
            Log.d("TAG", "SUSUka photo = " + userimage.get(position));
            Log.d("TAG", "SUSUka date = " + userdate.get(position));
            Log.d("TAG", "SUSUka text = " + usertext.get(position));
            Log.d("TAG", "SUSUka id = " + userid.get(position));



            String user = userid.get(position);
            String usImg = userimage.get(position);
            String usDate = userdate.get(position);
            String usText = usertext.get(position);
            SQLiteHandler db = new SQLiteHandler(getContext());
            String muid = db.getUserDetails().get("main_id");
            Log.d("TAG", "SUSUka id my = " + muid);
            Log.d("TAG", "SUSUka2 id my = " + user);
            /*if(user.equals(muid)) {
                if(convertView==null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout._message_my, parent, false);
                }
                TextView textMy = (TextView)convertView.findViewById(R.id.message_body_my);
                TextView dateMessageMy = (TextView)convertView.findViewById(R.id.dateMy);
                Log.d("TAG", "ERRRRRRRORRRRRRRR = " + textMy);
                textMy.setText(usText);
                dateMessageMy.setText(usDate);
            }
            else {
                if(convertView==null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout._their_message, parent, false);
                }
                TextView textYou = convertView.findViewById(R.id.message_body_you);
                ImageView youImage = convertView.findViewById(R.id.avatar);
                TextView dateMessageYou = convertView.findViewById(R.id.dateYou);
                textYou.setText(usText);
                Picasso.get().load(usImg).into(youImage);
                dateMessageYou.setText(usDate);
            }*/

            if(user.equals(muid)) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout._message_my, parent, false);
                TextView textMy = (TextView)convertView.findViewById(R.id.message_body_my);
                TextView dateMessageMy = (TextView)convertView.findViewById(R.id.dateMy);
                Log.d("TAG", "ERRRRRRRORRRRRRRR = " + textMy);
                textMy.setText(usText);
                dateMessageMy.setText(usDate);
            }
            else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout._their_message, parent, false);
                TextView textYou = convertView.findViewById(R.id.message_body_you);
                ImageView youImage = convertView.findViewById(R.id.avatar);
                TextView dateMessageYou = convertView.findViewById(R.id.dateYou);
                textYou.setText(usText);
                Picasso.get().load(usImg).into(youImage);
                dateMessageYou.setText(usDate);
            }
            return convertView;
        }
}
