package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Guest;
import com.httpso_hello.hello.adapters.GuestsListAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import static com.httpso_hello.hello.R.id.listGuests;

public class GuestsActivity extends SuperMainActivity{

    private Profile profile;
    private GuestsListAdapter glAdapter;
    private ProgressBar progressBarGuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.profile = new Profile(getApplicationContext());

        setContentView(R.layout.activity_guests);

        progressBarGuests = (ProgressBar) findViewById(R.id.progressBarGuests);

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
                Intent intent = new Intent(GuestsActivity.this, ProfileActivity.class);
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

        profile.getGuests(1, this, new Profile.GetGuestsCallback() {
            @Override
            public void onSuccess(final Guest[] guests, Activity activity) {

                ArrayList<Guest> defolt = new ArrayList<Guest>();
                Collections.addAll(defolt, guests);

                glAdapter = new GuestsListAdapter(
                        activity,
                        defolt
                );
                ((GridView) findViewById(listGuests)).setAdapter(glAdapter);

                ((GridView) findViewById(listGuests)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Обработчик клика по юзеру
                        Guest guest = ((GuestsListAdapter) parent.getAdapter()).getItem(position);
                        // Открытие профиля
                        Intent intent = new Intent(GuestsActivity.this, ProfileActivity.class);
                        intent.putExtra("profile_id", guest.guest_id);
                        intent.putExtra("profile_nickname", " " + guest.user_info.nickname);
                        startActivity(intent);
                    }
                });

                progressBarGuests.setVisibility(View.GONE);

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
