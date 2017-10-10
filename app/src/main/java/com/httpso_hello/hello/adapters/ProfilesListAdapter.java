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
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.activity.SearchActivity;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.httpso_hello.hello.helper.ConverterDate.convertDateToAge;

/**
 * Created by mixir on 13.08.2017.
 */

public class ProfilesListAdapter extends ArrayAdapter<User> {

    private final Activity context;
    private ArrayList<Message> messages;// = new ArrayList<>();
    private final Settings stgs;
    private Bitmap userAvatar = null;
    private Bitmap contactAvatar = null;
    private ArrayList<User> users;

    public ProfilesListAdapter(Activity context, ArrayList<User> users){
        super(context, R.layout.content_search, users);
        this.context = context;
        stgs = new Settings(context.getApplicationContext());
        this.users = users;
    }

    static class ViewHolder{
        public ImageView search_profile_avatar;
        public ImageView isOnline;
        public TextView search_profile_nickname;
        public TextView search_profile_city;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ProfilesListAdapter.ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_search_item, null, true);
            holder = new ProfilesListAdapter.ViewHolder();
            holder.search_profile_avatar = (ImageView) rowView.findViewById(R.id.search_profile_avatar);
            holder.isOnline = (ImageView) rowView.findViewById(R.id.isOnline);
            holder.search_profile_nickname = (TextView) rowView.findViewById(R.id.search_profile_nickname);
            holder.search_profile_city = (TextView) rowView.findViewById(R.id.search_profile_city);
            rowView.setTag(holder);
        } else {
            holder = (ProfilesListAdapter.ViewHolder) rowView.getTag();
        }


        User user = this.users.get(position);


        DisplayMetrics displaymetrics = getContext().getResources().getDisplayMetrics();
        int width = displaymetrics.widthPixels / 2 - 50;
        holder.search_profile_avatar.setMinimumHeight(width);
        holder.search_profile_avatar.setMinimumWidth(width);
        if(user.avatar!=null)
             Picasso.with(this.context)
                    .load(Uri.parse(Constant.upload + user.avatar.normal))
                    .centerCrop()
                    .resize(width, width)
                    .error(getContext().getResources().getDrawable(R.mipmap.avatar))
                    .into(holder.search_profile_avatar);
         else
             Picasso.with(this.context)
                    .load(Uri.parse(Constant.default_avatar))
                    .centerCrop()
                    .resize(width, width)
                    .into(holder.search_profile_avatar);

        if (user.is_online){
            holder.isOnline.setVisibility(View.VISIBLE);
        } else holder.isOnline.setVisibility(View.INVISIBLE);
        holder.search_profile_city.setText(convertDateToAge(user.birth_date) + ", " + user.city_cache);
        holder.search_profile_nickname.setText(user.nickname);

        Log.d("sP", Integer.toString(position));

        //Подгружаем новых Юзеров
        SearchActivity sa = ((SearchActivity) getContext());
        if(position == (this.users.size() - 29)) sa.getNew();

        return rowView;
    }

    @Override
    public int getCount() {
        return this.users.size();
    }

    public void add(ArrayList<User> users) {
        this.users.addAll(users);
    }

    public void set(ArrayList<User> users){
        this.users = users;
        this.notifyDataSetChanged();
    }
}
