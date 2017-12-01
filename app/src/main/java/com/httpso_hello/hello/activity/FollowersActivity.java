package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.adapters.FollowersAdapter;
import com.httpso_hello.hello.helper.Groups;

import java.util.ArrayList;
import java.util.Collections;

import static android.R.style.Animation_Dialog;

public class FollowersActivity extends AppCompatActivity {

    private int groupId;
    private boolean isAdmin;
    private ListView followers;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FollowersAdapter followersAdapter;
    private ActionBar actionBar;
    private View header;
    private PopupWindow popUpWindowUser;
    private View popupViewUser;
    private ProgressBar launch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        groupId = getIntent().getExtras().getInt("id");
        isAdmin = getIntent().getExtras().getBoolean("isAdmin");
        followers = (ListView) findViewById(R.id.followers);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        actionBar = getSupportActionBar();
        header = getLayoutInflater().inflate(R.layout.footer6dp, null);
        popupViewUser = getLayoutInflater().inflate(R.layout.popup_for_followers, null);
        popUpWindowUser = new PopupWindow(popupViewUser, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        launch = (ProgressBar) popupViewUser.findViewById(R.id.launch);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        followers.addHeaderView(header);
        followers.addFooterView(header);
        getFollowers();

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFollowers();
            }
        });
    }

    private void getFollowers () {
        swipeRefreshLayout.setRefreshing(true);
        Groups groups = new Groups(getApplicationContext());
        groups.getFollowers(groupId, new Groups.GetFollowersCallback() {
            @Override
            public void onSuccess(final User[] users) {
                ArrayList<User> result = new ArrayList<User>();
                Collections.addAll(result, users);
                followersAdapter = new FollowersAdapter(FollowersActivity.this, result, isAdmin, groupId);
                followers.setAdapter(followersAdapter);
                followers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            Intent intent = new Intent(FollowersActivity.this, ProfileActivity.class);
                            intent.putExtra("profile_id", users[position - 1].id);
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
                        getFollowers();
                    }
                }, 5000);
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getFollowers();
                    }
                }, 5000);
            }
        });
    }

    public void getPopup(final int group_id, final int user_id, final String nickname) {
        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
        popUpWindowUser.setWidth(displaymetrics.widthPixels);
        popUpWindowUser.setHeight(displaymetrics.heightPixels);
        popUpWindowUser.setAnimationStyle(Animation_Dialog);
        ((TextView) popupViewUser.findViewById(R.id.zagolovok)).setText(nickname);
        popUpWindowUser.showAtLocation(followers, Gravity.CENTER, 0, 0);
        ((TextView) popupViewUser.findViewById(R.id.deleteFollower)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch.setVisibility(View.VISIBLE);
                com.httpso_hello.hello.helper.Groups groups = new com.httpso_hello.hello.helper.Groups(getApplicationContext());
                groups.subscribe(0, group_id, user_id, new com.httpso_hello.hello.helper.Groups.GetSubscribeCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext().getApplicationContext(), nickname + " удален(а) из сообщества", Toast.LENGTH_LONG).show();
                        launch.setVisibility(View.GONE);
                        popUpWindowUser.dismiss();
                        getFollowers();
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        Toast.makeText(getApplicationContext().getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                        launch.setVisibility(View.GONE);
                    }

                    @Override
                    public void onInternetError() {
                        Toast.makeText(getApplicationContext().getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                        launch.setVisibility(View.GONE);
                    }
                });
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

    @Override
    public void onBackPressed() {
        if (popUpWindowUser.isShowing()) popUpWindowUser.dismiss();
        else super.onBackPressed();
    }

    @Override
    public void onPause(){
        popUpWindowUser.dismiss();
        super.onPause();
    }
}
