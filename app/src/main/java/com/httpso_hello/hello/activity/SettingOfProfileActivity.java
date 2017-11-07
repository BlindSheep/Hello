package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;

import com.httpso_hello.hello.R;

public class SettingOfProfileActivity extends SuperMainActivity {

    private Switch switchMsg;
    private Switch switchComm;
    private Switch switchLikes;
    private Switch switchGifts;
    private RadioButton radioButtonProfile;
    private RadioButton radioButtonMsg;
    private RadioButton radioButtonSearch;
    private RadioButton radioButtonBoard;
    private LinearLayout ignorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_of_profile);
        setHeader();
        setMenuItem("SettingOfProfileActivity");

        switchMsg = (Switch) findViewById(R.id.switchMsg);
        switchComm = (Switch) findViewById(R.id.switchComm);
        switchLikes = (Switch) findViewById(R.id.switchLikes);
        switchGifts = (Switch) findViewById(R.id.switchGifts);
        radioButtonProfile = (RadioButton) findViewById(R.id.radioButtonProfile);
        radioButtonMsg = (RadioButton) findViewById(R.id.radioButtonMsg);
        radioButtonSearch = (RadioButton) findViewById(R.id.radioButtonSearch);
        radioButtonBoard = (RadioButton) findViewById(R.id.radioButtonBoard);
        ignorList = (LinearLayout) findViewById(R.id.ignorList);

        pushSettings();
        startPageSettings();
        setOnClickOnIgnorList();
    }

    //Настройки пушей
    private void pushSettings() {
        //Если 0 значит пуши включены
        if (stgs.getSettingInt("message") == 0) {
            switchMsg.setChecked(true);
        }
        if (stgs.getSettingInt("comment") == 0) {
            switchComm.setChecked(true);
        }
        if (stgs.getSettingInt("rating") == 0) {
            switchLikes.setChecked(true);
        }
        if (stgs.getSettingInt("gift") == 0) {
            switchGifts.setChecked(true);
        }

        switchMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stgs.getSettingInt("message") == 0) {
                    switchMsg.setChecked(false);
                    stgs.setSettingInt("message", 1);
                } else {
                    switchMsg.setChecked(true);
                    stgs.setSettingInt("message", 0);
                }
            }
        });

        switchComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stgs.getSettingInt("comment") == 0) {
                    switchComm.setChecked(false);
                    stgs.setSettingInt("comment", 1);
                } else {
                    switchComm.setChecked(true);
                    stgs.setSettingInt("comment", 0);
                }
            }
        });

        switchLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stgs.getSettingInt("rating") == 0) {
                    switchLikes.setChecked(false);
                    stgs.setSettingInt("rating", 1);
                } else {
                    switchLikes.setChecked(true);
                    stgs.setSettingInt("rating", 0);
                }
            }
        });

        switchGifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stgs.getSettingInt("gift") == 0) {
                    switchGifts.setChecked(false);
                    stgs.setSettingInt("gift", 1);
                } else {
                    switchGifts.setChecked(true);
                    stgs.setSettingInt("gift", 0);
                }
            }
        });
    }

    //Стартовая страница
    private void startPageSettings() {
        if (stgs.getSettingInt("startPage") == 3) {
            radioButtonProfile.setChecked(true);
            radioButtonMsg.setChecked(false);
            radioButtonSearch.setChecked(false);
            radioButtonBoard.setChecked(false);
        }
        if (stgs.getSettingInt("startPage") == 2) {
            radioButtonProfile.setChecked(false);
            radioButtonMsg.setChecked(true);
            radioButtonSearch.setChecked(false);
            radioButtonBoard.setChecked(false);
        }
        if (stgs.getSettingInt("startPage") == 1) {
            radioButtonProfile.setChecked(false);
            radioButtonMsg.setChecked(false);
            radioButtonSearch.setChecked(true);
            radioButtonBoard.setChecked(false);
        }
        if (stgs.getSettingInt("startPage") == 0) {
            radioButtonProfile.setChecked(false);
            radioButtonMsg.setChecked(false);
            radioButtonSearch.setChecked(false);
            radioButtonBoard.setChecked(true);
        }

        radioButtonProfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) stgs.setSettingInt("startPage", 3);
            }
        });
        radioButtonMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) stgs.setSettingInt("startPage", 2);
            }
        });
        radioButtonSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) stgs.setSettingInt("startPage", 1);
            }
        });
        radioButtonBoard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) stgs.setSettingInt("startPage", 0);
            }
        });
    }

    private void setOnClickOnIgnorList() {
        ignorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingOfProfileActivity.this, IgnorListActivity.class);
                startActivity(intent);
            }
        });
    }
}
