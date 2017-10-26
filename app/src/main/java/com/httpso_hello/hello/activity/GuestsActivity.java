package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Guest;
import com.httpso_hello.hello.Structures.NoticeItem;
import com.httpso_hello.hello.adapters.GuestsListAdapter;
import com.httpso_hello.hello.helper.Profile;

import java.util.ArrayList;
import java.util.Collections;


public class GuestsActivity extends SuperMainActivity{

    private ListView listGuestsNew;
    private ListView listGuestsOld;
    private TextView guests_text_new;
    private TextView guests_text_old;
    private View header;
    private Profile profile;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests);
        setHeader();
        setMenuItem("GuestsActivity");

        listGuestsNew = ((ListView) findViewById(R.id.listGuestsNew));
        listGuestsOld = ((ListView) findViewById(R.id.listGuestsOld));
        guests_text_new = ((TextView) findViewById(R.id.guests_text_new));
        guests_text_old = ((TextView) findViewById(R.id.guests_text_old));
        header = getLayoutInflater().inflate(R.layout.footer6dp, null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listGuestsNew.addFooterView(header);
        listGuestsOld.addFooterView(header);
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
                final ArrayList<Guest> newItem = new ArrayList<Guest>();
                final ArrayList<Guest> oldItem = new ArrayList<Guest>();

                for (int i = 0; i < guests.length; i++){
                    if (guests[i].status != 0) oldItem.add(guests[i]);
                    else newItem.add(guests[i]);
                }

                GuestsListAdapter glAdapterNew = new GuestsListAdapter(activity, newItem);
                GuestsListAdapter glAdapterOld = new GuestsListAdapter(activity, oldItem);

                if (newItem.size() != 0) {
                    guests_text_new.setVisibility(View.VISIBLE);
                    listGuestsNew.setVisibility(View.VISIBLE);
                    listGuestsNew.setAdapter(glAdapterNew);
                    listGuestsNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(GuestsActivity.this, ProfileActivity.class);
                            intent.putExtra("profile_id", newItem.get(position).guest_id);
                            intent.putExtra("profile_nickname", " " + newItem.get(position).user_info.nickname);
                            startActivity(intent);
                        }
                    });
                } else {
                    guests_text_new.setVisibility(View.GONE);
                    listGuestsNew.setVisibility(View.GONE);
                }

                if (oldItem.size() != 0) {
                    guests_text_old.setVisibility(View.VISIBLE);
                    listGuestsOld.setVisibility(View.VISIBLE);
                    listGuestsOld.setAdapter(glAdapterOld);
                    listGuestsOld.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(GuestsActivity.this, ProfileActivity.class);
                            intent.putExtra("profile_id", oldItem.get(position).guest_id);
                            intent.putExtra("profile_nickname", " " + oldItem.get(position).user_info.nickname);
                            startActivity(intent);
                        }
                    });
                } else {
                    guests_text_old.setVisibility(View.GONE);
                    listGuestsOld.setVisibility(View.GONE);
                }

                setDynamicHeight(listGuestsNew);
                setDynamicHeight(listGuestsOld);
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
