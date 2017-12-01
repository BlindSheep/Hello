package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.httpso_hello.hello.R;

public class SettingsOfGroupActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private LinearLayout settings;
    private LinearLayout followers;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_of_group);

        actionBar = getSupportActionBar();
        settings = (LinearLayout) findViewById(R.id.settings);
        followers = (LinearLayout) findViewById(R.id.followers);
        groupId = getIntent().getExtras().getInt("id");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Настройки и описание
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsOfGroupActivity.this, StgsAndDescOfGroupActivity.class);
                intent.putExtra("id", groupId);
                startActivity(intent);
            }
        });

        //Подписчики
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsOfGroupActivity.this, FollowersActivity.class);
                intent.putExtra("id", groupId);
                intent.putExtra("isAdmin", true);
                startActivity(intent);
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
