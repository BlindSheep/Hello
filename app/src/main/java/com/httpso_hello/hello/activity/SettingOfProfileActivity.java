package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.helper.AlwaysOnline;
import com.httpso_hello.hello.helper.Auth;

public class SettingOfProfileActivity extends SuperMainActivity {

    private Switch switchMsg;
    private Switch switchComm;
    private Switch switchLikes;
    private Switch switchGifts;
    private Switch switchFlirtiks;
    private RadioButton radioButtonProfile;
    private RadioButton radioButtonMsg;
    private RadioButton radioButtonSearch;
    private RadioButton radioButtonBoard;
    private LinearLayout ignorList;
    private Switch switchOnline;
    private LinearLayout exit;
    private LinearLayout share;
    private LinearLayout newVersion;

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
        switchFlirtiks = (Switch) findViewById(R.id.switchFlirtiks);
        radioButtonProfile = (RadioButton) findViewById(R.id.radioButtonProfile);
        radioButtonMsg = (RadioButton) findViewById(R.id.radioButtonMsg);
        radioButtonSearch = (RadioButton) findViewById(R.id.radioButtonSearch);
        radioButtonBoard = (RadioButton) findViewById(R.id.radioButtonBoard);
        ignorList = (LinearLayout) findViewById(R.id.ignorList);
        switchOnline = (Switch) findViewById(R.id.switchOnline);
        exit = (LinearLayout) findViewById(R.id.exit);
        share = (LinearLayout) findViewById(R.id.share);
        newVersion = (LinearLayout) findViewById(R.id.newVersion);

        pushSettings();
        startPageSettings();
        setOnClickOnIgnorList();
        setAlwaysOnline();
        setExit();
        setShare();
        setNewVersion();
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
        if (stgs.getSettingInt("flirtik") == 1) {
            switchFlirtiks.setChecked(true);
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

        switchFlirtiks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stgs.getSettingInt("flirtik") == 1) {
                    switchFlirtiks.setChecked(false);
                    stgs.setSettingInt("flirtik", 0);
                } else {
                    switchFlirtiks.setChecked(true);
                    stgs.setSettingInt("flirtik", 1);
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

    private void setAlwaysOnline() {
        if (stgs.getSettingInt("always_online") == 0) {
            switchOnline.setChecked(false);
        } else switchOnline.setChecked(true);
        switchOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) stgs.setSettingInt("always_online", 1);
                else stgs.setSettingInt("always_online", 0);

                startService(new Intent(getApplicationContext(), AlwaysOnline.class));
            }
        });
    }

    private void setExit() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth auth = new Auth(getApplicationContext());
                auth.logout(new Auth.LogoutFinishingCallback() {
                    @Override
                    public void onSuccess() {
                        stgs.setSettingInt("always_online", 0);
                        startService(new Intent(getApplicationContext(), AlwaysOnline.class));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getApplicationContext(),
                                "Что то пошло не так", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });
    }

    private void setShare() {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.httpso_hello.hello");
                startActivity(Intent.createChooser(shareIntent, "Поделиться ссылкой на Hello"));
            }
        });
    }

    private void setNewVersion() {
        newVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse("https://play.google.com/store/apps/details?id=com.httpso_hello.hello");
                Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openlinkIntent);
            }
        });
    }
}
