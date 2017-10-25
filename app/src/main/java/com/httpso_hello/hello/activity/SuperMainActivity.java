package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.helper.Auth;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Picasso;
import com.yandex.metrica.YandexMetrica;

public class SuperMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Auth auth;
    protected Settings stgs;
    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stgs = new Settings(getApplicationContext());
        YandexMetrica.activate(getApplicationContext(), Constant.metrika_api_key);
        YandexMetrica.enableActivityAutoTracking(getApplication());

    }

    public void setHeader() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//"StatementWithEmptyBody"
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        switch (id) {
            case R.id.nav_profile:
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                startActivity(intent);
                finish();
                break;
            case R.id.nav_messages:
                intent = new Intent(getApplicationContext(), MessagesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_guests:
                intent = new Intent(getApplicationContext(), GuestsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_simpatii:
                intent = new Intent(getApplicationContext(), SimpationActivity.class);
                startActivity(intent);
                finish();
                break;
            case
                    R.id.nav_frinds:
                intent = new Intent(getApplicationContext(), FriendsActivity.class);
                intent.putExtra("profile_id", 0);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_notises:
                intent = new Intent(getApplicationContext(), NotisesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_search:
                intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_board:
                intent = new Intent(getApplicationContext(), BoardActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_servises:
                intent = new Intent(getApplicationContext(), ServisesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_edit:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_support:
                intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("contact_id", 3008);
                intent.putExtra("nickname", "Поддержка");
                intent.putExtra("avatar", "ic_launcher.png");
                startActivity(intent);
                finish();
                break;
            case R.id.nav_exit:
                auth = new Auth(getApplicationContext());
                auth.logout(new Auth.LogoutFinishingCallback() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getApplicationContext(),
// TODO: 24.07.2017 Сделать локализацию
                                "Что то пошло не так", Toast.LENGTH_LONG)
                                .show();
                    }
                });
                break;

            default:
                intent = new Intent(getApplicationContext(), NotisesActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onPause(){
        super.onPause();
        YandexMetrica.getReporter(getApplicationContext(), Constant.metrika_api_key).onPauseSession();
    }
    @Override
    protected void onResume(){
        super.onResume();
        YandexMetrica.getReporter(getApplicationContext(), Constant.metrika_api_key).onResumeSession();
    }
}