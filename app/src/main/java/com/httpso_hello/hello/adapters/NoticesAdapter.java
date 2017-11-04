package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.NoticeItem;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.helper.*;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 24.08.2017.
 */

public class NoticesAdapter extends ArrayAdapter<NoticeItem> {

    private ArrayList<NoticeItem> noticeItem;
    private final Activity context;
    private Settings stgs;

    public NoticesAdapter(Activity context, ArrayList<NoticeItem> noticeItem){
        super(context, R.layout.content_notises, noticeItem);
        this.noticeItem = noticeItem;
        this.context = context;
        this.stgs = new Settings(getContext());
    }

    private class ViewHolder{
        public TextView noticeText;
        public TextView noticeDate;
        public ImageView noticeSenderAvatar;
        public ImageView targetNoticePreview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_notises_item, null, true);
            holder = new ViewHolder();
            holder.noticeText = (TextView) rowView.findViewById(R.id.noticeText);
            holder.noticeDate = (TextView) rowView.findViewById(R.id.noticeDate);
            holder.noticeSenderAvatar = (ImageView) rowView.findViewById(R.id.notice_sender_avatar);
            holder.targetNoticePreview = (ImageView) rowView.findViewById(R.id.notice_target_preview);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        NoticeItem noticeItem = this.noticeItem.get(position);
//        if(noticeItem.sender_user!=null)
        Picasso
                .with(getContext())
                .load(Constant.upload + noticeItem.sender_user.avatar.micro)
                .transform(new CircularTransformation(0))
                .resize(100, 100)
                .into(holder.noticeSenderAvatar, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso
                                .with(getContext())
                                .load(R.mipmap.avatar)
                                .transform(new CircularTransformation(0))
                                .resize(100, 100)
                                .into(holder.noticeSenderAvatar);
                    }
                });
        if(noticeItem.date_pub!=null) {
            holder.noticeDate.setText(ConverterDate.convertDateForGuest(noticeItem.date_pub));
        }
        switch(noticeItem.type_notice){
            case 1: // Уведомление о лайке
                String appricated = "";

                switch (noticeItem.sender_user.gender){
                    case User.GENDER_MAN:
                        appricated = " оценил ";
                        break;
                    case  User.GENDER_WOOMEN:
                        appricated = " оценила ";
                        break;
                    default:
                        appricated = " оценил(а) ";
                        break;

                }

                switch (noticeItem.target_controller){
                    case "photos":
                        uploadNoticePreview(
                                noticeItem.target_preview,
                                holder
                        );
                        holder.noticeText.setText(noticeItem.sender_user.nickname + appricated + "Вашу фотографию");

                        break;
                    case "content":
                        switch (noticeItem.content_type){
                            case "board":
//                                if(noticeItem.target_content!=null)
//                                    holder.noticeText.setText("Ваш объявление \" " + noticeItem.target_content.substring(0, 40) +" ...\"" + appricated + noticeItem.sender_user.nickname);
                                holder.noticeText.setText(noticeItem.sender_user.nickname + appricated + "Ваше объявление");
                                hideTargetPreview(holder);
                                break;
                        }


                        break;
                    case "comments":
                        holder.noticeText.setText(noticeItem.sender_user.nickname + appricated + " Ваш комментарий");
                        hideTargetPreview(holder);
                        break;
                }

                break;
            case 2: // Уведомление о коментарии
                switch (noticeItem.sender_user.gender){
                    case User.GENDER_MAN:
                        appricated = " прокомментировал ";
                        break;
                    case  User.GENDER_WOOMEN:
                        appricated = " прокомментировала ";
                        break;
                    default:
                        appricated = " прокомментировал(а) ";
                        break;

                }
                switch (noticeItem.target_controller){
                    case "photos":

                        uploadNoticePreview(
                                noticeItem.target_preview,
                                holder
                        );
                        holder.noticeText.setText(noticeItem.sender_user.nickname + appricated +"вашу фотографию");
                        break;
                    case "content":
                        switch(noticeItem.content_type){
                            case "board":
                                hideTargetPreview(holder);
                                holder.noticeText.setText(noticeItem.sender_user.nickname + appricated + "ваше объявление");
//                                holder.noticeText.setText(noticeItem.sender_user.nickname + " прокоментировал ваше объявление \" " + noticeItem.target_content +" \"");
                                break;
                        }

                        break;
                }
                break;
            case 3:
                uploadNoticePreview(
                        noticeItem.target_preview,
                        holder
                );
                switch (noticeItem.sender_user.gender){
                    case User.GENDER_MAN:
                        appricated = " отправил ";
                        break;
                    case  User.GENDER_WOOMEN:
                        appricated = " отправила ";
                        break;
                    default:
                        appricated = " отправил(а) ";
                        break;

                }
                holder.noticeText.setText(noticeItem.sender_user.nickname + appricated + "Вам подарок");
                break;
        }



        return rowView;

    }

    private void uploadNoticePreview(Image image, final ViewHolder holder){
        if(image!=null) {
            Picasso
                    .with(getContext())
                    .load(Constant.upload + image.small)
                    .resize(Help.getPxFromDp(100, getContext()), Help.getPxFromDp(100, getContext()))
                    .into(holder.targetNoticePreview, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.targetNoticePreview.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            hideTargetPreview(holder);
        }
    }

    private void hideTargetPreview(ViewHolder holder){
        holder.targetNoticePreview.setVisibility(View.GONE);
    }

    public NoticeItem getItem(int position) {
        return this.noticeItem.get(position);
    }
}