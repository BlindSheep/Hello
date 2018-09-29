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
import com.httpso_hello.hello.activity.NotisesActivity;
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
        public TextView new_notise;
        public TextView old_notise;
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
            holder.new_notise = (TextView) rowView.findViewById(R.id.new_notise);
            holder.old_notise = (TextView) rowView.findViewById(R.id.old_notise);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        NoticeItem thisNotice = this.noticeItem.get(position);
        NoticeItem lastNotice = null;
        if (position != 0) lastNotice = this.noticeItem.get(position - 1);

        if ((position == 0) && thisNotice.isNew) {
            holder.new_notise.setVisibility(View.VISIBLE);
            holder.old_notise.setVisibility(View.GONE);
        }
        else if ((position == 0) && !thisNotice.isNew){
            holder.new_notise.setVisibility(View.GONE);
            holder.old_notise.setVisibility(View.VISIBLE);
        } else {
            holder.new_notise.setVisibility(View.GONE);
            if ((lastNotice != null) && (thisNotice.isNew != lastNotice.isNew)) holder.old_notise.setVisibility(View.VISIBLE);
            else holder.old_notise.setVisibility(View.GONE);
        }

        Picasso
                .with(getContext())
                .load(Constant.upload + thisNotice.user.avatar.micro)
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
        if(thisNotice.datePub != null) {
            holder.noticeDate.setText(ConverterDate.convertDateForGuest(thisNotice.datePub));
        }
        switch(thisNotice.typeNotice){
            case 1: // Уведомление о лайке
                String appricated = "";

                switch (thisNotice.user.gender){
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

                switch (thisNotice.targetController){
                    case "photos":
                        uploadNoticePreview(
                                thisNotice.target_preview,
                                holder
                        );
                        holder.noticeText.setText(thisNotice.user.nickname + appricated + "Вашу фотографию");

                        break;
                    case "content":
                        holder.noticeText.setText(thisNotice.user.nickname + appricated + "Ваше объявление");
                        hideTargetPreview(holder);
                        break;
                    case "comments":
                        holder.noticeText.setText(thisNotice.user.nickname + appricated + " Ваш комментарий");
                        hideTargetPreview(holder);
                        break;
                }

                break;
            case 2: // Уведомление о коментарии
                if(thisNotice.parentUser == null) {
                    switch (thisNotice.user.gender) {
                        case User.GENDER_MAN:
                            appricated = " прокомментировал ";
                            break;
                        case User.GENDER_WOOMEN:
                            appricated = " прокомментировала ";
                            break;
                        default:
                            appricated = " прокомментировал(а) ";
                            break;

                    }
                    switch (thisNotice.targetController) {
                        case "photos":

                            uploadNoticePreview(
                                    thisNotice.target_preview,
                                    holder
                            );
                            holder.noticeText.setText(thisNotice.user.nickname + appricated + "вашу фотографию");
                            break;
                        case "content":
                            hideTargetPreview(holder);
                            holder.noticeText.setText(thisNotice.user.nickname + appricated + "ваше объявление");
                            break;
                    }

                    break;
                } else {
                    switch (thisNotice.user.gender) {
                        case User.GENDER_MAN:
                            appricated = " ответил ";
                            break;
                        case User.GENDER_WOOMEN:
                            appricated = " ответила ";
                            break;
                        default:
                            appricated = " ответил(а) ";
                            break;

                    }
                    holder.noticeText.setText(thisNotice.user.nickname + appricated + "на Ваш комментарий");
                    hideTargetPreview(holder);
                }
                break;
            case 3:
                uploadNoticePreview(
                        thisNotice.target_preview,
                        holder
                );
                switch (thisNotice.user.gender){
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
                holder.noticeText.setText(thisNotice.user.nickname + appricated + "Вам подарок");
                break;
            case 4:
                hideTargetPreview(holder);
                switch (thisNotice.user.gender){
                    case User.GENDER_MAN:
                        appricated = " проявил ";
                        break;
                    case  User.GENDER_WOOMEN:
                        appricated = " проявила ";
                        break;
                    default:
                        appricated = " проявил(а) ";
                        break;

                }
                holder.noticeText.setText(thisNotice.user.nickname + appricated + " к Вам симпатию");
                break;
        }

        NotisesActivity na = ((NotisesActivity) getContext());
        if(position == (this.noticeItem.size() - 1)) {
            na.getNewPages();
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