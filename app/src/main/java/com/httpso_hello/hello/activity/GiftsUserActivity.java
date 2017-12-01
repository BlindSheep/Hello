package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.GiftItem;
import com.httpso_hello.hello.Structures.Vote;
import com.httpso_hello.hello.adapters.GiftsUserAdapter;
import com.httpso_hello.hello.adapters.LikesAdapter;
import com.httpso_hello.hello.helper.Gifts;

import java.util.ArrayList;
import java.util.Collections;

public class GiftsUserActivity extends AppCompatActivity {

    private Bundle extras;
    private ListView LV;
    private View header;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_gifts_user);

        LV = (ListView) findViewById(R.id.listGifts);
        header = getLayoutInflater().inflate(R.layout.header, null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LV.addFooterView(header);
        getGifts();

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGifts();
            }
        });
    }

    private void getGifts() {
        swipeRefreshLayout.setRefreshing(true);
        Gifts.getInstance(getApplicationContext())
                .getGifts(extras.getInt("user_id"), new Gifts.GetGiftsCallback() {
                    @Override
                    public void onSuccess(GiftItem[] gi) {
                        final ArrayList<GiftItem> defolt = new ArrayList<GiftItem>();
                        Collections.addAll(defolt, gi);
                        GiftsUserAdapter giftsUserAdapter = new GiftsUserAdapter(GiftsUserActivity.this, defolt, extras.getBoolean("isUserProfile"));
                        LV.setAdapter(giftsUserAdapter);
                        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (extras.getBoolean("isUserProfile") || (defolt.get(position).isPrivate == 0)) {
                                    Intent intent = new Intent(GiftsUserActivity.this, ProfileActivity.class);
                                    intent.putExtra("profile_id", defolt.get(position).sender);
                                    startActivity(intent);
                                }
                            }
                        });
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getGifts();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onInternetError() {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getGifts();
                            }
                        }, 5000);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }
}
