package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Groups;
import com.httpso_hello.hello.adapters.GroupsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class GroupsActivity extends SuperMainActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private GroupsAdapter groupsAdapter;
    private ListView groupsList;
    private View header;
    private TextView my;
    private TextView all;
    private int parameters = 0;
    private ImageView search;
    private EditText editForSearch;
    private TextView textName;
    private Timer timer = new Timer();
    private Handler Tread1_Handler;
    private double density;
    private ImageView create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        setHeader();
        setMenuItem("GroupsActivity");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        groupsList = (ListView) findViewById(R.id.groupsList);
        header = getLayoutInflater().inflate(R.layout.header_for_all_groups, null);
        my = (TextView) header.findViewById(R.id.my);
        all = (TextView) header.findViewById(R.id.all);
        search = (ImageView) findViewById(R.id.search);
        editForSearch = (EditText) findViewById(R.id.editForSearch);
        textName = (TextView) findViewById(R.id.textName);
        Tread1_Handler = new Handler();
        density = getApplicationContext().getResources().getDisplayMetrics().density;
        create = (ImageView) findViewById(R.id.create);

        //Мои группы
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameters = 0;
                getGroups(null);
                int myInt = (int) (density * 15);
                int allInt = (int) (density * 13);
                my.setPadding(myInt, myInt, myInt, myInt);
                all.setPadding(allInt, allInt, allInt, allInt);
            }
        });

        //Все группы
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameters = 1;
                getGroups(null);
                int myInt = (int) (density * 13);
                int allInt = (int) (density * 15);
                my.setPadding(myInt, myInt, myInt, myInt);
                all.setPadding(allInt, allInt, allInt, allInt);
            }
        });

        //Показать строку поиска
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editForSearch.setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
                textName.setVisibility(View.GONE);
            }
        });

        //Изменения в строке поиска
        editForSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                swipeRefreshLayout.setRefreshing(true);
                if(timer != null) timer.cancel();
            }
            @Override
            public void afterTextChanged(final Editable s) {
                     timer = new Timer();
                     timer.schedule(new TimerTask() {
                         @Override
                         public void run() {
                             Tread1_Handler.post(new Runnable() {public void run() {
                                 getGroups(s.toString());
                                 timer.cancel();
                             }});
                         }
                     }, 1500, 1500);
            }
        });

        //Кнопка создания группы
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupsActivity.this, CreateNewGroupActivity.class);
                startActivity(intent);
            }
        });

        //Получения списка групп
        groupsList.addHeaderView(header);
        getGroups(null);

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGroups(null);
            }
        });
    }

    //Получения списка групп
    private void getGroups (final String search) {
        swipeRefreshLayout.setRefreshing(true);
        com.httpso_hello.hello.helper.Groups.getInstance(getApplicationContext()).getGroups(parameters, 0, search, new com.httpso_hello.hello.helper.Groups.GetGroupsCallback() {
            @Override
            public void onSuccess(Groups[] groupItem) {
                final ArrayList<Groups> list = new ArrayList<Groups>();
                if (groupItem.length != 0) Collections.addAll(list, groupItem);
                groupsAdapter = new GroupsAdapter(GroupsActivity.this, list);
                groupsList.setAdapter(groupsAdapter);
                groupsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(GroupsActivity.this, OneGroupActivity.class);
                        intent.putExtra("id", list.get(position - 1).id);
                        startActivity(intent);
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int error_code, String error_msg) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getGroups(search);
                    }
                }, 5000);
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getGroups(search);
                    }
                }, 5000);
            }
        });
    }

    @Override
    public void onBackPressed () {
        if (editForSearch.isShown()) {
            editForSearch.setVisibility(View.GONE);
            editForSearch.setText(null);
            search.setVisibility(View.VISIBLE);
            textName.setVisibility(View.VISIBLE);
        } else super.onBackPressed();
    }
}
