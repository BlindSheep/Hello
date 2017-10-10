package com.httpso_hello.hello.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.adapters.FragmentPhotoAdapter;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.HackyViewPager;
import com.httpso_hello.hello.helper.Like;
import com.httpso_hello.hello.helper.ZoomOutPageTransformer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenPhotoActivity extends AppCompatActivity {

    private ArrayList<String> photoOrig;
    private ArrayList<Integer> photoLike;
    private ArrayList<Integer> photoId;
    private ArrayList<String> photoIsVoted;
    private boolean likeble;
    private int posit;
    private static final String ISLOCKED_ARG = "isLocked";
    private ActionBar toolbar;
    private TextView like_string;
    private ImageButton like_button;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private ViewPager mViewPager;
    private ImageView mContentView;
    private TextView likeString;
    private ImageView likeButton;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        setContentView(R.layout.activity_fullscreen_photo);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        toolbar = getSupportActionBar();
        like_string = (TextView) findViewById(R.id.like_string);
        like_button = (ImageButton) findViewById(R.id.like_button);

        //Обязательные параметры для передачи (оригиналы фоток и можно ли их лайкать) (кол-во лайков и ID фоток - опционально)
        likeble = extras.getBoolean("likeble"); //Обязательно передать, можно лайкать фотки или нельзя!
        photoOrig = extras.getStringArrayList("photoOrig");
        posit = extras.getInt("position");
        if (likeble) {
            photoLike = extras.getIntegerArrayList("photoLike");
            photoId = extras.getIntegerArrayList("photoId");
            photoIsVoted = extras.getStringArrayList("photoIsVoted");
        }

        //Скрываем нижнюю панель если нельзя лайкать
        if (likeble) mControlsView.setVisibility(View.VISIBLE);
        else mControlsView.setVisibility(View.GONE);

        //Заполняем тулбар
        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setHomeButtonEnabled(true);
        toolbar.setHomeAsUpIndicator(R.mipmap.ic_action_back);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Фотография");
        toolbar.setSubtitle(posit+1 + " из " + photoOrig.size());

        //Заполняем лайки
        if (likeble) {
            if (photoLike.get(posit) != 0) {
                like_string.setText(ConverterDate.likeStr(photoLike.get(posit)));
            } else like_string.setText("Нет оценок");
        }

        // Запихиваем в адаптер нужные значения
        FragmentPhotoAdapter mTextPagerAdapter = new FragmentPhotoAdapter(getSupportFragmentManager(), photoOrig);

        //Вставляем фрагмент на страницу
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setAdapter(mTextPagerAdapter);
        mViewPager.setCurrentItem(posit);

        //Прослушивание и Открытие активности с лайками
        if (likeble) {
            like_string.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (photoLike.get(posit) != 0) {
                        Intent intent = new Intent(FullscreenPhotoActivity.this, LikeActivity.class);
                        intent.putExtra("target_id", photoId.get(posit));
                        intent.putExtra("subject", "photo");
                        intent.putExtra("target_controller", "photos");
                        startActivity(intent);
                    } else
                        Toast.makeText(getApplicationContext(), "Нет оценок", Toast.LENGTH_LONG).show();
                }
            });
        }

        //Цвет кнопки лайка
        if (likeble) {
            if (!(photoIsVoted.get(posit).equals("true")))
                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
            else
                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
        }

        //Нажатие кнопки лайк
        if (likeble) {
            like_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(photoIsVoted.get(posit).equals("true"))) {
                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                        photoIsVoted.set(posit, "true");
                        photoLike.set(posit, (photoLike.get(posit) + 1));
                        like_string.setText(ConverterDate.likeStr(photoLike.get(posit)));
                        Like.getInstance(getApplicationContext()).sendLike(photoId.get(posit),"up", "photo", "photos", new Like.SendLikeCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                photoIsVoted.set(posit, "false");
                                photoLike.set(posit, (photoLike.get(posit) - 1));
                                like_string.setText(ConverterDate.likeStr(photoLike.get(posit)));
                                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onInternetError() {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                photoIsVoted.set(posit, "false");
                                photoLike.set(posit, (photoLike.get(posit) - 1));
                                like_string.setText(ConverterDate.likeStr(photoLike.get(posit)));
                                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                        photoIsVoted.set(posit, "false");
                        photoLike.set(posit, (photoLike.get(posit) - 1));
                        like_string.setText(ConverterDate.likeStr(photoLike.get(posit)));
                        Like.getInstance(getApplicationContext()).sendLike(photoId.get(posit), "down", "photo", "photos", new Like.SendLikeCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                photoIsVoted.set(posit, "true");
                                photoLike.set(posit, (photoLike.get(posit) + 1));
                                like_string.setText(ConverterDate.likeStr(photoLike.get(posit)));
                                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onInternetError() {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                photoIsVoted.set(posit, "true");
                                photoLike.set(posit, (photoLike.get(posit) + 1));
                                like_string.setText(ConverterDate.likeStr(photoLike.get(posit)));
                                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(final int position) {
                //Заполняем тулбар и лайки при смене фоток
                toolbar.setTitle("Фотография");
                toolbar.setSubtitle(position+1 + " из " + photoOrig.size());

                if (likeble) {
                    if (photoLike.get(position) != 0) {
                        like_string.setText(ConverterDate.likeStr(photoLike.get(position)));
                    } else like_string.setText("Нет оценок");
                }

                //Цвет кнопки лайка
                if (likeble) {
                    if (!(photoIsVoted.get(position).equals("true")))
                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                    else
                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                }

                //Нажатие кнопки лайк
                if (likeble) {
                    like_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!(photoIsVoted.get(position).equals("true"))) {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                photoIsVoted.set(position, "true");
                                photoLike.set(position, (photoLike.get(position) + 1));
                                like_string.setText(ConverterDate.likeStr(photoLike.get(position)));
                                Like.getInstance(getApplicationContext()).sendLike(photoId.get(position), "up", "photo", "photos", new Like.SendLikeCallback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(int error_code, String error_msg) {
                                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                        photoIsVoted.set(position, "false");
                                        photoLike.set(position, (photoLike.get(position) - 1));
                                        like_string.setText(ConverterDate.likeStr(photoLike.get(position)));
                                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onInternetError() {
                                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                        photoIsVoted.set(position, "false");
                                        photoLike.set(position, (photoLike.get(position) - 1));
                                        like_string.setText(ConverterDate.likeStr(photoLike.get(position)));
                                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                photoIsVoted.set(position, "false");
                                photoLike.set(position, (photoLike.get(position) - 1));
                                like_string.setText(ConverterDate.likeStr(photoLike.get(position)));
                                Like.getInstance(getApplicationContext()).sendLike(photoId.get(position), "down", "photo", "photos", new Like.SendLikeCallback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(int error_code, String error_msg) {
                                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                        photoIsVoted.set(position, "true");
                                        photoLike.set(position, (photoLike.get(position) + 1));
                                        like_string.setText(ConverterDate.likeStr(photoLike.get(position)));
                                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onInternetError() {
                                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                        photoIsVoted.set(position, "true");
                                        photoLike.set(position, (photoLike.get(position) + 1));
                                        like_string.setText(ConverterDate.likeStr(photoLike.get(position)));
                                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });
                }

                //Открытие активности с теми кто лайкнул
                if (likeble) {
                    like_string.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (photoLike.get(position) != 0) {
                                Intent intent = new Intent(FullscreenPhotoActivity.this, LikeActivity.class);
                                intent.putExtra("target_id", photoId.get(position));
                                intent.putExtra("subject", "photo");
                                intent.putExtra("target_controller", "photos");
                                startActivity(intent);
                            } else
                                Toast.makeText(getApplicationContext(), "Нет оценок", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Set up the user interaction to manually show or hide the system UI.
        mViewPager.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }
}
