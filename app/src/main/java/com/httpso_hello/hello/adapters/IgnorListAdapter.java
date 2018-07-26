package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.IgnoreUser;
import com.httpso_hello.hello.activity.IgnorListActivity;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.httpso_hello.hello.helper.ConverterDate.convertDateToAge;

/**
 * Created by mixir on 07.11.2017.
 */

public class IgnorListAdapter extends ArrayAdapter<IgnoreUser> {

    private final Activity context;
    private final Settings stgs;
    private ArrayList<IgnoreUser> ignoreUserList;

    public IgnorListAdapter(Activity context, ArrayList<IgnoreUser> ignoreUserList){
        super(context, R.layout.activity_ignor_list, ignoreUserList);
        this.context = context;
        stgs = new Settings(context.getApplicationContext());
        this.ignoreUserList = ignoreUserList;
    }

    static class ViewHolder{
        public ImageView userAvatarIgnore;
        public TextView userNicknameIgnore;
        public RelativeLayout deleteForIgnore;
        public ImageView deleteButton;
        public ProgressBar progress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final IgnorListAdapter.ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.activity_ignor_list_item, null, true);
            holder = new IgnorListAdapter.ViewHolder();
            holder.userAvatarIgnore = (ImageView) rowView.findViewById(R.id.userAvatarIgnore);
            holder.userNicknameIgnore = (TextView) rowView.findViewById(R.id.userNicknameIgnore);
            holder.deleteForIgnore = (RelativeLayout) rowView.findViewById(R.id.deleteForIgnore);
            holder.deleteButton = (ImageView) rowView.findViewById(R.id.deleteButton);
            holder.progress = (ProgressBar) rowView.findViewById(R.id.progress);
            rowView.setTag(holder);
        } else {
            holder = (IgnorListAdapter.ViewHolder) rowView.getTag();
        }

        final IgnoreUser thisGuest = this.ignoreUserList.get(position);

        if(thisGuest.user.avatar != null) {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.upload + thisGuest.user.avatar.micro))
                    .transform(new CircularTransformation(0))
                    .into(holder.userAvatarIgnore, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso
                                    .with(getContext())
                                    .load(Uri.parse(Constant.default_avatar))
                                    .transform(new CircularTransformation(0))
                                    .into(holder.userAvatarIgnore);
                        }
                    });
        } else {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.default_avatar))
                    .transform(new CircularTransformation(0))
                    .into(holder.userAvatarIgnore);
        }

        holder.userNicknameIgnore.setText(thisGuest.user.nickname);

        holder.deleteButton.setVisibility(View.VISIBLE);
        holder.progress.setVisibility(View.GONE);

        holder.deleteForIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.deleteButton.setVisibility(View.GONE);
                holder.progress.setVisibility(View.VISIBLE);
                Profile.getInstance(getContext())
                        .deleteUserFromIgnore(thisGuest.user.id, new Profile.DeleteUserIgnoreCallback() {
                            @Override
                            public void onSuccess() {
                                IgnorListActivity ILA = ((IgnorListActivity) context);
                                ILA.setContent();
                                Toast.makeText(getContext(), thisGuest.user.nickname + " удален из черного списка", Toast.LENGTH_LONG).show();
                                holder.deleteButton.setVisibility(View.VISIBLE);
                                holder.progress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                Toast.makeText(getContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                holder.deleteButton.setVisibility(View.VISIBLE);
                                holder.progress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onInternetError() {
                                Toast.makeText(getContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                holder.deleteButton.setVisibility(View.VISIBLE);
                                holder.progress.setVisibility(View.GONE);
                            }
                        });
            }
        });

        return rowView;
    }
}
