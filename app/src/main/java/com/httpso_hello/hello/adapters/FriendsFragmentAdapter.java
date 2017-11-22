package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.FriendItem;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.activity.FriendsActivity;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Friend;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 23.08.2017.
 */

public class FriendsFragmentAdapter extends ArrayAdapter<FriendItem> {

    private ArrayList<FriendItem> friends;
    private final Activity context;
    private boolean isUserFriends;
    private boolean isRequests;
    private Settings stgs;

    public FriendsFragmentAdapter(Activity context, ArrayList<FriendItem> friends, boolean isRequests, boolean isUserFriends) {
        super(context, R.layout.content_friends, friends);
        this.friends = friends;
        this.context = context;
        this.stgs = new Settings(getContext());
        this.isUserFriends = isUserFriends;
        this.isRequests = isRequests;
    }

    private class ViewHolder {
        public ImageView userAvatarFriend;
        public TextView userNicknameFriend;
        public TextView userInfoFriend;
        public ImageView acceptRequestInFriend;
        public ImageView deleteFriend;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final FriendsFragmentAdapter.ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_friends_item, null, true);
            holder = new FriendsFragmentAdapter.ViewHolder();
            holder.userAvatarFriend = (ImageView) rowView.findViewById(R.id.userAvatarFriend);
            holder.userNicknameFriend = (TextView) rowView.findViewById(R.id.userNicknameFriend);
            holder.userInfoFriend = (TextView) rowView.findViewById(R.id.userInfoFriend);
            holder.acceptRequestInFriend = (ImageView) rowView.findViewById(R.id.acceptRequestInFriend);
            holder.deleteFriend = (ImageView) rowView.findViewById(R.id.deleteFriend);
            rowView.setTag(holder);
        } else {
            holder = (FriendsFragmentAdapter.ViewHolder) rowView.getTag();
        }

        final FriendItem friend = this.friends.get(position);

        //Устанавливаем аватар
        if(friend.avatar != null) {
            Picasso
                    .with(getContext())
                    .load(Constant.upload + friend.avatar.micro)
                    .transform(new CircularTransformation(0))
                    .into(holder.userAvatarFriend);
        }

        //Устанавливаем имя
        holder.userNicknameFriend.setText(friend.nickname);

        //Устанавливаем инфу о юзере
        if ((friend.birth_date != null) && (friend.city_cache != null)) holder.userInfoFriend.setText(ConverterDate.convertDateToAge(friend.birth_date) + ", " + friend.city_cache);
        else if (friend.birth_date != null) holder.userInfoFriend.setText(ConverterDate.convertDateToAge(friend.birth_date));
        else if (friend.city_cache != null) holder.userInfoFriend.setText(friend.city_cache);
        else holder.userInfoFriend.setText("");

        //Кнопка принять заявку и удалить друга
        if (isRequests) {
            holder.deleteFriend.setVisibility(View.GONE);
            holder.acceptRequestInFriend.setVisibility(View.VISIBLE);
            holder.acceptRequestInFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FriendsActivity FA = ((FriendsActivity) getContext());
                    FA.getPopupForAcceptOrDelete(friend.id);
                }
            });
        } else {
            holder.acceptRequestInFriend.setVisibility(View.GONE);
            if (isUserFriends) holder.deleteFriend.setVisibility(View.VISIBLE);
            else holder.deleteFriend.setVisibility(View.GONE);
            holder.deleteFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FriendsActivity FA = ((FriendsActivity) getContext());
                    FA.getPopupForDeleteFriend(friend.id, friend.nickname, friend.avatar);
                }
            });
        }


        return rowView;

    }
}