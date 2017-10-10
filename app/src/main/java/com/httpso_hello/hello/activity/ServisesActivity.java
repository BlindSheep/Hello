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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servises);

        billing = (LinearLayout) findViewById(R.id.billing);
        priceUpAnket = (TextView) findViewById(R.id.priceUpAnket);
        position = (TextView) findViewById(R.id.position);
        recomendationUpAnket = (TextView) findViewById(R.id.recomendationUpAnket);
        raiseTheProfile = (LinearLayout) findViewById(R.id.raiseTheProfile);
        progressBarServises = (ProgressBar) findViewById(R.id.progressBarServises);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                Intent intent = new Intent(ServisesActivity.this, ProfileActivity.class);
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
    }

    @Override
    protected void onResume() {
        progressBarServises.setVisibility(View.VISIBLE);
        Profile.getInstance(getApplicationContext())
                .getBalance(new Profile.GetBalanceCallback() {
                    @Override
                    public void onSuccess(BalanceReq balanceReq) {
                        toolbar.setSubtitle("На счету " + Integer.toString(balanceReq.balance) + " баллов");
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
