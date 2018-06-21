package com.httpso_hello.hello.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.helper.Groups;

public class StgsAndDescOfGroupActivity extends AppCompatActivity {

    private int groupId;
    private ActionBar actionBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText title;
    private EditText desc;
    private Switch moderation;
    private TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stgs_and_desc_of_group);

        groupId = getIntent().getExtras().getInt("id");
        actionBar = getSupportActionBar();
        title = (EditText) findViewById(R.id.title);
        desc = (EditText) findViewById(R.id.desc);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        moderation = (Switch) findViewById(R.id.moderation);
        save = (TextView) findViewById(R.id.save);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setInfo();
        setOnSaveListener();

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setInfo();
            }
        });
    }

    private void setInfo() {
        swipeRefreshLayout.setRefreshing(true);
        com.httpso_hello.hello.helper.Groups groups = new com.httpso_hello.hello.helper.Groups(getApplicationContext());
        groups.getGroups(2, groupId, null, new Groups.GetGroupsCallback() {
            @Override
            public void onSuccess(com.httpso_hello.hello.Structures.Groups[] groupItem) {
                com.httpso_hello.hello.Structures.Groups group = groupItem[0];
                if (group.title != null) title.setText(group.title);
                if (group.description != null) desc.setText(group.description);
                if (!group.moderate) moderation.setChecked(false);
                else moderation.setChecked(true);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int error_code, String error_msg) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        setInfo();
                    }
                }, 5000);
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        setInfo();
                    }
                }, 5000);
            }
        });
    }

    private void setOnSaveListener() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                Groups groups = new Groups(getApplicationContext());
                String titleStr = title.getText().toString();
                String descStr = desc.getText().toString();
                boolean isModerate = moderation.isChecked();
                if (!titleStr.isEmpty()) {
                    groups.editGroup(groupId,titleStr, descStr, isModerate, new Groups.EditGroupCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext().getApplicationContext(), "Данные успешно изменены", Toast.LENGTH_LONG).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(int error_code, String error_msg) {
                            Toast.makeText(getApplicationContext().getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onInternetError() {
                            Toast.makeText(getApplicationContext().getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Введите название сообщества", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    //Кнопка назад(переход на страницу сообщества)
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
