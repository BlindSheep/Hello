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
import com.httpso_hello.hello.activity.Super.SocketActivity;
import com.httpso_hello.hello.adapters.NoticesAdapter;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Notice;

import java.util.ArrayList;
import java.util.Collections;

public class NotisesActivity extends SocketActivity {

    private ListView notices_list_new;
    private View footerForLoading;
    private Notice notice;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NoticesAdapter likesAdapterNew;
    private int page = 0;
    private boolean thatsAll;
    private boolean isLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notises);
        setHeader();
        setMenuItem("NotisesActivity");

        notices_list_new = ((ListView) findViewById(R.id.notices_list_new));
        footerForLoading = getLayoutInflater().inflate(R.layout.footer_loading, null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        notices_list_new.addFooterView(footerForLoading);
        getNotices();

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getNotices();
            }
        });
    }

    private void getNotices() {
        swipeRefreshLayout.setRefreshing(true);
        if (notices_list_new.getFooterViewsCount() == 0) notices_list_new.addFooterView(footerForLoading);
        thatsAll = false;
        isLaunch = true;
        page = 0;
        notice = new Notice(getApplicationContext());
        notice.getNotice(page, this,
                new Notice.GetNoticeCallback() {
                    @Override
                    public void onSuccess(NoticeItem[] noticeItem, Activity activity) {
                        page = page + 1;
                        if ((noticeItem.length == 0) || ((noticeItem.length == 1))) {
                            thatsAll = true;
                            notices_list_new.removeFooterView(footerForLoading);
                        }
                        ArrayList<NoticeItem> newItem = new ArrayList<NoticeItem>();
                        Collections.addAll(newItem, noticeItem);
                        likesAdapterNew = new NoticesAdapter(activity, newItem);
                        notices_list_new.setAdapter(likesAdapterNew);
                        notices_list_new.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                NoticeItem noticeItem = likesAdapterNew.getItem(position);
                                int type = likesAdapterNew.getItem(position).typeNotice;
                                Intent intent;
                                switch (type) {
                                    case 1:
                                        switch(noticeItem.targetController){
                                            case "photos":
                                                intent = new Intent(NotisesActivity.this, ProfileActivity.class);
                                                intent.putExtra("profile_id", noticeItem.profileId);
                                                intent.putExtra("profile_nickname", noticeItem.user.nickname);
                                                intent.putExtra("avatar", "");
                                                startActivity(intent);
                                                break;
                                            case "content":
                                                intent = new Intent(NotisesActivity.this, BoardContentActivity.class);
                                                intent.putExtra("id", noticeItem.targetId);
                                                startActivity(intent);
                                                break;
                                        }
                                        break;
                                    case 2:
                                        switch(noticeItem.targetController){
                                            case "photos":
                                                intent = new Intent(NotisesActivity.this, ProfileActivity.class);
                                                intent.putExtra("profile_id", noticeItem.profileId);
                                                intent.putExtra("profile_nickname", noticeItem.user.nickname);
                                                intent.putExtra("avatar", "");
                                                startActivity(intent);
                                                break;
                                            case "content":
                                                intent = new Intent(NotisesActivity.this, BoardContentActivity.class);
                                                intent.putExtra("id", noticeItem.targetId);
                                                startActivity(intent);
                                                break;
                                        }
                                        break;
                                    case 3:
                                        intent = new Intent(NotisesActivity.this, ProfileActivity.class);
                                        intent.putExtra("profile_id", noticeItem.profileId);
                                        intent.putExtra("profile_nickname", noticeItem.user.nickname);
                                        intent.putExtra("avatar", "");
                                        startActivity(intent);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                        swipeRefreshLayout.setRefreshing(false);
                        isLaunch = false;
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

    public void getNewPages() {
        if (!thatsAll && !isLaunch) {
            isLaunch = true;
            notice = new Notice(getApplicationContext());
            notice.getNotice(page, this,
                    new Notice.GetNoticeCallback() {
                        @Override
                        public void onSuccess(NoticeItem[] noticeItem, Activity activity) {
                            page = page + 1;
                            if ((noticeItem.length == 0) || ((noticeItem.length == 1))) {
                                thatsAll = true;
                                notices_list_new.removeFooterView(footerForLoading);
                            }
                            ArrayList<NoticeItem> newItem = new ArrayList<NoticeItem>();
                            Collections.addAll(newItem, noticeItem);
                            likesAdapterNew.addAll(newItem);
                            likesAdapterNew.notifyDataSetChanged();
                            isLaunch = false;
                        }
                    },
                    new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {
                            isLaunch = false;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getNewPages();
                                }
                            }, 5000);
                        }

                        @Override
                        public void onInternetError() {
                            isLaunch = false;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getNewPages();
                                }
                            }, 5000);
                        }
                    });
        }
    }
}
