package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.activity.FollowersActivity;
import com.httpso_hello.hello.activity.SearchActivity;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 25.11.2017.
 */

public class FollowersAdapter extends ArrayAdapter<User> {

    private ArrayList<User> users;
    private final Activity context;
    private Settings stgs;
    private boolean isAdmin;
    private int group_id;

    public FollowersAdapter(Activity context, ArrayList<User> users, boolean isAdmin, int group_id){
        super(context, R.layout.activity_followers, users);
        this.users = users;
        this.context = context;
        this.stgs = new Settings(getContext());
        this.isAdmin = isAdmin;
        this.group_id = group_id;
    }

    private class ViewHolder{
        public ImageView userAvatar;
        public TextView userNickname;
        public ImageView deleteFollower;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        final FollowersAdapter.ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.acrivity_followers_item, null, true);
            holder = new FollowersAdapter.ViewHolder();
            holder.userAvatar = (ImageView) rowView.findViewById(R.id.userAvatar);
            holder.userNickname = (TextView) rowView.findViewById(R.id.userNickname);
            holder.deleteFollower = (ImageView) rowView.findViewById(R.id.deleteFollower);
            rowView.setTag(holder);
        } else {
            holder = (FollowersAdapter.ViewHolder) rowView.getTag();
        }

        final User user = this.users.get(position);

        if(user.avatar != null) {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.upload + user.avatar.micro))
                    .transform(new CircularTransformation(0))
                    .into(holder.userAvatar, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso
                                    .with(getContext())
                                    .load(Uri.parse(Constant.default_avatar))
                                    .transform(new CircularTransformation(0))
                                    .into(holder.userAvatar);
                        }
                    });
        } else {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.default_avatar))
                    .transform(new CircularTransformation(0))
                    .into(holder.userAvatar);
        }

        holder.userNickname.setText(user.nickname);

        if (isAdmin && !(stgs.getSettingInt("user_id") == user.id)) {
            holder.deleteFollower.setVisibility(View.VISIBLE);
            holder.deleteFollower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FollowersActivity) getContext()).getPopup(group_id, user.id, user.nickname, position);
                }
            });
        } else {
            holder.deleteFollower.setVisibility(View.GONE);
        }

        //Подгружаем новых Юзеров
        FollowersActivity fa = ((FollowersActivity) getContext());
        if(position == (this.users.size()) - 1) fa.getNextPage();

        return rowView;

    }

    public void add (ArrayList<User> users) {
        this.users.addAll(users);
    }

    public void delete (int position) {
        this.users.remove(position);
        notifyDataSetChanged();
    }
}