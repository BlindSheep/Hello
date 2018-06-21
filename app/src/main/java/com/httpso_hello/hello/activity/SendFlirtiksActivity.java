package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.adapters.CardsAdapter;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.HackyViewPager;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.ZoomOutPageTransformer;

import java.util.ArrayList;

public class SendFlirtiksActivity extends SuperMainActivity {

    private Profile profile;
    private ImageView cancel;
    private ImageView like;
    private HackyViewPager mViewPager;
    private CardsAdapter mTextPagerAdapter;
    private int gender_str;
    private int ageFrom;
    private int ageTo;
    private int page = 1;
    private ArrayList<User> userList;
    private Animation anim;
    private TextView textName;
    private ImageView back;
    private ImageView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_flirtiks);
        setHeader();

        cancel = (ImageView) findViewById(R.id.cancel);
        like = (ImageView) findViewById(R.id.like);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_for_button);
        textName = (TextView) findViewById(R.id.textName);
        back = (ImageView) findViewById(R.id.back);
        settings = (ImageView) findViewById(R.id.settings);

        //Если юзер не разу не фильтровал, произвести валидацию
        if (stgs.getSettingInt("ageFrom") == 0) {
            stgs.setSettingInt("ageFrom", 18);
        }
        if (stgs.getSettingInt("ageTo") == 0) {
            stgs.setSettingInt("ageTo", 35);
        }
        if (stgs.getSettingInt("gender") == 0) {
            stgs.setSettingInt("gender", 0);
        }

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendFlirtiksActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getFlirtiks() {
        this.gender_str = stgs.getSettingInt("gender");
        this.ageFrom = stgs.getSettingInt("ageFrom");
        this.ageTo = stgs.getSettingInt("ageTo");
        page = 1;
        userList = new ArrayList<User>();

        profile = new Profile(getApplicationContext());
        this.profile.searchProfiles(
                ageFrom,
                ageTo,
                gender_str,
                page,
                0,
                new Profile.SearchProfilesCallback() {
                    @Override
                    public void onSuccess(final User[] users) {
                        for (int i = 0; i < users.length; i++) {
                            if (users[i].avatar != null) userList.add(users[i]);
                        }
                        page = page + 1;

                        if(users[0].birthDate != null) {
                            textName.setText(users[0].nickname + ", " + ConverterDate.convertDateToAge(users[0].birthDate));
                        } else textName.setText(users[0].nickname);

                        ArrayList<String> defolt = new ArrayList<>();
                        for (int i = 0; i < users.length; i++) {
                            if (users[i].avatar != null) defolt.add(Constant.upload + users[i].avatar.normal_form_original);
                        }
                        // Запихиваем в адаптер нужные значения
                        mTextPagerAdapter = new CardsAdapter(getSupportFragmentManager(), defolt);

                        //Вставляем фрагмент на страницу
                        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                        mViewPager.setAdapter(mTextPagerAdapter);
                        mViewPager.setLocked(true);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancel.startAnimation(anim);
                                mViewPager.setLocked(false);
                                mViewPager.setCurrentItem(1);
                            }
                        });

                        like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                like.startAnimation(anim);
                                mViewPager.setLocked(false);
                                mViewPager.setCurrentItem(1);
                            }
                        });

                        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            }

                            @Override
                            public void onPageSelected(final int position) {
                                if ((mTextPagerAdapter.getCount() - 3) == position)  getNew();

                                if (position != 0) {
                                    back.setVisibility(View.VISIBLE);
                                    back.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mViewPager.setLocked(false);
                                            mViewPager.setCurrentItem(position - 1);
                                        }
                                    });
                                } else {
                                    back.setVisibility(View.GONE);
                                }

                                if(userList.get(position).birthDate != null) textName.setText(userList.get(position).nickname + ", " + ConverterDate.convertDateToAge(userList.get(position).birthDate));
                                else textName.setText(userList.get(position).nickname);

                                mViewPager.setLocked(true);
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cancel.startAnimation(anim);
                                        mViewPager.setLocked(false);
                                        mViewPager.setCurrentItem(position + 1);
                                    }
                                });

                                like.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        like.startAnimation(anim);
                                        mViewPager.setLocked(false);
                                        mViewPager.setCurrentItem(position + 1);
                                    }
                                });
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }
                        });
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getFlirtiks();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onInternetError() {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getFlirtiks();
                            }
                        }, 5000);
                    }
                }
        );
    }

    private void getNew() {
        profile = new Profile(getApplicationContext());
        this.profile.searchProfiles(
                ageFrom,
                ageTo,
                gender_str,
                page,
                0,
                new Profile.SearchProfilesCallback() {
                    @Override
                    public void onSuccess(User[] users) {
                        for (int i = 0; i < users.length; i++) {
                            if (users[i].avatar != null) userList.add(users[i]);
                        }
                        page = page + 1;
                        ArrayList<String> defolt = new ArrayList<>();
                        for (int i = 0; i < users.length; i++) {
                            if (users[i].avatar != null) defolt.add(Constant.upload + users[i].avatar.normal_form_original);
                        }
                        // Запихиваем в адаптер нужные значения
                        mTextPagerAdapter.addAll(defolt);
                        mTextPagerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getFlirtiks();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onInternetError() {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getFlirtiks();
                            }
                        }, 5000);
                    }
                }
        );
    }

    @Override
    public void onResume() {
        page = 1;
        getFlirtiks();
        super.onResume();
    }
}
