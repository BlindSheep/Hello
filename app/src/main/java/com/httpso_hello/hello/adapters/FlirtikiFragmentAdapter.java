package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.FlirtikItem;
import com.httpso_hello.hello.Structures.FriendItem;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 23.08.2017.
 */

public class FlirtikiFragmentAdapter extends ArrayAdapter<FlirtikItem> {

    private ArrayList<FlirtikItem> flirtik;
    private final Activity context;
    private Settings stgs;

    public FlirtikiFragmentAdapter(Activity context, ArrayList<FlirtikItem> flirtik) {
        super(context, R.layout.content_simpation, flirtik);
        this.flirtik = flirtik;
        this.context = context;
        this.stgs = new Settings(getContext());
    }

    private class ViewHolder {
        public ImageView userAvatarSimpatii;
        public TextView userNicknameSimpatii;
        public TextView userInfoSimpatii;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final FlirtikiFragmentAdapter.ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_simpation_item, null, true);
            holder = new FlirtikiFragmentAdapter.ViewHolder();
            holder.userAvatarSimpatii = (ImageView) rowView.findViewById(R.id.userAvatarSimpatii);
            holder.userNicknameSimpatii = (TextView) rowView.findViewById(R.id.userNicknameSimpatii);
            holder.userInfoSimpatii = (TextView) rowView.findViewById(R.id.userInfoSimpatii);
            rowView.setTag(holder);
        } else {
            holder = (FlirtikiFragmentAdapter.ViewHolder) rowView.getTag();
        }

        final FlirtikItem flirtik = this.flirtik.get(position);

//        //Устанавливаем аватар
        if(flirtik.avatar != null) {
            Picasso
                    .with(getContext())
                    .load(Constant.upload + flirtik.avatar.micro)
                    .resize(200, 200)
                    .centerCrop()
                    .transform(new CircularTransformation(0))
                    .into(holder.userAvatarSimpatii);
        }

        //Устанавливаем имя
        holder.userNicknameSimpatii.setText(flirtik.nickname);

        //Устанавливаем инфу о юзере
        if ((flirtik.birth_date != null) && (flirtik.city_cache != null)) holder.userInfoSimpatii.setText(ConverterDate.convertDateToAge(flirtik.birth_date) + ", " + flirtik.city_cache);
        else if (flirtik.birth_date != null) holder.userInfoSimpatii.setText(ConverterDate.convertDateToAge(flirtik.birth_date));
        else if (flirtik.city_cache != null) holder.userInfoSimpatii.setText(flirtik.city_cache);
        else holder.userInfoSimpatii.setText("");

        return rowView;

    }
}