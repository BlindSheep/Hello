package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests);
        setHeader();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getGuests();

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGuests();
            }
        });
    }


    private void getGuests() {
        swipeRefreshLayout.setRefreshing(true);
        profile = new Profile(getApplicationContext());
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
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int error_code, String error_msg) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getGuests();
                    }
                }, 5000);
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getGuests();
                    }
                }, 5000);
            }
        });
    }
}
