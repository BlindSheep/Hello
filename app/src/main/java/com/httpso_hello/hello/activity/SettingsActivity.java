package com.httpso_hello.hello.activity;

import android.app.Activity;
//import android.app.Fragment;
import android.os.Handler;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.view.GravityCompat;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.NewProfile;
import com.httpso_hello.hello.activity.Super.SocketActivity;
import com.httpso_hello.hello.adapters.SettingsAdapter;
import com.httpso_hello.hello.fragments.SettingsIntrestingFragment;
import com.httpso_hello.hello.fragments.SettingsLookingForFragment;
import com.httpso_hello.hello.fragments.SettingsProfileFragment;
import com.httpso_hello.hello.helper.Auth;
import com.httpso_hello.hello.helper.Profile;

public class SettingsActivity extends SocketActivity {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setHeader();
        setMenuItem("SettingsActivity");

        auth = new Auth(getApplicationContext());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        setFragments();
    }

    public void setFragments() {
        //Если юзер зашел первый раз
        if (auth.firstLogin()){
            drawer.openDrawer(GravityCompat.START , true);
            stgs.setSettingInt("firstLogin", 1);
        }

        swipeRefreshLayout.setRefreshing(true);
        Profile profiles = new Profile(getApplicationContext());
        profiles.getProfile(stgs.getSettingInt("userId"), this, new Profile.GetProfileCallback() {
            @Override
            public void onSuccess(final NewProfile user, Activity activity) {
                //содаем фрагменты и аргументы
                Bundle settingsIntrestingFragmentArg = new Bundle();
                Bundle settingsProfileFragmentArg = new Bundle();
                Bundle settingsLookingForFragmentArg = new Bundle();
                SettingsIntrestingFragment settingsIntrestingFragment = new SettingsIntrestingFragment();
                SettingsProfileFragment settingsProfileFragment  = new SettingsProfileFragment();
                SettingsLookingForFragment settingsLookingForFragment = new SettingsLookingForFragment();

                //фрагмент интересы
                settingsIntrestingFragmentArg.putParcelable("User", user.info);
                settingsIntrestingFragment.setArguments(settingsIntrestingFragmentArg);

                //фрагмент редактировать профиль
                settingsProfileFragmentArg.putParcelable("User", user.info);
                settingsProfileFragment.setArguments(settingsProfileFragmentArg);

                //фрагмент кого ищу
                settingsLookingForFragmentArg.putParcelable("User", user.info);
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
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
            }

            @Override
            public void onError(int error_code, String error_msg) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        setFragments();
                    }
                }, 5000);
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        setFragments();
                    }
                }, 5000);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


