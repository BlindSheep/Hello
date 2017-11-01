package com.httpso_hello.hello.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;

import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.helper.Auth;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.Settings;

public class MainActivity extends Activity{

    private TextView btnLogin;
    private TextView btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private Auth auth;
    private ProgressBar progressBarLogin;
    private Settings stgs;
    private TextView rememberPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = new Auth(getApplicationContext());
        stgs = new Settings(getApplicationContext());
        //Проверяем авторизовано ли приложения
        if (auth.autoLogion()){
            //Если авторизовано переходит на страницу сообщений
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

            try {
                Profile profile = new Profile(getApplicationContext());
                profile.getProfile(stgs.getSettingInt("user_id"), this, new Profile.GetProfileCallback() {
                    @Override
                    public void onSuccess(User user, Activity activity) {
                        stgs.setSetting("user_nickname", user.nickname);
                        if (user.birth_date != null) {
                            stgs.setSetting("user_age", ConverterDate.convertDateToAge(user.birth_date));
                        }
                        if (user.avatar != null) {
                            stgs.setSetting("user_avatar.micro", Constant.upload + user.avatar.micro);
                        }
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {

                    }

                    @Override
                    public void onInternetError() {

                    }
                });
            } catch (Exception e) {

            }
        }

        setContentView(R.layout.activity_main);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnLinkToRegister = (TextView) findViewById(R.id.btnLinkToRegisterScreen);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
        rememberPassword = (TextView) findViewById(R.id.rememberPassword);

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

        progressBarLogin.setVisibility(View.GONE);

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
                            if (auth.firstLogin()){
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
                    if (email.isEmpty()) Toast.makeText(getApplicationContext(), "Введите электронную почту", Toast.LENGTH_LONG).show();
                    if (password.isEmpty()) Toast.makeText(getApplicationContext(), "Введите пароль", Toast.LENGTH_LONG).show();
                }


            }


        });

    }
}
