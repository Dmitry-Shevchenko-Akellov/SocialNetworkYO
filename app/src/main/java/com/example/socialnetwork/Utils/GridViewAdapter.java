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
import android.widget.ProgressBar;

import com.example.socialnetwork.R;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private int layoutResource;
    private String append;

    public GridViewAdapter(@NonNull Context context, int resource, String append, ArrayList<String> imagePath) {
        super(context,resource,imagePath);
        mContext = context;
       layoutResource = resource;
       this.append = append;
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if(convertView==null) {

            convertView = LayoutInflater.from(mContext).inflate(layoutResource, parent, false);

        }

            ImageView gridImage = (ImageView)convertView.findViewById(R.id.grid_image_view);
            ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.grid_progress);
            String url = getItem(position);
            String urlImg = ("http://berna-diplom.norox.com.ua/img/" + url);
            ImageView ic_video =(ImageView)convertView.findViewById(R.id.ic_video);

         if(url!=null&&(!MediaFilesScanner.isVideo(urlImg))){

             if(isFirebaseVideo(urlImg)){
                 ic_video.setVisibility(View.VISIBLE);
             }else {
                 ic_video.setVisibility(View.GONE);
             }

         }else {
             ic_video.setVisibility(View.VISIBLE);
         }

         GlideImageLoader.loadImageWithTransition(mContext,urlImg,gridImage,progressBar);

        return  convertView;
    }


    private boolean isFirebaseVideo(String urlImg){

        return (urlImg.contains("video")&&urlImg.contains("videos"));
    }


}
