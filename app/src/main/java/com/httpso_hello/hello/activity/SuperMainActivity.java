package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.AllCounts;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.helper.Auth;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Messages;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yandex.metrica.YandexMetrica;

import java.util.Timer;
import java.util.TimerTask;

public class SuperMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Auth auth;
    protected Settings stgs;
    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected ActionBarDrawerToggle toggle;
    protected ImageView headerImageView;
    protected TextView nav_messages, nav_guests, nav_notises, nav_frinds;
    private static Handler countsHandler = new Handler();
    private Timer countsTimer;
    private NavigationView navigationView;

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        nav_messages = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_messages));
        nav_guests = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_guests));
        nav_notises = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_notises));
        nav_frinds = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_frinds));
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

    public void setMenuItem(String activityName) {
        if (activityName.equals("BoardActivity")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_board)).setChecked(true);
        else if (activityName.equals("MessagesActivity")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_messages)).setChecked(true);
        else if (activityName.equals("ServisesActivity")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_servises)).setChecked(true);
        else if (activityName.equals("FriendsActivity")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_frinds)).setChecked(true);
        else if (activityName.equals("GuestsActivity")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_guests)).setChecked(true);
        else if (activityName.equals("NotisesActivity")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_notises)).setChecked(true);
        else if (activityName.equals("MyProfile")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_profile)).setChecked(true);
        else if (activityName.equals("SearchActivity")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_search)).setChecked(true);
        else if (activityName.equals("SettingsActivity")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_edit)).setChecked(true);
        else if (activityName.equals("SimpationActivity")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_simpatii)).setChecked(true);
    }

    private void getCountIntoDrawer() {
        countsTimer = new Timer();
        countsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                countsHandler.post(new Runnable() {public void run() {
                    Profile.getInstance(getApplicationContext())
                            .getCount(new Profile.GetCountCallback() {
                        @Override
                        public void onSuccess(AllCounts allCounts) {
                            //Новые сообщения
                            if (allCounts.new_messages != 0) {
                                nav_messages.setVisibility(View.VISIBLE);
                                nav_messages.setGravity(Gravity.CENTER_VERTICAL);
                                nav_messages.setTypeface(null, Typeface.BOLD);
                                nav_messages.setText(Integer.toString(allCounts.new_messages));
                            } else nav_messages.setVisibility(View.GONE);
                            if (allCounts.new_guests != 0) {
                                nav_guests.setVisibility(View.VISIBLE);
                                nav_guests.setGravity(Gravity.CENTER_VERTICAL);
                                nav_guests.setTypeface(null, Typeface.BOLD);
                                nav_guests.setText(Integer.toString(allCounts.new_guests));
                            } else nav_guests.setVisibility(View.GONE);
                            if (allCounts.new_notices != 0) {
                                nav_notises.setVisibility(View.VISIBLE);
                                nav_notises.setGravity(Gravity.CENTER_VERTICAL);
                                nav_notises.setTypeface(null, Typeface.BOLD);
                                nav_notises.setText(Integer.toString(allCounts.new_notices));
                            } else nav_notises.setVisibility(View.GONE);
                            if (allCounts.requests_in_friends != 0) {
                                nav_frinds.setVisibility(View.VISIBLE);
                                nav_frinds.setGravity(Gravity.CENTER_VERTICAL);
                                nav_frinds.setTypeface(null, Typeface.BOLD);
                                nav_frinds.setText(Integer.toString(allCounts.requests_in_friends));
                            } else nav_frinds.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(int error_code, String error_msg) {

                        }

                        @Override
                        public void onInternetError() {

                        }
                    });
                }});
            }
        }, 1000, 30000);
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
            case R.id.nav_frinds:
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
        countsTimer.cancel();
        super.onPause();
        YandexMetrica.getReporter(getApplicationContext(), Constant.metrika_api_key).onPauseSession();
    }
    @Override
    protected void onResume(){
        getCountIntoDrawer();
        super.onResume();
        YandexMetrica.getReporter(getApplicationContext(), Constant.metrika_api_key).onResumeSession();
    }

    public void refreshMenuAvatar(){
        Picasso
                .with(getApplicationContext())
                .load(stgs.getSettingStr("user_avatar.micro"))
                .resize(300, 300)
                .centerCrop()
                .transform(new CircularTransformation(0))
                .into(headerImageView);
    }
}