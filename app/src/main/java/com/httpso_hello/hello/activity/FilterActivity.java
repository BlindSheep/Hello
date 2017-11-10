package com.httpso_hello.hello.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.helper.Settings;

public class FilterActivity extends AppCompatActivity {

    public TextView from;
    public TextView to;
    public Settings stgs;
    public int ageFrom;
    public int ageTo;
    public int gender;
    public RadioButton radioButtonMan;
    public RadioButton radioButtonWoman;
    public TextView save;
    public Spinner birth_date_from;
    public Spinner birth_date_to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        stgs = new Settings(getApplicationContext());
        ageFrom = stgs.getSettingInt("ageFrom");
        ageTo = stgs.getSettingInt("ageTo");
        gender = stgs.getSettingInt("gender");
        radioButtonMan = (RadioButton) findViewById(R.id.radioButtonMan);
        radioButtonWoman = (RadioButton) findViewById(R.id.radioButtonWoman);
        birth_date_from = (Spinner) findViewById(R.id.birth_date_from);
        birth_date_to = (Spinner) findViewById(R.id.birth_date_to);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (gender == 1) {
            radioButtonMan.setChecked(true);
            radioButtonWoman.setChecked(false);
        } else if (gender == 2) {
            radioButtonMan.setChecked(false);
            radioButtonWoman.setChecked(true);
        } else {
            radioButtonMan.setChecked(false);
            radioButtonWoman.setChecked(false);
        }

        birth_date_from.setSelection(ageFrom);
        birth_date_to.setSelection(ageTo);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonMan.isChecked()) stgs.setSettingInt("gender", 1);
                if (radioButtonWoman.isChecked()) stgs.setSettingInt("gender", 2);
                stgs.setSettingInt("ageFrom", birth_date_from.getSelectedItemPosition());
                stgs.setSettingInt("ageTo", birth_date_to.getSelectedItemPosition());
                Toast.makeText(getApplicationContext(), "Настройки успешно сохранены", Toast.LENGTH_LONG).show();
                finish();
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
}
