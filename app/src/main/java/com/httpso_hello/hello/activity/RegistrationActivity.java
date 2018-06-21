package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Registration;
import com.httpso_hello.hello.helper.Auth;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;

public class RegistrationActivity extends AppCompatActivity {

    private TextView btnForReg;
    private TextView btnLinkToEnter;
    private EditText inputNameForReg;
    private EditText inputTelefoneForReg;
    private EditText inputPasswordForReg1;
    private EditText inputPasswordForReg2;
    private Auth auth;
    private ActionBar actionBar;
    private CheckBox checkBoxPrivatePolice;
    private ProgressBar progressBarRegistration;
    private Settings stgs;

    //Кнопка назад(переход на страницу входа)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = new Auth(getApplicationContext());

        inputNameForReg = (EditText) findViewById(R.id.inputNameForReg);
        inputTelefoneForReg = (EditText) findViewById(R.id.inputTelefoneForReg);
        inputPasswordForReg1 = (EditText) findViewById(R.id.inputPasswordForReg1);
        inputPasswordForReg2 = (EditText) findViewById(R.id.inputPasswordForReg2);
        btnForReg = (TextView) findViewById(R.id.btnForReg);
        btnLinkToEnter = (TextView) findViewById(R.id.btnLinkToEnter);
        actionBar = getSupportActionBar();
        checkBoxPrivatePolice = (CheckBox) findViewById(R.id.checkBoxPrivatePolice);
        stgs = new Settings(getApplicationContext());

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Переход на страницу входа
        btnLinkToEnter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Кнопка Регистрации
        btnForReg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nickname = inputNameForReg.getText().toString().trim();
                final String telefone = inputTelefoneForReg.getText().toString().trim();
                String password1 = inputPasswordForReg1.getText().toString().trim();
                String password2 = inputPasswordForReg2.getText().toString().trim();
                if ((!nickname.isEmpty()) && (!telefone.isEmpty()) && (!password1.isEmpty()) && (!password2.isEmpty()) && (password1.equals(password2)) && checkBoxPrivatePolice.isChecked() && (password1.length() > 5)){
                    //Если всё заполнено правильно
                    btnForReg.setText("Регистрация...");
                    btnForReg.setTextColor(getResources().getColor(R.color.main_grey_color_hello));
                    auth.registration(telefone, password1, nickname, new Auth.RegistrationFinishCallBack() {
                        @Override
                        public void onSuccess(Registration registration) {
                            stgs.setSetting("token", registration.registeredUser.token);
                            stgs.setSettingInt("userId", registration.registeredUser.userId);
                            stgs.setSetting("nickname", registration.registeredUser.nickname);
                            Intent intent = new Intent(RegistrationActivity.this, AccountActivateActivity.class);
                            intent.putExtra("isRegistration", true);
                            intent.putExtra("telefone", telefone.toString());
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(int error_code, String error_msg) {
                            btnForReg.setText("Зарегистрироваться");
                            btnForReg.setTextColor(getResources().getColor(R.color.main_red_color_hello));
                            if (error_code == 100) {
                                Toast.makeText(getApplicationContext(), "Пользователь с таким E-mail уже существует", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    if (nickname.isEmpty()) Toast.makeText(getApplicationContext(), "Введите имя", Toast.LENGTH_LONG).show();
                    else if (telefone.isEmpty()) Toast.makeText(getApplicationContext(), "Введите телефон", Toast.LENGTH_LONG).show();
                    else if (password1.isEmpty()) Toast.makeText(getApplicationContext(), "Введите пароль", Toast.LENGTH_LONG).show();
                    else if (password2.isEmpty()) Toast.makeText(getApplicationContext(), "Повторите пароль", Toast.LENGTH_LONG).show();
                    else if (!password1.equals(password2)) Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_LONG).show();
                    else if (!checkBoxPrivatePolice.isChecked()) Toast.makeText(getApplicationContext(), "Примите условия обработки персональных данных", Toast.LENGTH_LONG).show();
                    else if (password1.length() < 6) Toast.makeText(getApplicationContext(), "Пароль должен содержать минимум 6 символов", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
