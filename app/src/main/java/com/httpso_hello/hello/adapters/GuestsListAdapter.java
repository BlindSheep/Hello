package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Guest;
import com.httpso_hello.hello.activity.GuestsActivity;
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
        public TextView old_guests;
        public TextView new_guests;
        public TextView guest_time_enter;
        public TextView count;
        public LinearLayout countLayout;
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
            holder.new_guests = (TextView) rowView.findViewById(R.id.new_guests);
            holder.old_guests = (TextView) rowView.findViewById(R.id.old_guests);
            holder.count = (TextView) rowView.findViewById(R.id.count);
            holder.countLayout = (LinearLayout) rowView.findViewById(R.id.countLayout);
            rowView.setTag(holder);
        } else {
            holder = (GuestsListAdapter.ViewHolder) rowView.getTag();
        }



        final Guest thisGuest = this.guests.get(position);
        Guest lastGuest = null;
        if (position != 0) lastGuest = this.guests.get(position - 1);

        if ((position == 0) && !thisGuest.readed) {
            holder.new_guests.setVisibility(View.VISIBLE);
            holder.old_guests.setVisibility(View.GONE);
        }
        else if ((position == 0) && thisGuest.readed){
            holder.new_guests.setVisibility(View.GONE);
            holder.old_guests.setVisibility(View.VISIBLE);
        } else {
            holder.new_guests.setVisibility(View.GONE);
            if ((lastGuest != null) && (thisGuest.readed != lastGuest.readed)) holder.old_guests.setVisibility(View.VISIBLE);
            else holder.old_guests.setVisibility(View.GONE);
        }

        if(thisGuest.user.avatar != null) {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.upload + thisGuest.user.avatar.micro))
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

        if(thisGuest.user.birthDate != null) {
            holder.guest_profile_city.setText(convertDateToAge(thisGuest.user.birthDate) + ", " + thisGuest.user.city_cache);
        } else {
            holder.guest_profile_city.setText(thisGuest.user.city_cache);
        }
        holder.guest_profile_nickname.setText(thisGuest.user.nickname);

        holder.guest_time_enter.setText(ConverterDate.convertDateForGuest(thisGuest.createdAt));

        holder.count.setText(Integer.toString(thisGuest.count));

        holder.countLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(), thisGuest.user.nickname + " просмотрел(а) ваш профиль " + Integer.toString(thisGuest.count) + " раз(а)", Toast.LENGTH_LONG).show();
            }
        });

        GuestsActivity ba = ((GuestsActivity) getContext());
        if(position == (this.guests.size() - 1)) {
//            ba.getNewGuests();
        }

        return rowView;
    }

    public void set(ArrayList<Guest> guests){
        this.guests = guests;
        this.notifyDataSetChanged();

    }
}
