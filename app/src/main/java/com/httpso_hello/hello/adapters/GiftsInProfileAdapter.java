package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.GiftItem;
import com.httpso_hello.hello.Structures.Photo;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 02.11.2017.
 */

public class GiftsInProfileAdapter extends ArrayAdapter<GiftItem> {

    private final Activity context;
    private final Settings stgs;
    private ArrayList<GiftItem> gifts;

    public GiftsInProfileAdapter(Activity context, ArrayList<GiftItem> gifts){
        super(context, R.layout.content_profile, gifts);
        this.context = context;
        stgs = new Settings(context.getApplicationContext());
        this.gifts = gifts;
    }

        static class ViewHolder{
            public ImageView imageView;
    }

    @Override
    public int getCount() {
        return gifts.size() + 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        final GiftsInProfileAdapter.ViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_gifts_in_profile, null, true);
            holder = new GiftsInProfileAdapter.ViewHolder();
            holder.imageView = (ImageView) rowView.findViewById(R.id.imageView);
            rowView.setTag(holder);
        } else {
            holder = (GiftsInProfileAdapter.ViewHolder) rowView.getTag();
        }

        DisplayMetrics displaymetrics = getContext().getResources().getDisplayMetrics();

        if(position == 0) {
            Picasso.with(getContext())
                    .load(R.drawable.ic_action_send_gift_2)
                    .resize((int) (displaymetrics.density * 30), (int) (displaymetrics.density * 30))
                    .into(holder.imageView);
        } else {
            final GiftItem gift = this.gifts.get(position - 1);
            Picasso.with(getContext())
                    .load(Constant.upload + gift.photo.small)
                    .resize((int) (displaymetrics.density * 60), (int) (displaymetrics.density * 60))
                    .into(holder.imageView);
        }
        return rowView;
    }
}