package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.helper.Groups;

public class CreateNewGroupActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private TextView save;
    private EditText title;
    private EditText desc;
    private Switch moderation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);

        actionBar = getSupportActionBar();
        save = (TextView) findViewById(R.id.save);
        title = (EditText) findViewById(R.id.title);
        desc = (EditText) findViewById(R.id.desc);
        moderation = (Switch) findViewById(R.id.moderation);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setOnCreateListenter();
    }

    private void setOnCreateListenter() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = title.getText().toString();
                String description = desc.getText().toString();
                if (!name.equals("")) {
                    save.setText("Сохраняем...");
                    Groups groups = new Groups(getApplicationContext());
                    groups.createGroup(name, description, moderation.isChecked(), new Groups.CreateGroupCallback() {
                                @Override
                                public void onSuccess(int id) {
                                    Intent intent = new Intent(CreateNewGroupActivity.this, OneGroupActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(int error_code, String error_msg) {
                                    Toast.makeText(getApplicationContext().getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                    save.setText("Создать");
                                }

                                @Override
                                public void onInternetError() {
                                    Toast.makeText(getApplicationContext().getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                    save.setText("Создать");
                                }
                            }
                    );
                } else {
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Введите название сообщества", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Кнопка назад(переход на страницу входа)
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
