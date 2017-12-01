package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Groups;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 24.11.2017.
 */

public class GroupsAdapter extends ArrayAdapter<Groups> {

    private ArrayList<Groups> groups;
    private final Activity context;
    private Settings stgs;

    public GroupsAdapter(Activity context, ArrayList<Groups> groups){
        super(context, R.layout.content_groups, groups);
        this.groups = groups;
        this.context = context;
        this.stgs = new Settings(getContext());
    }

    private class ViewHolder{
        public ImageView groupLogo;
        public TextView groupName;
        public TextView groupsFollowers;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        final GroupsAdapter.ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_groups_item, null, true);
            holder = new GroupsAdapter.ViewHolder();
            holder.groupLogo = (ImageView) rowView.findViewById(R.id.groupLogo);
            holder.groupName = (TextView) rowView.findViewById(R.id.groupName);
            holder.groupsFollowers = (TextView) rowView.findViewById(R.id.groupsFollowers);
            rowView.setTag(holder);
        } else {
            holder = (GroupsAdapter.ViewHolder) rowView.getTag();
        }

        final Groups group = this.groups.get(position);



        //// TODO: 30.07.2017 Добавить проверку наличия изображений: проверка заполнености размеров
        if(group.logo != null) {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.upload + group.logo.micro))
                    .transform(new CircularTransformation(0))
                    .into(holder.groupLogo, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso
                                    .with(getContext())
                                    .load(Uri.parse(Constant.default_avatar))
                                    .transform(new CircularTransformation(0))
                                    .into(holder.groupLogo);
                        }
                    });
        } else {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.default_avatar))
                    .transform(new CircularTransformation(0))
                    .into(holder.groupLogo);
        }

        holder.groupName.setText(group.title);
        holder.groupsFollowers.setText(ConverterDate.getFollowers(group.members_count));

        return rowView;

    }

}