package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.FlirtikItem;
import com.httpso_hello.hello.adapters.FriendsAdapter;
import com.httpso_hello.hello.adapters.SimpationAdapter;
import com.httpso_hello.hello.fragments.SimpationFragment;
import com.httpso_hello.hello.helper.Auth;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Settings;
import com.httpso_hello.hello.helper.Simpation;
import com.squareup.picasso.Picasso;

public class SimpationActivity extends SuperMainActivity{

    private ProgressBar progressBarSimpation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpation);
        setHeader();
        setMenuItem("SimpationActivity");

        progressBarSimpation = (ProgressBar) findViewById(R.id.progressBarSimpation);

        Simpation.getInstance(getApplicationContext()).getInfo(this, new Simpation.GetSimpationCallback() {
            @Override
            public void onSuccess(FlirtikItem[] vz, FlirtikItem[] vam, FlirtikItem[] vi, Activity activity) {
                //содаем фрагменты и аргументы
                Bundle simpationYouFragmentArg = new Bundle();
                Bundle simpationYourFragmentArg = new Bundle();
                Bundle simpationVzaimnoFragmentArg = new Bundle();
                SimpationFragment simpationYouFragment = new SimpationFragment();
                SimpationFragment simpationYourFragment  = new SimpationFragment();
                SimpationFragment simpationVzaimnoFragment = new SimpationFragment();

                //формируем аргументы для фрагмента "Вы понравились"
                simpationYouFragmentArg.putSerializable("flirtikArray", vi);
                simpationYouFragment.setArguments(simpationYouFragmentArg);

                //формируем аргументы для фрагмента "Вам понравились"
                simpationYourFragmentArg.putSerializable("flirtikArray", vam);
                simpationYourFragment.setArguments(simpationYourFragmentArg);

                //формируем аргументы для фрагмента "Взаимные симпатии"
                simpationVzaimnoFragmentArg.putSerializable("flirtikArray", vz);
                simpationVzaimnoFragment.setArguments(simpationVzaimnoFragmentArg);

                // Добавляем фраменты на страницу
                SimpationAdapter adapter = new SimpationAdapter(getSupportFragmentManager());
                ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerSimpation);
                adapter.addFragment(simpationYouFragment, "Вы (" + Integer.toString(vi.length) + ")");
                adapter.addFragment(simpationYourFragment, "Вам (" + Integer.toString(vam.length) + ")");
                adapter.addFragment(simpationVzaimnoFragment, "Взаимно (" + Integer.toString(vz.length) + ")");
                viewPager.setAdapter(adapter);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayoutSimpation);
                tabLayout.setupWithViewPager(viewPager);

                toolbar.setTitle("Вы понравились");
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(final int position) {
                        //Заполняем тулбар при смене вкладыки
                        if (position == 0)toolbar.setTitle("Вы понравились");
                        if (position == 1)toolbar.setTitle("Вам понравились");
                        if (position == 2)toolbar.setTitle("Взаимные симпатии");
                    }

                    @Override
                    public void onPageScrolled(int position, float positionOffset,
                                               int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });

                progressBarSimpation.setVisibility(View.GONE);
            }

            @Override
            public void onError(int error_code, String error_msg) {

            }

            @Override
            public void onInternetError() {

            }
        });
    }
}
