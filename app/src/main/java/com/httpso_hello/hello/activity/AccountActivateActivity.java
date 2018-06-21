package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.httpso_hello.hello.R;
import com.httpso_hello.hello.helper.Confirmation;
import com.httpso_hello.hello.helper.Profile;

import java.util.HashMap;
import java.util.Map;

public class AccountActivateActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private EditText telNumber;
    private TextView sendButton;
    private EditText smsCode;
    private TextView saveButton;
    private TextView plus;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_activate);

        actionBar = getSupportActionBar();
        telNumber = (EditText) findViewById(R.id.telNumber);
        sendButton = (TextView) findViewById(R.id.sendButton);
        smsCode = (EditText) findViewById(R.id.smsCode);
        saveButton = (TextView) findViewById(R.id.saveButton);
        plus = (TextView) findViewById(R.id.plus);
        progress = (ProgressBar) findViewById(R.id.progress);

        smsCode.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);

        if (getIntent().getExtras().getBoolean("isRegistration")) {
            telNumber.setEnabled(false);
            plus.setVisibility(View.VISIBLE);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            telNumber.setText(getIntent().getExtras().getString("telefone"));
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progress.setVisibility(View.VISIBLE);
                    final Confirmation confirmation = new Confirmation(getApplicationContext());
                    confirmation.startConfirm("register", new Confirmation.StartConfirmCallback() {
                        @Override
                        public void onSuccess() {
                            progress.setVisibility(View.GONE);
                            actionBar.setTitle("+" + telNumber.getText().toString() + " - код выслан");
                            plus.setVisibility(View.GONE);
                            telNumber.setVisibility(View.GONE);
                            sendButton.setVisibility(View.GONE);
                            smsCode.setVisibility(View.VISIBLE);
                            saveButton.setVisibility(View.VISIBLE);
                            saveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progress.setVisibility(View.VISIBLE);
                                    confirmation.endConfirm(smsCode.getText().toString(), "register", new Confirmation.EndConfirmCallback() {
                                        @Override
                                        public void onSuccess() {
                                            Intent intent = new Intent(AccountActivateActivity.this, BoardActivity.class);
                                            finish();
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onError(int error_code, String error_msg) {
                                            Toast.makeText(getApplicationContext().getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                                            progress.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onInternetError() {
                                            Toast.makeText(getApplicationContext().getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                            progress.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onError(int error_code, String error_msg) {
                            Toast.makeText(getApplicationContext().getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                            progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onInternetError() {
                            Toast.makeText(getApplicationContext().getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                            progress.setVisibility(View.GONE);
                        }
                    });
                }
            });
        } else {
            telNumber.setEnabled(true);
            telNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        telNumber.setText("7");
                        plus.setVisibility(View.VISIBLE);
                    }
                }
            });
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((telNumber.getText().toString().length() == 11) && (telNumber.getText().toString().toCharArray()[0] == '7')) {
                        progress.setVisibility(View.VISIBLE);

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("phone", telNumber.getText().toString());
                        GsonBuilder GB = new GsonBuilder();
                        Gson gson = GB.create();
                        String gsonString = gson.toJson(params);
                        Profile profile = new Profile(getApplicationContext());
                        profile.editProfile(gsonString, new Profile.EditProfileCallback() {
                            @Override
                            public void onSuccess() {
                                final Confirmation confirmation = new Confirmation(getApplicationContext());
                                confirmation.startConfirm("register", new Confirmation.StartConfirmCallback() {
                                    @Override
                                    public void onSuccess() {
                                        progress.setVisibility(View.GONE);
                                        actionBar.setTitle("+" + telNumber.getText().toString() + " - код выслан");
                                        plus.setVisibility(View.GONE);
                                        telNumber.setVisibility(View.GONE);
                                        sendButton.setVisibility(View.GONE);
                                        smsCode.setVisibility(View.VISIBLE);
                                        saveButton.setVisibility(View.VISIBLE);
                                        saveButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                progress.setVisibility(View.VISIBLE);
                                                confirmation.endConfirm(smsCode.getText().toString(), "register", new Confirmation.EndConfirmCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        Intent intent = new Intent(AccountActivateActivity.this, BoardActivity.class);
                                                        finish();
                                                        startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onError(int error_code, String error_msg) {
                                                        Toast.makeText(getApplicationContext().getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                                                        progress.setVisibility(View.GONE);
                                                    }

                                                    @Override
                                                    public void onInternetError() {
                                                        Toast.makeText(getApplicationContext().getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                                        progress.setVisibility(View.GONE);
                                                    }
                                                });
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(int error_code, String error_msg) {
                                        Toast.makeText(getApplicationContext().getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                        progress.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onInternetError() {
                                        Toast.makeText(getApplicationContext().getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                        progress.setVisibility(View.GONE);
                                    }
                                });
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                Toast.makeText(getApplicationContext().getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                progress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onInternetError() {
                                Toast.makeText(getApplicationContext().getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                progress.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext().getApplicationContext(), "Введите телефон в формате +71112223344", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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
