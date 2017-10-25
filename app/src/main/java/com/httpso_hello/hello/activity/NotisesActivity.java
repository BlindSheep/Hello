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
import com.httpso_hello.hello.Structures.NoticeItem;
import com.httpso_hello.hello.adapters.NoticesAdapter;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Notice;

import java.util.ArrayList;
import java.util.Collections;

public class NotisesActivity extends SuperMainActivity{

    private ListView notices_list;
    private View header;
    private Notice notice;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notises);
        setHeader();

        notices_list = ((ListView) findViewById(R.id.notices_list));
        header = getLayoutInflater().inflate(R.layout.footer6dp, null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        notices_list.addHeaderView(header);
        notices_list.addFooterView(header);
        getNotices();

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotices();
            }
        });
    }

    private void getNotices() {
        swipeRefreshLayout.setRefreshing(true);
        notice = new Notice(getApplicationContext());
        notice.getNotice(this,
                new Notice.GetNoticeCallback() {
                    @Override
                    public void onSuccess(NoticeItem[] noticeItem, Activity activity) {
                        ArrayList<NoticeItem> defolt = new ArrayList<NoticeItem>();
                        Collections.addAll(defolt, noticeItem);

                        final NoticesAdapter likesAdapter = new NoticesAdapter(activity, defolt);
                        notices_list.setAdapter(likesAdapter);
                        notices_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                NoticeItem noticeItem = likesAdapter.getItem(position-1);
                                Intent intent = new Intent(NotisesActivity.this, ProfileActivity.class);
                                intent.putExtra("profile_id", noticeItem.profile_id);
                                intent.putExtra("profile_nickname", noticeItem.sender_user.nickname);
                                intent.putExtra("avatar", "");
                                startActivity(intent);
                            }
                        });

                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Help.ErrorCallback() {
                    @Override
                    public void onError(int error_code, String error_msg) {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getNotices();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onInternetError() {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getNotices();
                            }
                        }, 5000);
                    }
                });
    }
}
