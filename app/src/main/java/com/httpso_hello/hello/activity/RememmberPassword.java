package com.httpso_hello.hello.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.helper.Auth;

public class RememmberPassword extends AppCompatActivity {

    private ActionBar actionBar;
    private TextView sendEmail;
    private EditText enterEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rememmber_password);

        sendEmail = (TextView) findViewById(R.id.sendEmail);
        enterEmail = (EditText) findViewById(R.id.enterEmail);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = enterEmail.getText().toString();
                new Auth(getApplicationContext()).authRestore(getEmail, getApplicationContext(), new Auth.AuthRestoreCallBack() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "На ваш E-mail отправлено письмо. Если письмо не пришло, пожалуйста, проверьте папку \"Спам\"", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        Toast.makeText(getApplicationContext(), "Произошла какая-то ошибка. Попробуйте позже.", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onInternetError() {
                        Toast.makeText(getApplicationContext(), "Произошла какая-то ошибка. Попробуйте позже.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
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
