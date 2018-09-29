package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.GiftItem;
import com.httpso_hello.hello.Structures.GiftItemNew;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 03.11.2017.
 */

public class GiftsGetGiftsAdapter extends ArrayAdapter<GiftItem> {

    private final Activity context;
    private final Settings stgs;
    private ArrayList<GiftItem> gifts;

    public GiftsGetGiftsAdapter(Activity context, ArrayList<GiftItem> gifts){
        super(context, R.layout.popup_for_send_gift, gifts);
        this.context = context;
        stgs = new Settings(context.getApplicationContext());
        this.gifts = gifts;
    }

    static class ViewHolder{
        public ImageView imageGift;
        public TextView prise;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        final GiftsGetGiftsAdapter.ViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_get_gifts_item, null, true);
            holder = new GiftsGetGiftsAdapter.ViewHolder();
            holder.imageGift = (ImageView) rowView.findViewById(R.id.imageGift);
            holder.prise = (TextView) rowView.findViewById(R.id.prise);
            rowView.setTag(holder);
        } else {
            holder = (GiftsGetGiftsAdapter.ViewHolder) rowView.getTag();
        }

        GiftItem giftItem = gifts.get(position);

        Picasso.with(getContext())
                .load(Constant.upload + giftItem.gift.photo.small)
                .into(holder.imageGift);

        holder.prise.setText("Цена: " + Integer.toString(giftItem.gift.price) + " баллов");

        return rowView;
    }
}