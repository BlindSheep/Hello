package com.httpso_hello.hello.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.adapters.FragmentPhotoAdapter;
import com.httpso_hello.hello.helper.*;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenPhotoActivityUser extends AppCompatActivity {

    private ArrayList<String> photoOrig;
    private ArrayList<Integer> photoLike;
    private ArrayList<Integer> photoId;
    private ArrayList<String> photoIsVoted;
    private ArrayList<Integer> photoComments;
    private boolean likeble;
    private int posit;
    private static final String ISLOCKED_ARG = "isLocked";
    private ActionBar toolbar;
    private TextView like_string;
    private LinearLayout likeBlock;
    private ImageView like_button;
    private LinearLayout commentsBlock;
    private TextView comments_string;

    public static int photoID;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fullscreen_photo_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_delete:
//Сюда код удаления фотки
                int photoId = getPhotoId(); // - метод получения ID фото, которая видна на экране
                Photo.getInstance(getApplicationContext()).deletePhoto(
                        photoId,
                        new Photo.DeletePhotoCalback() {
                            @Override
                            public void onSuccess() {

                            }
                        },
                        new Help.ErrorCallback() {
                            @Override
                            public void onError(int error_code, String error_msg) {

                            }

                            @Override
                            public void onInternetError() {

                            }
                        }
                );
                finish();
                return true;

            default:return super.onOptionsItemSelected(item);
        }
    }

    private int getPhotoId(){
        return photoID;
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
        likeBlock = (LinearLayout) findViewById(R.id.likeBlock);
        like_button = (ImageView) findViewById(R.id.like_button);
        commentsBlock = (LinearLayout) findViewById(R.id.commentsBlock);
        comments_string = (TextView) findViewById(R.id.comments_string);

        photoID = 0;

//Обязательные параметры для передачи (оригиналы фоток и можно ли их лайкать) (кол-во лайков и ID фоток - опционально)
        likeble = extras.getBoolean("likeble"); //Обязательно передать, можно лайкать фотки или нельзя!
        photoOrig = extras.getStringArrayList("photoOrig");
        posit = extras.getInt("position");
        if (likeble) {
            photoLike = extras.getIntegerArrayList("photoLike");
            photoId = extras.getIntegerArrayList("photoId");
            photoIsVoted = extras.getStringArrayList("photoIsVoted");
            photoComments = extras.getIntegerArrayList("photoComments");
        }

        //ID фотки
        this.photoID = photoId.get(posit);

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

        if (likeble) {
            //Нижняя панель
            mControlsView.setVisibility(View.VISIBLE);
            //Заполняем лайки
            like_string.setText(Integer.toString(photoLike.get(posit)));
            //Заполняем комменты
            comments_string.setText(Integer.toString(photoComments.get(posit)));
            //Цвет кнопки лайка
            if (!(photoIsVoted.get(posit).equals("true"))) {
                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                like_string.setTextColor(getResources().getColor(R.color.main_white_color_hello));
            } else {
                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                like_string.setTextColor(getResources().getColor(R.color.main_blue_color_hello));
            }
            //Нажатие кнопки лайк
            likeBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(photoIsVoted.get(posit).equals("true"))) {
                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                        like_string.setTextColor(getResources().getColor(R.color.main_blue_color_hello));
                        photoIsVoted.set(posit, "true");
                        photoLike.set(posit, (photoLike.get(posit) + 1));
                        like_string.setText(Integer.toString(photoLike.get(posit)));
                        Like.getInstance(getApplicationContext()).sendLike(photoId.get(posit),"up", "photo", "photos", new Like.SendLikeCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                like_string.setTextColor(getResources().getColor(R.color.main_dark_grey_color_hello));
                                photoIsVoted.set(posit, "false");
                                photoLike.set(posit, (photoLike.get(posit) - 1));
                                like_string.setText(Integer.toString(photoLike.get(posit)));
                                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onInternetError() {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                like_string.setTextColor(getResources().getColor(R.color.main_dark_grey_color_hello));
                                photoIsVoted.set(posit, "false");
                                photoLike.set(posit, (photoLike.get(posit) - 1));
                                like_string.setText(Integer.toString(photoLike.get(posit)));
                                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                        like_string.setTextColor(getResources().getColor(R.color.main_dark_grey_color_hello));
                        photoIsVoted.set(posit, "false");
                        photoLike.set(posit, (photoLike.get(posit) - 1));
                        like_string.setText(Integer.toString(photoLike.get(posit)));
                        Like.getInstance(getApplicationContext()).sendLike(photoId.get(posit), "down", "photo", "photos", new Like.SendLikeCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                like_string.setTextColor(getResources().getColor(R.color.main_blue_color_hello));
                                photoIsVoted.set(posit, "true");
                                photoLike.set(posit, (photoLike.get(posit) + 1));
                                like_string.setText(Integer.toString(photoLike.get(posit)));
                                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onInternetError() {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                like_string.setTextColor(getResources().getColor(R.color.main_blue_color_hello));
                                photoIsVoted.set(posit, "true");
                                photoLike.set(posit, (photoLike.get(posit) + 1));
                                like_string.setText(Integer.toString(photoLike.get(posit)));
                                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
            //Нажатие кнопки комменты
            commentsBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PhotoCommentsActivity.class);
                    intent.putExtra("id", photoId.get(posit));
                    intent.putExtra("photo", photoOrig.get(posit));
                    intent.putExtra("likes", photoLike.get(posit));
                    startActivity(intent);
                }
            });
        } else {
            mControlsView.setVisibility(View.GONE);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(final int position) {
                //ID фотки
                photoID = photoId.get(position);

                //Заполняем тулбар и лайки при смене фоток
                toolbar.setTitle("Фотография");
                toolbar.setSubtitle(position+1 + " из " + photoOrig.size());

                if (likeble) {
                    //Кол-во лайков
                    like_string.setText(Integer.toString(photoLike.get(position)));
                    //Кол-во комментов
                    comments_string.setText(Integer.toString(photoComments.get(position)));
                    //Цвет кнопки лайка
                    if (!(photoIsVoted.get(position).equals("true"))) {
                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                        like_string.setTextColor(getResources().getColor(R.color.main_white_color_hello));
                    }
                    else {
                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                        like_string.setTextColor(getResources().getColor(R.color.main_blue_color_hello));
                    }
                    //Нажатие кнопки лайк
                    likeBlock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!(photoIsVoted.get(position).equals("true"))) {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                like_string.setTextColor(getResources().getColor(R.color.main_blue_color_hello));
                                photoIsVoted.set(position, "true");
                                photoLike.set(position, (photoLike.get(position) + 1));
                                like_string.setText(Integer.toString(photoLike.get(position)));
                                Like.getInstance(getApplicationContext()).sendLike(photoId.get(position), "up", "photo", "photos", new Like.SendLikeCallback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(int error_code, String error_msg) {
                                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                        like_string.setTextColor(getResources().getColor(R.color.main_white_color_hello));
                                        photoIsVoted.set(position, "false");
                                        photoLike.set(position, (photoLike.get(position) - 1));
                                        like_string.setText(Integer.toString(photoLike.get(position)));
                                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onInternetError() {
                                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                        like_string.setTextColor(getResources().getColor(R.color.main_white_color_hello));
                                        photoIsVoted.set(position, "false");
                                        photoLike.set(position, (photoLike.get(position) - 1));
                                        like_string.setText(Integer.toString(photoLike.get(position)));
                                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                like_string.setTextColor(getResources().getColor(R.color.main_white_color_hello));
                                photoIsVoted.set(position, "false");
                                photoLike.set(position, (photoLike.get(position) - 1));
                                like_string.setText(Integer.toString(photoLike.get(position)));
                                Like.getInstance(getApplicationContext()).sendLike(photoId.get(position), "down", "photo", "photos", new Like.SendLikeCallback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(int error_code, String error_msg) {
                                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                        like_string.setTextColor(getResources().getColor(R.color.main_blue_color_hello));
                                        photoIsVoted.set(position, "true");
                                        photoLike.set(position, (photoLike.get(position) + 1));
                                        like_string.setText(Integer.toString(photoLike.get(position)));
                                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onInternetError() {
                                        like_button.getBackground().setColorFilter(getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                        like_string.setTextColor(getResources().getColor(R.color.main_blue_color_hello));
                                        photoIsVoted.set(position, "true");
                                        photoLike.set(position, (photoLike.get(position) + 1));
                                        like_string.setText(Integer.toString(photoLike.get(position)));
                                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });
                    //Нажатие кнопки комменты
                    commentsBlock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), PhotoCommentsActivity.class);
                            intent.putExtra("id", photoId.get(position));
                            intent.putExtra("photo", photoOrig.get(position));
                            intent.putExtra("likes", photoLike.get(position));
                            startActivity(intent);
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