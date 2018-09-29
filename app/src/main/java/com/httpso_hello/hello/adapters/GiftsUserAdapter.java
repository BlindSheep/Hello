package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.GiftItem;
import com.httpso_hello.hello.Structures.GiftItemNew;
import com.httpso_hello.hello.Structures.Vote;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 03.11.2017.
 */

public class GiftsUserAdapter  extends ArrayAdapter<GiftItem> {

    private ArrayList<GiftItem> gifts;
    private final Activity context;
    private Settings stgs;
    private boolean isUserProfile;

    public GiftsUserAdapter(Activity context, ArrayList<GiftItem> gifts, boolean isUserProfile){
        super(context, R.layout.content_gofts_user_item, gifts);
        this.gifts = gifts;
        this.context = context;
        this.stgs = new Settings(getContext());
        this.isUserProfile = isUserProfile;
    }

    private class ViewHolder{
        public ImageView userAvatarGift;
        public TextView userNicknameGifts;
        public TextView dateGift;
        public ImageView photoGift;
        public TextView commentGift;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        final GiftsUserAdapter.ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_gofts_user_item, null, true);
            holder = new GiftsUserAdapter.ViewHolder();
            holder.userAvatarGift = (ImageView) rowView.findViewById(R.id.userAvatarGift);
            holder.userNicknameGifts = (TextView) rowView.findViewById(R.id.userNicknameGifts);
            holder.dateGift = (TextView) rowView.findViewById(R.id.dateGift);
            holder.photoGift = (ImageView) rowView.findViewById(R.id.photoGift);
            holder.commentGift = (TextView) rowView.findViewById(R.id.commentGift);
            rowView.setTag(holder);
        } else {
            holder = (GiftsUserAdapter.ViewHolder) rowView.getTag();
        }

        final GiftItem giftItem = this.gifts.get(position);

        //Если подарки этого юзера или не приватно
        if (isUserProfile || (!giftItem.isPrivate)) {
            //Аватарка
            if (giftItem.user.avatar != null) {
                if (giftItem.user.avatar.micro != null) {
                    Picasso
                            .with(getContext())
                            .load(Uri.parse(Constant.upload + giftItem.user.avatar.micro))
                            .transform(new CircularTransformation(0))
                            .into(holder.userAvatarGift, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso
                                            .with(getContext())
                                            .load(Uri.parse(Constant.default_avatar))
                                            .transform(new CircularTransformation(0))
                                            .into(holder.userAvatarGift);
                                }
                            });
                } else {
                    Picasso
                            .with(getContext())
                            .load(Uri.parse(Constant.default_avatar))
                            .transform(new CircularTransformation(0))
                            .into(holder.userAvatarGift);
                }
            } else {
                Picasso
                        .with(getContext())
                        .load(Uri.parse(Constant.default_avatar))
                        .transform(new CircularTransformation(0))
                        .into(holder.userAvatarGift);
            }
            //Имя
            holder.userNicknameGifts.setText(giftItem.user.nickname);

            //Дата
            if (giftItem.createdAt != null) holder.dateGift.setText(ConverterDate.convertDateForGuest(giftItem.createdAt));

            //Картинка подарка
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.upload + giftItem.gift.photo.normal))
                    .into(holder.photoGift);

            //Тест коммента
            if (giftItem.text != null) {
                holder.commentGift.setVisibility(View.VISIBLE);
                holder.commentGift.setText(giftItem.text);
            } else holder.commentGift.setVisibility(View.GONE);

        } else {
            Picasso
                    .with(getContext())
                    .load(R.drawable.ic_action_anonimnost)
                    .transform(new CircularTransformation(0))
                    .into(holder.userAvatarGift);
            //Имя
            holder.userNicknameGifts.setText("Анонимно");

            //Дата
            if (giftItem.createdAt != null) holder.dateGift.setText(ConverterDate.convertDateForGuest(giftItem.createdAt));

            //Картинка подарка
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.upload + giftItem.gift.photo.normal))
                    .into(holder.photoGift);

            //Тест коммента
            holder.commentGift.setVisibility(View.GONE);
        }

        return rowView;

    }

}
