package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.BalanceReq;
import com.httpso_hello.hello.activity.Super.SocketActivity;
import com.httpso_hello.hello.helper.Billing;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.push_services.TokenReq;

public class UpAnketActivity extends SocketActivity {

    private TextView position;
    private TextView priceUpAnket;
    private TextView riseAnket;
    private TextView balance;
    private TextView nedostatochno;
    private Billing billing;
    private String raisingToken;
    private ProgressBar progressBarUpAnket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_anket);
        setHeader();
        setMenuItem("ServisesActivity");

        position = (TextView) findViewById(R.id.position);
        priceUpAnket = (TextView) findViewById(R.id.priceUpAnket);
        riseAnket = (TextView) findViewById(R.id.riseAnket);
        balance = (TextView) findViewById(R.id.balance);
        nedostatochno = (TextView) findViewById(R.id.nedostatochno);
        progressBarUpAnket = (ProgressBar) findViewById(R.id.progressBarUpAnket);

        Profile.getInstance(getApplicationContext())
                .getPosistion(new Profile.GetPosistionCallback() {
                    @Override
                    public void onSuccess(BalanceReq balanceReq) {
                        int balanceInt = stgs.getSettingInt("balance");
                        balance.setText(Integer.toString(balanceInt) + " баллов");
                        priceUpAnket.setText(Integer.toString(balanceReq.paidRaising) + " баллов");
                        if (!balanceReq.goodPosition) {
                            position.setTextColor(getResources().getColor(R.color.main_red_color_hello));
                        } else {
                            position.setTextColor(getResources().getColor(R.color.main_green_color_hello));
                        }
                        position.setText(Integer.toString(balanceReq.position) + " место");

                        progressBarUpAnket.setVisibility(View.GONE);

                        if(balanceInt - balanceReq.paidRaising < 0) {
                            // если не хватает средств
                            riseAnket.setText("Пополнить баланс");
                            nedostatochno.setVisibility(View.VISIBLE);
                            riseAnket.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(UpAnketActivity.this, BillingActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            // если хватает средств
                            riseAnket.setText("Поднять анкету");
                            nedostatochno.setVisibility(View.GONE);
                            riseAnket.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //поднимаем анкету
                                progressBarUpAnket.setVisibility(View.VISIBLE);
                                billing.getInstance(getApplicationContext()).pay("RAIZING" ,new Billing.PayCallback() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(getApplicationContext(), "Анкета успешно поднята", Toast.LENGTH_LONG).show();
                                        finish();
                                    }

                                    @Override
                                    public void onError(int error_code, String error_msg) {
                                        showMessage("Что-то пошло не так, попробуйте позднее!");
                                    }

                                    @Override
                                    public void onInternetError() {
                                        showMessage("Ошибка интернет соединения");
                                    }
                                });

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        progressBarUpAnket.setVisibility(View.GONE);
                    }

                    @Override
                    public void onInternetError() {
                        progressBarUpAnket.setVisibility(View.GONE);
                    }
                });
    }
}
