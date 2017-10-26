package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Guest;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.httpso_hello.hello.helper.ConverterDate.convertDateToAge;

/**
 * Created by mixir on 13.08.2017.
 */

public class GuestsListAdapter extends ArrayAdapter<Guest> {

    private final Activity context;
    private final Settings stgs;
    private ArrayList<Guest> guests;

    public GuestsListAdapter(Activity context, ArrayList<Guest> guests){
        super(context, R.layout.content_guests, guests);
        this.context = context;
        stgs = new Settings(context.getApplicationContext());
        this.guests = guests;
    }

    static class ViewHolder{
        public ImageView guest_profile_avatar;
        public ImageView isOnline;
        public TextView guest_profile_nickname;
        public TextView guest_profile_city;
        public TextView blockHeader;
        public TextView guest_is_new;
        public TextView guest_time_enter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final GuestsListAdapter.ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_guests_item, null, true);
            holder = new GuestsListAdapter.ViewHolder();
            holder.guest_profile_avatar = (ImageView) rowView.findViewById(R.id.guest_profile_avatar);
            holder.guest_profile_nickname= (TextView) rowView.findViewById(R.id.guest_profile_nickname);
            holder.guest_profile_city = (TextView) rowView.findViewById(R.id.guest_profile_city);
            holder.guest_time_enter = (TextView) rowView.findViewById(R.id.guest_time_enter);
            rowView.setTag(holder);
        } else {
            holder = (GuestsListAdapter.ViewHolder) rowView.getTag();
        }



        Guest guest = this.guests.get(position);

        DisplayMetrics displaymetrics = getContext().getResources().getDisplayMetrics();
        int width = displaymetrics.widthPixels / 2 - 50;
        holder.guest_profile_avatar.setMinimumHeight(width);
        holder.guest_profile_avatar.setMinimumWidth(width);

        if(guest.user_info.avatar != null) {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.upload + guest.user_info.avatar.micro))
                    .transform(new CircularTransformation(0))
                    .into(holder.guest_profile_avatar, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso
                                    .with(getContext())
                                    .load(Uri.parse(Constant.default_avatar))
                                    .transform(new CircularTransformation(0))
                                    .into(holder.guest_profile_avatar);
                        }
                    });
        } else {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.default_avatar))
                    .transform(new CircularTransformation(0))
                    .into(holder.guest_profile_avatar);
        }

        if(guest.user_info.birth_date != null) {
            holder.guest_profile_city.setText(convertDateToAge(guest.user_info.birth_date) + ", " + guest.user_info.city_cache);
        } else {
            holder.guest_profile_city.setText(guest.user_info.city_cache);
        }
        holder.guest_profile_nickname.setText(guest.user_info.nickname);

        holder.guest_time_enter.setText(ConverterDate.convertDateForGuest(guest.date));


        return rowView;
    }

    public void set(ArrayList<Guest> guests){
        this.guests = guests;
        this.notifyDataSetChanged();

    }
}
