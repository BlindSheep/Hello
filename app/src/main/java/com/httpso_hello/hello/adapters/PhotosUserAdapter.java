package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.Structures.Photo;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.httpso_hello.hello.helper.ConverterDate.convertDateToAge;

/**
 * Created by mixir on 16.08.2017.
 */

public class PhotosUserAdapter extends ArrayAdapter<Photo>{

    private final Activity context;
    private final Settings stgs;
    private ArrayList<Photo> photos;
    private int width = 400;
    private int lastPosition = 0;
    private boolean firstEntries = false;

    public PhotosUserAdapter(Activity context, ArrayList<Photo> photos, int content){
        super(context, content, photos);
        this.context = context;
        stgs = new Settings(context.getApplicationContext());
        this.photos = photos;
    }

    static class ViewHolder{
        public ImageView profile_photos_item;
        public TextView profile_photos_like;
        public ImageView profile_photos_icon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final PhotosUserAdapter.ViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_profile_photos_item, null, true);
            holder = new PhotosUserAdapter.ViewHolder();
            holder.profile_photos_item = (ImageView) rowView.findViewById(R.id.profile_photos_item);
            holder.profile_photos_like = (TextView) rowView.findViewById(R.id.profile_photos_like);
            holder.profile_photos_icon = (ImageView) rowView.findViewById(R.id.profile_photos_icon);
            rowView.setTag(holder);
        } else {
            holder = (PhotosUserAdapter.ViewHolder) rowView.getTag();
        }


        final Photo photo = this.photos.get(position);
        if(photo != null){
//            if(photo.image.micro.length()>0)
                Picasso.with(getContext())
                        .load(Constant.upload + photo.image.micro)
                        .into(holder.profile_photos_item, new Callback() {
                            @Override
                            public void onSuccess() {
                            }
                            @Override
                            public void onError() {
                                Picasso.with(getContext())
                                        .load(Constant.upload + photo.image.small)
                                        .into(holder.profile_photos_item, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                            }
                                            @Override
                                            public void onError() {
                                                Picasso.with(getContext())
                                                        .load(Constant.upload + photo.image.normal)
                                                        .into(holder.profile_photos_item, new Callback() {
                                                            @Override
                                                            public void onSuccess() {
                                                            }
                                                            @Override
                                                            public void onError() {
                                                                Picasso.with(getContext())
                                                                        .load(Constant.default_avatar)
                                                                        .into(holder.profile_photos_item);
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
        } else {
            Picasso.with(getContext())
                    .load(Constant.default_avatar)
                    .into(holder.profile_photos_item);
        }
        if (photo.rating != 0) {
            holder.profile_photos_like.setText(Integer.toString(photo.rating));
            holder.profile_photos_like.setVisibility(View.VISIBLE);
            holder.profile_photos_icon.setVisibility(View.VISIBLE);
        } else {
            holder.profile_photos_like.setVisibility(View.GONE);
            holder.profile_photos_icon.setVisibility(View.GONE);
        }

        return rowView;
    }
}