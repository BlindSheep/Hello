package com.httpso_hello.hello.activity.Super;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.activity.BoardActivity;
import com.httpso_hello.hello.activity.ChatActivity;
import com.httpso_hello.hello.activity.FriendsActivity;
import com.httpso_hello.hello.activity.GroupsActivity;
import com.httpso_hello.hello.activity.GuestsActivity;
import com.httpso_hello.hello.activity.MessagesActivity;
import com.httpso_hello.hello.activity.NotisesActivity;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.activity.SearchActivity;
import com.httpso_hello.hello.activity.ServisesActivity;
import com.httpso_hello.hello.activity.SettingOfProfileActivity;
import com.httpso_hello.hello.activity.SettingsActivity;
import com.httpso_hello.hello.activity.SimpationActivity;
import com.httpso_hello.hello.helper.Auth;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Picasso;
import com.yandex.metrica.YandexMetrica;

/**
 * Created by mixir on 26.07.2018.
 */

public class ComponentsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Auth auth;
    public Settings stgs;
    public TextView nav_messages, nav_guests, nav_notises, nav_frinds, nav_simpations;
    public Toolbar toolbar;
    public DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;

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
        nav_simpations = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_simpatii));
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
                .load(stgs.getSettingStr("avatar.micro"))
                .resize(300, 300)
                .centerCrop()
                .transform(new CircularTransformation(0))
                .into(headerImageView);
        if(stgs.getSettingStr("user_age") != null) {
            user_name_and_age_header.setText(stgs.getSettingStr("nickname") + ", " + stgs.getSettingStr("birthDate"));
        } else user_name_and_age_header.setText(stgs.getSettingStr("nickname"));
        user_id_header.setText("Ваш ID " + Integer.toString(stgs.getSettingInt("userId")));
        setCounters();
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
        else if (activityName.equals("SettingOfProfileActivity")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_edit_profile)).setChecked(true);
        else if (activityName.equals("GroupsActivity")) ((MenuItem) navigationView.getMenu().findItem(R.id.nav_groups)).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_profile:
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("profile_id", stgs.getSettingInt("userId"));
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
                intent.putExtra("profile_id", stgs.getSettingInt("userId"));
                startActivity(intent);
                finish();
                break;
            case R.id.nav_notises:
                intent = new Intent(getApplicationContext(), NotisesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_board:
                intent = new Intent(getApplicationContext(), BoardActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_groups:
                intent = new Intent(getApplicationContext(), GroupsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_search:
                intent = new Intent(getApplicationContext(), SearchActivity.class);
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
            case R.id.nav_edit_profile:
                intent = new Intent(getApplicationContext(), SettingOfProfileActivity.class);
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

    public void setCounters() {
        if (stgs.getSettingInt("messages") != 0) {
            nav_messages.setText(Integer.toString(stgs.getSettingInt("messages")));
            nav_messages.setVisibility(View.VISIBLE);
            nav_messages.setGravity(Gravity.CENTER_VERTICAL);
            nav_messages.setTypeface(null, Typeface.BOLD);
        } else{
            nav_messages.setVisibility(View.GONE);
            nav_messages.setText(Integer.toString(stgs.getSettingInt("messages")));
        }
        if (stgs.getSettingInt("guests") != 0) {
            nav_guests.setText(Integer.toString(stgs.getSettingInt("guests")));
            nav_guests.setVisibility(View.VISIBLE);
            nav_guests.setGravity(Gravity.CENTER_VERTICAL);
            nav_guests.setTypeface(null, Typeface.BOLD);
        } else{
            nav_guests.setVisibility(View.GONE);
            nav_notises.setText(Integer.toString(stgs.getSettingInt("notices")));
        }
        if (stgs.getSettingInt("notices") != 0) {
            nav_notises.setText(Integer.toString(stgs.getSettingInt("notices")));
            nav_notises.setVisibility(View.VISIBLE);
            nav_notises.setGravity(Gravity.CENTER_VERTICAL);
            nav_notises.setTypeface(null, Typeface.BOLD);
        } else{
            nav_notises.setVisibility(View.GONE);
            nav_notises.setText(Integer.toString(stgs.getSettingInt("notices")));
        }
        if (stgs.getSettingInt("newFriends") != 0) {
            nav_frinds.setText(Integer.toString(stgs.getSettingInt("newFriends")));
            nav_frinds.setVisibility(View.VISIBLE);
            nav_frinds.setGravity(Gravity.CENTER_VERTICAL);
            nav_frinds.setTypeface(null, Typeface.BOLD);
        } else{
            nav_frinds.setVisibility(View.GONE);
            nav_frinds.setText(Integer.toString(stgs.getSettingInt("newFriends")));
        }
        if (stgs.getSettingInt("mutuallySimpations") + stgs.getSettingInt("incomingSimpations") != 0) {
            nav_simpations.setText(Integer.toString(stgs.getSettingInt("mutuallySimpations") + stgs.getSettingInt("incomingSimpations")));
            nav_simpations.setVisibility(View.VISIBLE);
            nav_simpations.setGravity(Gravity.CENTER_VERTICAL);
            nav_simpations.setTypeface(null, Typeface.BOLD);
        } else{
            nav_simpations.setVisibility(View.GONE);
            nav_simpations.setText(Integer.toString(stgs.getSettingInt("mutuallySimpations") + stgs.getSettingInt("incomingSimpations")));
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        YandexMetrica.getReporter(getApplicationContext(), Constant.metrika_api_key).onResumeSession();
    }

    @Override
    protected void onPause(){
        super.onPause();
        YandexMetrica.getReporter(getApplicationContext(), Constant.metrika_api_key).onPauseSession();
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
}
