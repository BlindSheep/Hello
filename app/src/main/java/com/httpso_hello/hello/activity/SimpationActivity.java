package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.ForUserOnly;
import com.httpso_hello.hello.activity.Super.SocketActivity;
import com.httpso_hello.hello.adapters.SimpationAdapter;
import com.httpso_hello.hello.fragments.SimpationFragment;
import com.httpso_hello.hello.helper.Simpation;

public class SimpationActivity extends SocketActivity {

    private ProgressBar progressBarSimpation;
    private Bundle simpationYouFragmentArg;
    private Bundle simpationYourFragmentArg;
    private Bundle simpationVzaimnoFragmentArg;
    private SimpationFragment simpationYouFragment;
    private SimpationFragment simpationYourFragment;
    private SimpationFragment simpationVzaimnoFragment;
    private SimpationAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpation);
        setHeader();
        setMenuItem("SimpationActivity");
        simpationYouFragmentArg = new Bundle();
        simpationYourFragmentArg = new Bundle();
        simpationVzaimnoFragmentArg = new Bundle();
        simpationYouFragment = new SimpationFragment();
        simpationYourFragment  = new SimpationFragment();
        simpationVzaimnoFragment = new SimpationFragment();
        adapter = new SimpationAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpagerSimpation);

        progressBarSimpation = (ProgressBar) findViewById(R.id.progressBarSimpation);
        getSimpations();
    }

    private void getSimpations () {
        Simpation.getInstance(getApplicationContext()).getInfo("ingoing", SimpationActivity.this, new Simpation.GetSimpationCallback() {
            @Override
            public void onSuccess(ForUserOnly[] vz, Activity activity) {
                //формируем аргументы для фрагмента "Вы понравились"
                simpationYouFragmentArg.putSerializable("flirtikArray", vz);
                simpationYouFragment.setArguments(simpationYouFragmentArg);
                adapter.addFragment(simpationYouFragment, "Вы (" + Integer.toString(vz.length) + ")");
                Simpation.getInstance(getApplicationContext()).getInfo("outgoing", SimpationActivity.this, new Simpation.GetSimpationCallback() {
                    @Override
                    public void onSuccess(ForUserOnly[] vz, Activity activity) {
                        //формируем аргументы для фрагмента "Вам понравились"
                        simpationYourFragmentArg.putSerializable("flirtikArray", vz);
                        simpationYourFragment.setArguments(simpationYourFragmentArg);
                        adapter.addFragment(simpationYourFragment, "Вам (" + Integer.toString(vz.length) + ")");
                        Simpation.getInstance(getApplicationContext()).getInfo("mutually", SimpationActivity.this, new Simpation.GetSimpationCallback() {
                            @Override
                            public void onSuccess(ForUserOnly[] vz, Activity activity) {
                                //формируем аргументы для фрагмента "Взаимные симпатии"
                                simpationVzaimnoFragmentArg.putSerializable("flirtikArray", vz);
                                simpationVzaimnoFragment.setArguments(simpationVzaimnoFragmentArg);
                                adapter.addFragment(simpationVzaimnoFragment, "Взаимно (" + Integer.toString(vz.length) + ")");
                                // После выполнения всех запросов, создаем страницу
                                createPage();
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override public void run() {
                                        getSimpations();
                                    }
                                }, 5000);
                            }

                            @Override
                            public void onInternetError() {
                                new Handler().postDelayed(new Runnable() {
                                    @Override public void run() {
                                        getSimpations();
                                    }
                                }, 5000);
                            }
                        });
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getSimpations();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onInternetError() {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getSimpations();
                            }
                        }, 5000);
                    }
                });
            }

            @Override
            public void onError(int error_code, String error_msg) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getSimpations();
                    }
                }, 5000);
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getSimpations();
                    }
                }, 5000);
            }
        });
    }

    private void createPage() {
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayoutSimpation);
        tabLayout.setupWithViewPager(viewPager);
        progressBarSimpation.setVisibility(View.GONE);
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
    }
}
