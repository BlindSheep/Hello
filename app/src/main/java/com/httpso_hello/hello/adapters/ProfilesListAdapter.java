package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.activity.SearchActivity;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
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
        public ProgressBar search_profile_load;
        public LinearLayout click;

        public ImageView search_profile_avatar_second;
        public ImageView isOnline_second;
        public TextView search_profile_nickname_second;
        public TextView search_profile_city_second;
        public ProgressBar search_profile_load_second;
        public LinearLayout click_second;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ProfilesListAdapter.ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_search_item, null, true);
            holder = new ProfilesListAdapter.ViewHolder();
            holder.search_profile_avatar = (ImageView) rowView.findViewById(R.id.search_profile_avatar);
            holder.isOnline = (ImageView) rowView.findViewById(R.id.isOnline);
            holder.search_profile_nickname = (TextView) rowView.findViewById(R.id.search_profile_nickname);
            holder.search_profile_city = (TextView) rowView.findViewById(R.id.search_profile_city);
            holder.search_profile_load = (ProgressBar) rowView.findViewById(R.id.search_profile_load);
            holder.click = (LinearLayout) rowView.findViewById(R.id.click);
            holder.search_profile_avatar_second = (ImageView) rowView.findViewById(R.id.search_profile_avatar_second);
            holder.isOnline_second = (ImageView) rowView.findViewById(R.id.isOnline_second);
            holder.search_profile_nickname_second = (TextView) rowView.findViewById(R.id.search_profile_nickname_second);
            holder.search_profile_city_second = (TextView) rowView.findViewById(R.id.search_profile_city_second);
            holder.search_profile_load_second = (ProgressBar) rowView.findViewById(R.id.search_profile_load_second);
            holder.click_second = (LinearLayout) rowView.findViewById(R.id.click_second);
            rowView.setTag(holder);
        } else {
            holder = (ProfilesListAdapter.ViewHolder) rowView.getTag();
        }

        final User userFirst;
        final User userSecond;
        if (position == 0) {
            userFirst = users.get(position);
            userSecond = users.get(position + 1);
        } else {
            userFirst = users.get(position * 2);
            userSecond = users.get(position * 2 + 1);
        }

        holder.search_profile_load.setVisibility(View.VISIBLE);
        holder.search_profile_load_second.setVisibility(View.VISIBLE);

        final SearchActivity SA = ((SearchActivity) context);
        DisplayMetrics displaymetrics = getContext().getResources().getDisplayMetrics();
        int width = displaymetrics.widthPixels / 2 - 50;

        //Первый юзер
        holder.search_profile_avatar.setMinimumHeight(width);
        holder.search_profile_avatar.setMinimumWidth(width);
        if(userFirst.avatar!=null)
             Picasso.with(this.context)
                    .load(Uri.parse(Constant.upload + userFirst.avatar.normal))
                    .centerCrop()
                    .resize(width, width)
                    .error(getContext().getResources().getDrawable(R.mipmap.avatar))
                    .into(holder.search_profile_avatar, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.search_profile_load.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.search_profile_load.setVisibility(View.GONE);
                        }
                    });
         else
             Picasso.with(this.context)
                    .load(Uri.parse(Constant.default_avatar))
                    .centerCrop()
                    .resize(width, width)
                    .into(holder.search_profile_avatar, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.search_profile_load.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.search_profile_load.setVisibility(View.GONE);
                        }
                    });

        if (userFirst.is_online){
            holder.isOnline.setVisibility(View.VISIBLE);
        } else holder.isOnline.setVisibility(View.INVISIBLE);
        holder.search_profile_city.setText(convertDateToAge(userFirst.birth_date) + ", " + userFirst.city_cache);
        holder.search_profile_nickname.setText(userFirst.nickname);
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("profile_id", userFirst.id);
                intent.putExtra("profile_nickname", " " + userFirst.nickname);
                // TODO: 01.08.2017 Добавить проверку размеров
                if (userFirst.avatar == null) {
                    intent.putExtra("avatar", Constant.default_avatar);
                } else {
                    intent.putExtra("avatar", userFirst.avatar.micro);
                }
                SA.startActivity(intent);
            }
        });

        //Второй юзер
        holder.search_profile_avatar_second.setMinimumHeight(width);
        holder.search_profile_avatar_second.setMinimumWidth(width);
        if(userSecond.avatar!=null)
            Picasso.with(this.context)
                    .load(Uri.parse(Constant.upload + userSecond.avatar.normal))
                    .centerCrop()
                    .resize(width, width)
                    .error(getContext().getResources().getDrawable(R.mipmap.avatar))
                    .into(holder.search_profile_avatar_second, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.search_profile_load_second.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.search_profile_load_second.setVisibility(View.GONE);
                        }
                    });
        else
            Picasso.with(this.context)
                    .load(Uri.parse(Constant.default_avatar))
                    .centerCrop()
                    .resize(width, width)
                    .into(holder.search_profile_avatar_second, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.search_profile_load_second.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.search_profile_load_second.setVisibility(View.GONE);
                        }
                    });

        if (userSecond.is_online){
            holder.isOnline_second.setVisibility(View.VISIBLE);
        } else holder.isOnline_second.setVisibility(View.INVISIBLE);
        holder.search_profile_city_second.setText(convertDateToAge(userSecond.birth_date) + ", " + userSecond.city_cache);
        holder.search_profile_nickname_second.setText(userSecond.nickname);
        holder.click_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("profile_id", userSecond.id);
                intent.putExtra("profile_nickname", " " + userSecond.nickname);
                // TODO: 01.08.2017 Добавить проверку размеров
                if (userFirst.avatar == null) {
                    intent.putExtra("avatar", Constant.default_avatar);
                } else {
                    intent.putExtra("avatar", userSecond.avatar.micro);
                }
                SA.startActivity(intent);
            }
        });

        //Подгружаем новых Юзеров
        SearchActivity sa = ((SearchActivity) getContext());
        if(position == (this.users.size()) / 2 - 1) sa.getNew();

        return rowView;
    }

    @Override
    public int getCount() {
        return this.users.size() / 2;
    }

    public void add(ArrayList<User> users) {
        this.users.addAll(users);
    }

    public void set(ArrayList<User> users){
        this.users = users;
        this.notifyDataSetChanged();
    }
}
