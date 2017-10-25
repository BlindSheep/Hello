package com.httpso_hello.hello.activity;

import android.Manifest;
import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.*;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.*;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.adapters.SettingsAdapter;
import com.httpso_hello.hello.adapters.SimpationAdapter;
import com.httpso_hello.hello.fragments.SettingsIntrestingFragment;
import com.httpso_hello.hello.fragments.SettingsLookingForFragment;
import com.httpso_hello.hello.fragments.SettingsProfileFragment;
import com.httpso_hello.hello.fragments.SimpationFragment;
import com.httpso_hello.hello.helper.Auth;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Profile;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.R.style.Animation_Dialog;

public class SettingsActivity extends SuperMainActivity{

    private ProgressBar progressBarSettings;
//    private Uri imageUri;
//    private SettingsProfileFragment settingsProfileFragment;
//    private Bundle settingsProfileFragmentArg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setHeader();

        progressBarSettings = (ProgressBar) findViewById(R.id.progressBarSettings);
        auth = new Auth(getApplicationContext());

        //Если юзер зашел первый раз
        if (auth.firstLogin()){
            drawer.openDrawer(GravityCompat.START , true);
            stgs.setSettingInt("firstLogin", 1);
        }

        Profile profiles = new Profile(getApplicationContext());
        profiles.getProfile(stgs.getSettingInt("user_id"), this, new Profile.GetProfileCallback() {
            @Override
            public void onSuccess(final User user, Activity activity) {
                //содаем фрагменты и аргументы
                Bundle settingsIntrestingFragmentArg = new Bundle();
                Bundle settingsProfileFragmentArg = new Bundle();
                Bundle settingsLookingForFragmentArg = new Bundle();
                SettingsIntrestingFragment settingsIntrestingFragment = new SettingsIntrestingFragment();
                SettingsProfileFragment settingsProfileFragment  = new SettingsProfileFragment();
                SettingsLookingForFragment settingsLookingForFragment = new SettingsLookingForFragment();

                //формируем аргументы для фрагмента "Вы понравились"
                settingsIntrestingFragmentArg.putParcelable("User", user);
                settingsIntrestingFragment.setArguments(settingsIntrestingFragmentArg);

                //формируем аргументы для фрагмента "Вам понравились"
                settingsProfileFragmentArg.putParcelable("User", user);
                settingsProfileFragment.setArguments(settingsProfileFragmentArg);

                //формируем аргументы для фрагмента "Взаимные симпатии"
                settingsLookingForFragmentArg.putParcelable("User", user);
                settingsLookingForFragment.setArguments(settingsLookingForFragmentArg);

                // Добавляем фраменты на страницу
                SettingsAdapter adapter = new SettingsAdapter(getSupportFragmentManager());
                ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerSettings);
                if(!settingsProfileFragment.isAdded())
                    adapter.addFragment(settingsProfileFragment, "Профиль");
                if(!settingsIntrestingFragment.isAdded())
                    adapter.addFragment(settingsIntrestingFragment, "Интересы");
                if(!settingsLookingForFragment.isAdded())
                    adapter.addFragment(settingsLookingForFragment, "Ищу");
                viewPager.setAdapter(adapter);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayoutSettings);
                tabLayout.setupWithViewPager(viewPager);

                toolbar.setTitle("Профиль");
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(final int position) {
                        //Заполняем тулбар при смене вкладыки
                        if (position == 0)toolbar.setTitle("Профиль");
                        if (position == 1)toolbar.setTitle("Интересы");
                        if (position == 2)toolbar.setTitle("Ищу");
                    }

                    @Override
                    public void onPageScrolled(int position, float positionOffset,
                                               int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
                progressBarSettings.setVisibility(View.GONE);
            }

            @Override
            public void onError(int error_code, String error_msg) {
                Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onInternetError() {
                Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
//        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        finish();
    }
}


