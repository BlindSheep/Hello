package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Vote;
import com.httpso_hello.hello.activity.Super.SocketActivity;
import com.httpso_hello.hello.adapters.LikesAdapter;
import com.httpso_hello.hello.helper.Converter;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Like;

import java.util.ArrayList;
import java.util.Collections;

public class LikeActivity extends SocketActivity {

    private Bundle extras;
    private int target_id;
    private String target_controller;
    private String subject;
    private LikesAdapter likesAdapter;
    private View header;
    private ListView LV;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_like);
        setHeader();

        header = getLayoutInflater().inflate(R.layout.footer6dp, null);
        LV = (ListView) findViewById(R.id.listLikes);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        target_id = extras.getInt("target_id");
        subject = extras.getString("subject");
        target_controller = extras.getString("target_controller");

//Заполнение контентом
        swipeRefreshLayout.setRefreshing(true);
        LV.addHeaderView(header);
        LV.addFooterView(header);
        getLikes();

// Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getLikes();
            }
        });
    }

    //Получение юзеров, поставивших лайк
    private void getLikes() {
        Like.getInstance(getApplicationContext()).getInfo(this, target_id, target_controller,
                new Like.GetInfoCallback() {
                    @Override
                    public void onSuccess(final Vote[] votes, Activity activity) {
                        ArrayList<Vote> defolt = new ArrayList<Vote>();
                        Collections.addAll(defolt, votes);
                        likesAdapter = new LikesAdapter(activity, defolt);
                        LV.setAdapter(likesAdapter);
                        getSupportActionBar().setTitle(Converter.likeStr(votes.length));

                        ((ListView) findViewById(R.id.listLikes)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// Обработчик клика по юзеру
                                Vote vote = votes[position - 1];
// Открытие профиля
                                Intent intent = new Intent(LikeActivity.this, ProfileActivity.class);
                                intent.putExtra("profile_id", vote.user.id);
                                intent.putExtra("profile_nickname", " " + vote.user.nickname);
                                startActivity(intent);
                            }
                        });
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        new
                                Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getLikes();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onInternetError() {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getLikes();
                            }
                        }, 5000);
                    }
                }
        );
    }
}