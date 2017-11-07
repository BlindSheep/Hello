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
import com.httpso_hello.hello.Structures.IgnoreUser;
import com.httpso_hello.hello.adapters.IgnorListAdapter;
import com.httpso_hello.hello.helper.Profile;

import java.util.ArrayList;
import java.util.Collections;

public class IgnorListActivity extends AppCompatActivity {

    private ListView LV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ignor_list);

        LV = (ListView) findViewById(R.id.ignorList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        header = getLayoutInflater().inflate(R.layout.footer6dp, null);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContent();
        LV.addHeaderView(header);
        LV.addFooterView(header);

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setContent();
            }
        });
    }

    public void setContent() {
        swipeRefreshLayout.setRefreshing(true);
        Profile.getInstance(getApplicationContext())
                .getIgnoreList(new Profile.GetIgnoreListCallback() {
                    @Override
                    public void onSuccess(IgnoreUser[] iu) {
                        final ArrayList<IgnoreUser> ignoreUsers = new ArrayList<IgnoreUser>();
                        Collections.addAll(ignoreUsers, iu);
                        IgnorListAdapter ILS = new IgnorListAdapter(IgnorListActivity.this, ignoreUsers);
                        LV.setAdapter(ILS);
                        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(IgnorListActivity.this, ProfileActivity.class);
                                intent.putExtra("profile_id", ignoreUsers.get(position - 1).user_id);
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
                                setContent();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onInternetError() {
                        new
                                Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                setContent();
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
