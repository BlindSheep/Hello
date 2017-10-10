package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Vote;
import com.httpso_hello.hello.activity.FullscreenPhotoActivity;
import com.httpso_hello.hello.activity.LikeActivity;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 20.08.2017.
 */

public class LikesAdapter extends ArrayAdapter<Vote> {

    private ArrayList<Vote> votes;
    private final Activity context;
    private Settings stgs;

    public LikesAdapter(Activity context, ArrayList<Vote> votes){
        super(context, R.layout.content_like, votes);
        this.votes = votes;
        this.context = context;
        this.stgs = new Settings(getContext());
    }

    private class ViewHolder{
        public ImageView userAvatarLike;
        public TextView userNicknameLike;
        public TextView userInfoLike;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        final ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_like_item, null, true);
            holder = new ViewHolder();
            holder.userAvatarLike = (ImageView) rowView.findViewById(R.id.userAvatarLike);
            holder.userNicknameLike = (TextView) rowView.findViewById(R.id.userNicknameLike);
            holder.userInfoLike = (TextView) rowView.findViewById(R.id.userInfoLike);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        final Vote vote = this.votes.get(position);



        //// TODO: 30.07.2017 Добавить проверку наличия изображений: проверка заполнености размеров
        if(vote.user_avatar != null) {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.upload + vote.user_avatar.micro))
                    .transform(new CircularTransformation(0))
                    .into(holder.userAvatarLike, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso
                                    .with(getContext())
                                    .load(Uri.parse(Constant.default_avatar))
                                    .transform(new CircularTransformation(0))
                                    .into(holder.userAvatarLike);
                        }
                    });
        } else {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.default_avatar))
                    .transform(new CircularTransformation(0))
                    .into(holder.userAvatarLike);
        }

        holder.userNicknameLike.setText(vote.user_nickname);
        if ((vote.birth_date != null) && (vote.city_cache != null)) holder.userInfoLike.setText(ConverterDate.convertDateToAge(vote.birth_date) + ", " + vote.city_cache);
        else if (vote.birth_date != null) holder.userInfoLike.setText(ConverterDate.convertDateToAge(vote.birth_date));
        else if (vote.city_cache != null) holder.userInfoLike.setText(vote.city_cache);
        else holder.userInfoLike.setText("");

        return rowView;

    }

}
