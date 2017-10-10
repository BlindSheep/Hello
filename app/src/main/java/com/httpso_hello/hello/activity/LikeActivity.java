package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Vote;
import com.httpso_hello.hello.adapters.LikesAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Friend;
import com.httpso_hello.hello.helper.Like;
import com.httpso_hello.hello.helper.Simpation;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class LikeActivity extends SuperMainActivity{

    private Bundle extras;
    private int target_id;
    private String target_controller;
    private String subject;
    private LikesAdapter likesAdapter;
    private ProgressBar progressBarLike;
    private View header;
    private ListView LV;
    private LinearLayout llBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_like);

        progressBarLike = (ProgressBar) findViewById(R.id.progressBarLike);
        header = getLayoutInflater().inflate(R.layout.header, null);
        LV = (ListView) findViewById(R.id.listLikes);
        llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet_more_info);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Во все активности перенести, заполнение шапки в меню
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikeActivity.this, ProfileActivity.class);
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

        target_id = extras.getInt("target_id");
        subject = extras.getString("subject");
        target_controller = extras.getString("target_controller");

        LV.addHeaderView(header);
        LV.addFooterView(header);

        Like.getInstance(getApplicationContext()).getInfo(this, target_id, subject, target_controller,
                new Like.GetInfoCallback() {
                    @Override
                    public void onSuccess(final Vote[] votes, Activity activity) {
                        ArrayList<Vote> defolt = new ArrayList<Vote>();
                        Collections.addAll(defolt, votes);
                        likesAdapter = new LikesAdapter(activity, defolt);
                        LV.setAdapter(likesAdapter);
                        getSupportActionBar().setTitle(ConverterDate.likeStr(votes.length));

                        ((ListView) findViewById(R.id.listLikes)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // Обработчик клика по юзеру
                                Vote vote = votes[position - 1];
                                // Открытие профиля
                                Intent intent = new Intent(LikeActivity.this, ProfileActivity.class);
                                intent.putExtra("profile_id", vote.user_id);
                                intent.putExtra("profile_nickname", " " + vote.user_nickname);
                                startActivity(intent);
                            }
                        });

                        progressBarLike.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {

                    }

                    @Override
                    public void onInternetError() {

                    }
                }
        );
    }
}
