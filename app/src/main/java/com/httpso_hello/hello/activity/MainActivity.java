package com.httpso_hello.hello.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;

import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.helper.*;

public class MainActivity extends Activity{

    private TextView btnLogin;
    private TextView btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private Auth auth;
    private ProgressBar progressBarLogin;
    private Settings stgs;
    private TextView rememberPassword;
    private RelativeLayout logo;
    private LinearLayout mainContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = new Auth(getApplicationContext());
        stgs = new Settings(getApplicationContext());

        setContentView(R.layout.activity_main);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnLinkToRegister = (TextView) findViewById(R.id.btnLinkToRegisterScreen);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
        rememberPassword = (TextView) findViewById(R.id.rememberPassword);
        logo = (RelativeLayout) findViewById(R.id.logo);
        mainContent = (LinearLayout) findViewById(R.id.mainContent);

        if (auth.autoLogion()) {
            //если авторизован
            mainContent.setVisibility(View.GONE);
            progressBarLogin.setVisibility(View.GONE);
            logo.setVisibility(View.VISIBLE);
            login();
        } else {
            //если не авторизован
            mainContent.setVisibility(View.VISIBLE);
            progressBarLogin.setVisibility(View.GONE);
            logo.setVisibility(View.GONE);

            //Переход на страницу регистрации
            btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            //Восстановить пароль
            rememberPassword.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, RememmberPassword.class);
                    startActivity(intent);
                }
            });

            btnLogin.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    // Проверка заполнения полей
                    if (!email.isEmpty() && !password.isEmpty()) {
                        progressBarLogin.setVisibility(View.VISIBLE);
                        auth.authorize(email, password, new Auth.AuthFinishingCallback() {
                            @Override
                            public void onSuccess(User user) {
                                if (auth.firstLogin()) {
                                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                progressBarLogin.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),
                                        error_msg, Toast.LENGTH_LONG)
                                        .show();
                            }

                            @Override
                            public void onLoccked() {
                                progressBarLogin.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),
                                        "Ваш аккаунт заблокирован, обратитесь в службу поддержки", Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    } else {
                        if (email.isEmpty())
                            Toast.makeText(getApplicationContext(), "Введите электронную почту", Toast.LENGTH_LONG).show();
                        if (password.isEmpty())
                            Toast.makeText(getApplicationContext(), "Введите пароль", Toast.LENGTH_LONG).show();
                    }


                }


            });
        }
    }

    private void login () {
        startService(new Intent(getApplicationContext(), AlwaysOnline.class));
        Profile profile = new Profile(getApplicationContext());
        profile.getSmallUserInfo(new Profile.GetSmallUserInfoCallback() {
            @Override
            public void onSuccess(User user) {
                    //Заполняем инфу
                    stgs.setSetting("user_nickname", user.nickname);
                    if (user.birth_date != null) {
                        stgs.setSetting("user_age", ConverterDate.convertDateToAge(user.birth_date));
                    }
                    if (user.avatar != null) {
                        stgs.setSetting("user_avatar.micro", Constant.host + user.avatar.micro);
                    }

                    //Переходим на нужную страницу
                    Intent intent;
                    if (stgs.getSettingInt("startPage") == 3) {
                        intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                    } else if (stgs.getSettingInt("startPage") == 2) {
                        intent = new Intent(MainActivity.this, MessagesActivity.class);
                    } else if (stgs.getSettingInt("startPage") == 1) {
                        intent = new Intent(MainActivity.this, SearchActivity.class);
                    } else {
                        intent = new Intent(MainActivity.this, BoardActivity.class);
                    }
                    startActivity(intent);
                    finish();
            }
        }, new Help.ErrorCallback() {
            @Override
            public void onError(int error_code, String error_msg) {
                auth.logout(new Auth.LogoutFinishingCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext().getApplicationContext(), "Произошла какая-то ошибка, возможно ваш аккаунт заблокирован! Попробуйте позднее или обратитесь в службу поддержки", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        login();
                    }
                }, 1000);
            }
        });
    }
}
