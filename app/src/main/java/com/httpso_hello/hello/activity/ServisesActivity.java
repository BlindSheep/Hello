package com.httpso_hello.hello.activity;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.BalanceReq;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Profile;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ServisesActivity extends SuperMainActivity{

    private LinearLayout billing;
    private TextView priceUpAnket;
    private TextView position;
    private TextView recomendationUpAnket;
    private LinearLayout raiseTheProfile;
    private Toolbar toolbar;
    private ProgressBar progressBarServises;
    private LinearLayout problems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servises);
        setHeader();
        setMenuItem("ServisesActivity");

        billing = (LinearLayout) findViewById(R.id.billing);
        priceUpAnket = (TextView) findViewById(R.id.priceUpAnket);
        position = (TextView) findViewById(R.id.position);
        recomendationUpAnket = (TextView) findViewById(R.id.recomendationUpAnket);
        raiseTheProfile = (LinearLayout) findViewById(R.id.raiseTheProfile);
        progressBarServises = (ProgressBar) findViewById(R.id.progressBarServises);
        problems = (LinearLayout) findViewById(R.id.problems);

        billing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServisesActivity.this, BillingActivity.class);
                startActivity(intent);
            }
        });

        raiseTheProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServisesActivity.this, UpAnketActivity.class);
                startActivity(intent);
            }
        });

        problems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("contact_id", 3008);
                intent.putExtra("nickname", "Поддержка");
                intent.putExtra("avatar", "ic_launcher.png");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        progressBarServises.setVisibility(View.VISIBLE);
        Profile.getInstance(getApplicationContext())
                .getBalance(new Profile.GetBalanceCallback() {
                    @Override
                    public void onSuccess(BalanceReq balanceReq) {
                        getSupportActionBar().setSubtitle("На счету " + Integer.toString(balanceReq.balance) + " баллов");
                        priceUpAnket.setText("Цена: " + Integer.toString(balanceReq.paid_raising) + " баллов");
                        if (!balanceReq.good_position) {
                            position.setTextColor(getResources().getColor(R.color.main_red_color_hello));
                            recomendationUpAnket.setText("Станьте заметнее и увеличьте количество просмотров, подняв Вашу анкету на первое место!");
                        } else {
                            position.setTextColor(getResources().getColor(R.color.main_green_color_hello));
                            recomendationUpAnket.setText("Вас хорошо видно в поиске!");
                        }
                        position.setText(Integer.toString(balanceReq.position) + " место");
                        progressBarServises.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        progressBarServises.setVisibility(View.GONE);
                    }

                    @Override
                    public void onInternetError() {
                        progressBarServises.setVisibility(View.GONE);
                    }
                });
        super.onResume();
    }
}
