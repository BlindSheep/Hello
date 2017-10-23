package com.httpso_hello.hello.adapters;

import android.app.Activity;
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
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Comments;
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

    public CommentsAdapter(Activity context, ArrayList<Coment> comments){
        super(context, R.layout.content_board_content, comments);
        this.comments = comments;
        this.context = context;
        this.stgs = new Settings(getContext());
    }

    private class ViewHolder{
        private TextView content;
        private ImageView avatar;
        private TextView contactNickname;
        private TextView datePub;
        private ImageView isOnline;
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
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Coment coment = this.comments.get(position);

        //Аватар
        if (coment.user.avatar != null) {

            String ava;
            if (!coment.date_pub.equals("Только что")) ava = Constant.upload + coment.user.avatar.micro;
            else ava = coment.user.avatar.micro;

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
        if (!coment.date_pub.equals("Только что")) holder.datePub.setText(ConverterDate.convertDateForGuest(coment.date_pub));
        else holder.datePub.setText("Только что");

        //Контент
        holder.content.setText(Html.fromHtml(coment.content));

        //Онлайн
        if(coment.user.is_online) holder.isOnline.setVisibility(View.VISIBLE);
        else holder.isOnline.setVisibility(View.GONE);

        return rowView;

    }

}