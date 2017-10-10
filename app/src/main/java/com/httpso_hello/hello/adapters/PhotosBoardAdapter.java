package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.content.Intent;
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
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.Structures.Photo;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.activity.BoardActivity;
import com.httpso_hello.hello.activity.FullscreenPhotoActivity;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.httpso_hello.hello.helper.ConverterDate.convertDateToAge;

/**
 * Created by mixir on 16.08.2017.
 */

public class PhotosBoardAdapter extends ArrayAdapter<Image>{

    private final Activity context;
    private final Settings stgs;
    private ArrayList<Image> photos;
    private int width = 400;
    private int lastPosition = 0;
    private boolean firstEntries = false;

    public PhotosBoardAdapter(Activity context, ArrayList<Image> photos, int content){
        super(context, content, photos);
        this.context = context;
        stgs = new Settings(context.getApplicationContext());
        this.photos = photos;
    }

    static class ViewHolder{
        public ImageView profile_photos_item;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        ViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_another_photos_board_item, null, true);
            holder = new ViewHolder();
            holder.profile_photos_item = (ImageView) rowView.findViewById(R.id.another_photos_board_item);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Image photo = this.photos.get(position);
        Log.d("board", Integer.toString(position));
        if(photo != null){
            Picasso.with(this.context)
                    .load(Constant.upload + photo.micro)
                    .into(holder.profile_photos_item);
        }

        return rowView;
    }
}