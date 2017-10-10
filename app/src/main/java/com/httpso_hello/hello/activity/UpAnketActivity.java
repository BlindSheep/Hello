package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.BalanceReq;
import com.httpso_hello.hello.helper.Billing;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Profile;
import com.squareup.picasso.Picasso;

public class UpAnketActivity extends SuperMainActivity{

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

        position = (TextView) findViewById(R.id.position);
        priceUpAnket = (TextView) findViewById(R.id.priceUpAnket);
        riseAnket = (TextView) findViewById(R.id.riseAnket);
        balance = (TextView) findViewById(R.id.balance);
        nedostatochno = (TextView) findViewById(R.id.nedostatochno);
        progressBarUpAnket = (ProgressBar) findViewById(R.id.progressBarUpAnket);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Во все активности перенести, заполнение шапки в меню
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpAnketActivity.this, ProfileActivity.class);
                intent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                startActivity(intent);
                finish();
            }
        });
        ImageView headerImageView = (ImageView) headerLayout.findViewById(R.id.user_avatar_header);
        TextView user_name_and_age_header = (TextView) headerLayout.findViewById(R.id.user_name_and_age_header);
        TextView user_id_header = (TextView) headerLayout.findViewById(R.id.user_id_header);
        Picasso
                .with(getApplicationContext())
                .load(stgs.getSettingStr("user_avatar.micro"))
                .resize(300, 300)
                .centerCrop()
                .transform(new CircularTransformation(0))
                .into(headerImageView);
        if(stgs.getSettingStr("user_age") != null) {
            user_name_and_age_header.setText(stgs.getSettingStr("user_nickname") + ", " + stgs.getSettingStr("user_age"));
        } else user_name_and_age_header.setText(stgs.getSettingStr("user_nickname"));
        user_id_header.setText("Ваш ID " + Integer.toString(stgs.getSettingInt("user_id")));

        billing.getInstance(getApplicationContext()).getRaisingToken(new Billing.GetRaisingTokenCallback() {
            @Override
            public void onSuccess(String token) {
                raisingToken = token;
            }

            @Override
            public void onError(int error_code, String error_msg) {

            }

            @Override
            public void onInternetError() {

            }
        });

        Profile.getInstance(getApplicationContext())
                .getBalance(new Profile.GetBalanceCallback() {
                    @Override
                    public void onSuccess(BalanceReq balanceReq) {
                        balance.setText(Integer.toString(balanceReq.balance) + " баллов");
                        priceUpAnket.setText(Integer.toString(balanceReq.paid_raising) + " баллов");
                        if (!balanceReq.good_position) {
                            position.setTextColor(getResources().getColor(R.color.main_red_color_hello));
                        } else {
                            position.setTextColor(getResources().getColor(R.color.main_green_color_hello));
                        }
                        position.setText(Integer.toString(balanceReq.position) + " место");

                        progressBarUpAnket.setVisibility(View.GONE);

                        if(balanceReq.balance - balanceReq.paid_raising < 0) {
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
                                billing.getInstance(getApplicationContext()).paidRaising(raisingToken ,new Billing.PaidRaisingCallback() {
                                    @Override
                                    public void onSuccess(Boolean response) {
                                        Toast.makeText(getApplicationContext(), "Анкета успешно поднята", Toast.LENGTH_LONG).show();
                                        finish();
                                    }

                                    @Override
                                    public void onError(int error_code, String error_msg) {
                                        if(error_code==401){
                                            // Если время вышло повторяем получение токена и оплату
                                            Log.d("paid_raising", "error");
                                            billing.getInstance(getApplicationContext()).getRaisingToken(new Billing.GetRaisingTokenCallback() {
                                                @Override
                                                public void onSuccess(String token) {
                                                    billing.getInstance(getApplicationContext()).paidRaising(token, new Billing.PaidRaisingCallback() {
                                                        @Override
                                                        public void onSuccess(Boolean response) {
                                                            Toast.makeText(getApplicationContext(), "Анкета успешно поднята", Toast.LENGTH_LONG).show();
                                                            finish();
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

                                    @Override
                                    public void onInternetError() {

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
