package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.helper.Groups;
import com.httpso_hello.hello.helper.Help;

import static android.R.style.Animation_Dialog;

public class SettingsOfGroupActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private LinearLayout settings;
    private LinearLayout followers;
    private LinearLayout deleteGroup;
    private int groupId;
    private PopupWindow popUpWindowForDelete;
    private View popupViewForDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_of_group);

        actionBar = getSupportActionBar();
        settings = (LinearLayout) findViewById(R.id.settings);
        followers = (LinearLayout) findViewById(R.id.followers);
        deleteGroup = (LinearLayout) findViewById(R.id.deleteGroup);
        groupId = getIntent().getExtras().getInt("id");
        popupViewForDelete = getLayoutInflater().inflate(R.layout.popup_for_delete_group, null);
        popUpWindowForDelete = new PopupWindow(popupViewForDelete, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
        popUpWindowForDelete.setWidth(displaymetrics.widthPixels);
        popUpWindowForDelete.setHeight(displaymetrics.heightPixels);
        popUpWindowForDelete.setAnimationStyle(Animation_Dialog);

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

        deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpWindowForDelete.showAtLocation((ScrollView) findViewById(R.id.parent), Gravity.CENTER, 0, 0);
                ((TextView) popupViewForDelete.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpWindowForDelete.dismiss();
                    }
                });
                ((TextView) popupViewForDelete.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((TextView) popupViewForDelete.findViewById(R.id.delete)).setText("Удаляем...");
                        final Groups groups = new Groups(getApplicationContext());
                        groups.deleteGroup(groupId, new Groups.DeleteGroupCallback() {
                            @Override
                            public void onSuccess() {
                                popUpWindowForDelete.dismiss();
                                Toast.makeText(getApplicationContext().getApplicationContext(), "Группа удалена", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SettingsOfGroupActivity.this, GroupsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, new Help.ErrorCallback() {
                            @Override
                            public void onError(int error_code, String error_msg) {
                                ((TextView) popupViewForDelete.findViewById(R.id.delete)).setText("Удалить");
                                Toast.makeText(getApplicationContext().getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onInternetError() {
                                ((TextView) popupViewForDelete.findViewById(R.id.delete)).setText("Удалить");
                                Toast.makeText(getApplicationContext().getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                });
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

    @Override
    public void onBackPressed() {
        if (popUpWindowForDelete.isShowing()) popUpWindowForDelete.dismiss();
        else super.onBackPressed();
    }

    @Override
    public void onPause() {
        if (popUpWindowForDelete != null) popUpWindowForDelete.dismiss();
        super.onPause();
    }
}
