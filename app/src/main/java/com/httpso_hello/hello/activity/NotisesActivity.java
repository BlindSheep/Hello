package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.NoticeItem;
import com.httpso_hello.hello.adapters.NoticesAdapter;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Notice;

import java.util.ArrayList;
import java.util.Collections;

public class NotisesActivity extends SuperMainActivity{

    private ListView notices_list_new;
    private ListView notices_list_old;
    private TextView notices_text_new;
    private TextView notices_text_old;
    private View header;
    private Notice notice;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notises);
        setHeader();
        setMenuItem("NotisesActivity");

        notices_list_new = ((ListView) findViewById(R.id.notices_list_new));
        notices_list_old = ((ListView) findViewById(R.id.notices_list_old));
        notices_text_new = ((TextView) findViewById(R.id.notices_text_new));
        notices_text_old = ((TextView) findViewById(R.id.notices_text_old));

        header = getLayoutInflater().inflate(R.layout.footer6dp, null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        notices_list_new.addFooterView(header);
        notices_list_old.addFooterView(header);
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
                        ArrayList<NoticeItem> newItem = new ArrayList<NoticeItem>();
                        ArrayList<NoticeItem> oldItem = new ArrayList<NoticeItem>();

                        for (int i = 0; i < noticeItem.length; i++){
                            if (noticeItem[i].is_new == 0) oldItem.add(noticeItem[i]);
                            else newItem.add(noticeItem[i]);
                        }

                        final NoticesAdapter likesAdapterNew = new NoticesAdapter(activity, newItem);
                        final NoticesAdapter likesAdapterOld = new NoticesAdapter(activity, oldItem);

                        if (newItem.size() != 0) {
                            notices_text_new.setVisibility(View.VISIBLE);
                            notices_list_new.setVisibility(View.VISIBLE);
                            notices_list_new.setAdapter(likesAdapterNew);
                            notices_list_new.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    NoticeItem noticeItem = likesAdapterNew.getItem(position);
                                    Intent intent = new Intent(NotisesActivity.this, ProfileActivity.class);
                                    intent.putExtra("profile_id", noticeItem.profile_id);
                                    intent.putExtra("profile_nickname", noticeItem.sender_user.nickname);
                                    intent.putExtra("avatar", "");
                                    startActivity(intent);
                                }
                            });
                        } else {
                            notices_text_new.setVisibility(View.GONE);
                            notices_list_new.setVisibility(View.GONE);
                        }

                        if (oldItem.size() != 0) {
                            notices_text_old.setVisibility(View.VISIBLE);
                            notices_list_old.setVisibility(View.VISIBLE);
                            notices_list_old.setAdapter(likesAdapterOld);
                            notices_list_old.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    NoticeItem noticeItem = likesAdapterOld.getItem(position);
                                    Intent intent = new Intent(NotisesActivity.this, ProfileActivity.class);
                                    intent.putExtra("profile_id", noticeItem.profile_id);
                                    intent.putExtra("profile_nickname", noticeItem.sender_user.nickname);
                                    intent.putExtra("avatar", "");
                                    startActivity(intent);
                                }
                            });
                        } else {
                            notices_text_old.setVisibility(View.GONE);
                            notices_list_old.setVisibility(View.GONE);
                        }

                        setDynamicHeight(notices_list_new);
                        setDynamicHeight(notices_list_old);
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

    public static void setDynamicHeight(ListView mListView) {
        ListAdapter mListAdapter = mListView.getAdapter();
        if (mListAdapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, mListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
        mListView.setLayoutParams(params);
        mListView.requestLayout();
        }
}
