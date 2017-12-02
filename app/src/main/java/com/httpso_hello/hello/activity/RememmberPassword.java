package com.httpso_hello.hello.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.helper.Auth;

import static android.R.style.Animation_Dialog;

public class RememmberPassword extends AppCompatActivity {

    private ActionBar actionBar;
    private TextView sendEmail;
    private EditText enterEmail;
    private PopupWindow popUpWindow;
    private View popupView;

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
        popupView = getLayoutInflater().inflate(R.layout.popup_for_wait, null);
        popUpWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
        popUpWindow.setWidth(displaymetrics.widthPixels);
        popUpWindow.setHeight(displaymetrics.heightPixels);
        popUpWindow.setAnimationStyle(Animation_Dialog);
        ((TextView) popupView.findViewById(R.id.textForWaiting)).setText("Восстанавливаем пароль...");

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpWindow.showAtLocation((LinearLayout) findViewById(R.id.RL), Gravity.CENTER, 0, 0);
                String getEmail = enterEmail.getText().toString();
                new Auth(getApplicationContext()).authRestore(getEmail, getApplicationContext(), new Auth.AuthRestoreCallBack() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "На ваш E-mail отправлено письмо. Если письмо не пришло, пожалуйста, проверьте папку \"Спам\"", Toast.LENGTH_LONG).show();
                        finish();
                        popUpWindow.dismiss();
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        Toast.makeText(getApplicationContext(), "Произошла какая-то ошибка. Попробуйте позже.", Toast.LENGTH_LONG).show();
                        finish();
                        popUpWindow.dismiss();
                    }

                    @Override
                    public void onInternetError() {
                        Toast.makeText(getApplicationContext(), "Произошла какая-то ошибка. Попробуйте позже.", Toast.LENGTH_LONG).show();
                        finish();
                        popUpWindow.dismiss();
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
