package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Coment;
import com.httpso_hello.hello.activity.BoardContentActivity;
import com.httpso_hello.hello.activity.PhotoCommentsActivity;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 24.08.2017.
 */

public class CommentsAdapter extends ArrayAdapter<Coment> {

    private ArrayList<Coment> comments;
    private final Activity context;
    private Settings stgs;
    private String type;
    private int contentId;
    private boolean isMyPhoto;

    public CommentsAdapter(Activity context, ArrayList<Coment> comments, String type, int contentId, boolean isMyPhoto){
        super(context, R.layout.content_board_content, comments);
        this.comments = comments;
        this.context = context;
        this.stgs = new Settings(getContext());
        this.type = type;
        this.contentId = contentId;
        this.isMyPhoto = isMyPhoto;
    }

    private class ViewHolder{
        private TextView content;
        private ImageView avatar;
        private TextView contactNickname;
        private TextView datePub;
        private ImageView isOnline;
        private ImageView popupButton;
        private ImageView imageAns;
        private TextView answerNickname;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_board_content_item, null, true);
            holder = new ViewHolder();
            holder.content = (TextView) rowView.findViewById(R.id.content);
            holder.avatar = (ImageView) rowView.findViewById(R.id.avatar);
            holder.contactNickname = (TextView) rowView.findViewById(R.id.contactNickname);
            holder.datePub = (TextView) rowView.findViewById(R.id.datePub);
            holder.isOnline = (ImageView) rowView.findViewById(R.id.isOnline);
            holder.popupButton = (ImageView) rowView.findViewById(R.id.popupButton);
            holder.answerNickname = (TextView) rowView.findViewById(R.id.answerNickname);
            holder.imageAns = (ImageView) rowView.findViewById(R.id.imageAns);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        final Coment coment = this.comments.get(position);

        //Аватар
        if (coment.user.avatar != null) {

            String ava;
            if (coment.createdAt != null) {
                if (!coment.createdAt.equals("Только что")) ava = Constant.upload + coment.user.avatar.micro;
                else ava = coment.user.avatar.micro;
            } ava = Constant.upload + coment.user.avatar.micro;

            Picasso
                    .with(getContext())
                    .load(ava)
                    .transform(new CircularTransformation(0))
                    .into(holder.avatar, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso
                                    .with(getContext())
                                    .load(Uri.parse(Constant.default_avatar))
                                    .transform(new CircularTransformation(0))
                                    .into(holder.avatar);
                        }
                    });
        } else {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.default_avatar))
                    .transform(new CircularTransformation(0))
                    .into(holder.avatar);
        }

        //Имя юзера
        holder.contactNickname.setText(coment.user.nickname);

        //Дата публикации
        if (coment.createdAt != null){
            if (!coment.createdAt.equals("Только что")) holder.datePub.setText(ConverterDate.convertDateForGuest(coment.createdAt));
            else holder.datePub.setText("Только что");
        }

        //Кому ответ
        if (coment.parent_user != null) {
            holder.answerNickname.setText(coment.parent_user.nickname);
            holder.answerNickname.setVisibility(View.VISIBLE);
            holder.answerNickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("profile_id", coment.parent_user.id);
                    if (type.equals("board")){
                        ((BoardContentActivity) getContext()).startActivity(intent);
                    } else if (type.equals("photo")) {
                        ((PhotoCommentsActivity) getContext()).startActivity(intent);
                    }
                }
            });
            holder.imageAns.setVisibility(View.VISIBLE);
        } else {
            holder.answerNickname.setVisibility(View.GONE);
            holder.imageAns.setVisibility(View.GONE);
        }

        //Контент
        holder.content.setText(Html.fromHtml(coment.content));

        //Онлайн
        if(coment.user.isOnline) holder.isOnline.setVisibility(View.VISIBLE);
        else holder.isOnline.setVisibility(View.GONE);

        //Удаление коммента
        if (type.equals("board")){
            if (stgs.getSettingInt("userId") == coment.userId) {
                holder.popupButton.setVisibility(View.VISIBLE);
                holder.popupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final BoardContentActivity bсa = ((BoardContentActivity) getContext());
                        bсa.popupComment(coment.id, "content", "board", contentId);
                    }
                });
            } else {
                holder.popupButton.setVisibility(View.GONE);
            }
            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("profile_id", coment.userId);
                    ((BoardContentActivity) getContext()).startActivity(intent);
                }
            });
        } else if (type.equals("photo")) {
            if ((stgs.getSettingInt("userId") == coment.userId) || isMyPhoto) {
                holder.popupButton.setVisibility(View.VISIBLE);
                holder.popupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final PhotoCommentsActivity pсa = ((PhotoCommentsActivity) getContext());
                        pсa.popupComment(coment.id, "photos", "photo", contentId);
                    }
                });
            } else {
                holder.popupButton.setVisibility(View.GONE);
            }
            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("profile_id", coment.userId);
                    ((PhotoCommentsActivity) getContext()).startActivity(intent);
                }
            });
        }

        return rowView;

    }

    public void addComments(ArrayList<Coment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }

}