package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.NoticeItem;
import com.httpso_hello.hello.adapters.NoticesAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Notice;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class NotisesActivity extends SuperMainActivity{

    private ProgressBar progressBarNotices;
    private ListView notices_list;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notises);

        progressBarNotices = (ProgressBar) findViewById(R.id.progressBarNotices);
        notices_list = ((ListView) findViewById(R.id.notices_list));
        header = getLayoutInflater().inflate(R.layout.header, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Во все активности перенести, заполнение шапки в меню
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotisesActivity.this, ProfileActivity.class);
                intent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                startActivity(intent);
                finish();
            }
        });
        ImageView headerImageView = (ImageView) headerLayout.findViewById(R.id.user_avatar_header);
        TextView user_name_and_age_header = (TextView) headerLayout.findViewById(R.id.user_name_and_age_header);
        TextView user_id_header = (TextView) headerLayout.findViewById(R.id.user_id_header);
        Picasso
                .with(getApplicationContext())
                .load(stgs.getSettingStr("user_avatar.micro"))
                .resize(300, 300)
                .centerCrop()
                .transform(new CircularTransformation(0))
                .into(headerImageView);
        if(stgs.getSettingStr("user_age") != null) {
            user_name_and_age_header.setText(stgs.getSettingStr("user_nickname") + ", " + stgs.getSettingStr("user_age"));
        } else user_name_and_age_header.setText(stgs.getSettingStr("user_nickname"));
        user_id_header.setText("Ваш ID " + Integer.toString(stgs.getSettingInt("user_id")));

        notices_list.addHeaderView(header);
        notices_list.addFooterView(header);

        Notice notice = new Notice(getApplicationContext());
        notice.getNotice(this, new Notice.GetNoticeCallback() {
            @Override
            public void onSuccess(NoticeItem[] noticeItem, Activity activity) {
                ArrayList<NoticeItem> defolt = new ArrayList<NoticeItem>();
                Collections.addAll(defolt, noticeItem);

                NoticesAdapter likesAdapter = new NoticesAdapter(activity, defolt);
                notices_list.setAdapter(likesAdapter);

                progressBarNotices.setVisibility(View.GONE);
            }

            @Override
            public void onError(int error_code, String error_msg) {

            }

            @Override
            public void onInternetError() {

            }
        });
    }
}
