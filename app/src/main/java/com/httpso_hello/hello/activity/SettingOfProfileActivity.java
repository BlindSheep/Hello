package com.httpso_hello.hello.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.httpso_hello.hello.R;

public class SettingOfProfileActivity extends SuperMainActivity {

    private Switch switchMsg;
    private Switch switchComm;
    private Switch switchLikes;
    private Switch switchGifts;

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

        pushSettings();
    }

    //Настройки пушей
    private void pushSettings() {
        //Если 0 значит пуши включены
        if (stgs.getSettingInt("pushMsg") == 0) {
            switchMsg.setChecked(true);
        }
        if (stgs.getSettingInt("pushComm") == 0) {
            switchComm.setChecked(true);
        }
        if (stgs.getSettingInt("pushLikes") == 0) {
            switchLikes.setChecked(true);
        }
        if (stgs.getSettingInt("pushGifts") == 0) {
            switchGifts.setChecked(true);
        }

        switchMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stgs.getSettingInt("pushMsg") == 0) {
                    switchMsg.setChecked(false);
                    stgs.setSettingInt("pushMsg", 1);
                } else {
                    switchMsg.setChecked(true);
                    stgs.setSettingInt("pushMsg", 0);
                }
            }
        });

        switchComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stgs.getSettingInt("pushComm") == 0) {
                    switchComm.setChecked(false);
                    stgs.setSettingInt("pushComm", 1);
                } else {
                    switchComm.setChecked(true);
                    stgs.setSettingInt("pushComm", 0);
                }
            }
        });

        switchLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stgs.getSettingInt("pushLikes") == 0) {
                    switchLikes.setChecked(false);
                    stgs.setSettingInt("pushLikes", 1);
                } else {
                    switchLikes.setChecked(true);
                    stgs.setSettingInt("pushLikes", 0);
                }
            }
        });

        switchGifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stgs.getSettingInt("pushGifts") == 0) {
                    switchGifts.setChecked(false);
                    stgs.setSettingInt("pushGifts", 1);
                } else {
                    switchGifts.setChecked(true);
                    stgs.setSettingInt("pushGifts", 0);
                }
            }
        });
    }
}
