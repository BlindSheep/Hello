package com.httpso_hello.hello.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.adapters.PhotosUserAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Friend;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.Photo;
import com.httpso_hello.hello.helper.Simpation;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import static android.R.style.Animation_Dialog;
import static com.httpso_hello.hello.helper.ConverterDate.convertDateForEnter;
import static com.httpso_hello.hello.helper.ConverterDate.convertDateToAge;

public class ProfileActivity extends SuperMainActivity{

    private ImageView avatar;
    private int profile_id;
    private String profile_nickname;
    private String profile_avatar;
    private PhotosUserAdapter plAdapter;
    private LinearLayout friendsCountButton;
    private ProgressBar progressBarProfile;
    private ProgressBar progressBarFlirtik;
    private boolean isUserProfile;
    private FloatingActionButton fab;
    private LinearLayout newPhotoButton;
    private TextView nameToolbar;
    private TextView onlineToolbar;
    private LinearLayout profile_content;
    private AppBarLayout profile_content2;
    private TextView phoneNumProfile;
    private LinearLayout phoneNumProfileConteiner;
    private TextView celZnakomstvaProfile0;
    private TextView celZnakomstvaProfile1;
    private TextView celZnakomstvaProfile2;
    private TextView celZnakomstvaProfile3;
    private TextView celZnakomstvaProfile4;
    private TextView celZnakomstvaProfile5;
    private TextView celZnakomstvaProfile6;
    private TextView celZnakomstvaProfile7;
    private TextView celZnakomstvaProfile8;
    private TextView celZnakomstvaProfile9;
    private TextView celZnakomstvaProfile10;
    private TextView celZnakomstvaProfile11;
    private TextView ageLookinFor;
    private GridView GV;
    private LinearLayout ll;
    private TextView photo_count;
    private TextView friendsCount;
    private TextView cityCache;
    private TextView looking_for;
    private LinearLayout lookinForBar;
    private LinearLayout celZnakomstvaProfileLL;
    private RelativeLayout msgAndFrndBtn;
    private TextView skypeProfile;
    private LinearLayout skypeProfileConteiner;
    private TextView spProfile;
    private LinearLayout spProfileContener;
    private LinearLayout anketaBar;
    private TextView autoProfile;
    private LinearLayout autoProfileContener;
    private TextView dohodProfile;
    private LinearLayout dohodProfileContener;
    private TextView musicProfile;
    private LinearLayout musicProfileContener;
    private TextView moviesProfile;
    private LinearLayout moviesProfileContener;
    private TextView booksProfile;
    private LinearLayout booksProfileContener;
    private TextView gamesProfile;
    private LinearLayout gamesProfileContener;
    private TextView intrestingProfile;
    private LinearLayout intrestingProfileContener;
    private LinearLayout rostAndVesProfileContener;
    private TextView rostTitleProfile;
    private TextView rostProfile;
    private TextView vesTitleProfile;
    private TextView vesProfile;
    private View findView;
    private ImageView friend1;
    private ImageView friend2;
    private ImageView friend3;
    private ImageView friend4;
    private ImageView friend5;
    private ImageView friend6;
    private ImageView friend7;
    private ImageView friend8;
    private ImageView friend9;
    private LinearLayout friendsBlock;
    private LinearLayout friendsLayout1;
    private LinearLayout friendsLayout2;
    private LinearLayout friendsLayout3;
    private TextView giftsCount;
    private ImageView gift1;
    private ImageView gift2;
    private ImageView gift3;
    private ImageView gift4;
    private ImageView gift5;
    private ImageView gift6;
    private ImageView gift7;
    private ImageView gift8;
    private ImageView gift9;
    private LinearLayout giftsBlock;
    private LinearLayout giftsLayout1;
    private LinearLayout giftsLayout2;
    private LinearLayout giftsLayout3;
    private LinearLayout friendsAndGiftsBlock;
    private PopupWindow popUpWindow;
    private View popupView;
    private PopupWindow popUpWindow2;
    private View popupView2;
    private PopupWindow popUpWindow3;
    private View popupView3;
    private Uri imageUri;
    String mCurrentPhotoPath;
    File photoFile;
    private int width;
    private Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();

        profile_id = extras.getInt("profile_id");
        profile_nickname = extras.getString("profile_nickname");
        profile_avatar = extras.getString("avatar");
        avatar = (ImageView) findViewById(R.id.avatar);
        progressBarProfile = (ProgressBar) findViewById(R.id.progressBarProfile);
        progressBarFlirtik = (ProgressBar) findViewById(R.id.progressBarFlirtik);
        newPhotoButton = ((LinearLayout) findViewById(R.id.new_photo_button));
        nameToolbar = (TextView) findViewById(R.id.nameToolbar);
        onlineToolbar = (TextView) findViewById(R.id.onlineToolbar);
        profile_content = (LinearLayout) findViewById(R.id.profile_content);
        profile_content2 = (AppBarLayout) findViewById(R.id.profile_content2);
        phoneNumProfile = (TextView) findViewById(R.id.phoneNumProfile);
        phoneNumProfileConteiner = (LinearLayout) findViewById(R.id.phoneNumProfileConteiner);
        celZnakomstvaProfile0 = (TextView) findViewById(R.id.celZnakomstvaProfile0);
        celZnakomstvaProfile1 = (TextView) findViewById(R.id.celZnakomstvaProfile1);
        celZnakomstvaProfile2 = (TextView) findViewById(R.id.celZnakomstvaProfile2);
        celZnakomstvaProfile3 = (TextView) findViewById(R.id.celZnakomstvaProfile3);
        celZnakomstvaProfile4 = (TextView) findViewById(R.id.celZnakomstvaProfile4);
        celZnakomstvaProfile5 = (TextView) findViewById(R.id.celZnakomstvaProfile5);
        celZnakomstvaProfile6 = (TextView) findViewById(R.id.celZnakomstvaProfile6);
        celZnakomstvaProfile7 = (TextView) findViewById(R.id.celZnakomstvaProfile7);
        celZnakomstvaProfile8 = (TextView) findViewById(R.id.celZnakomstvaProfile8);
        celZnakomstvaProfile9 = (TextView) findViewById(R.id.celZnakomstvaProfile9);
        celZnakomstvaProfile10 = (TextView) findViewById(R.id.celZnakomstvaProfile10);
        celZnakomstvaProfile11 = (TextView) findViewById(R.id.celZnakomstvaProfile11);
        ageLookinFor = (TextView)findViewById(R.id.ageLookinFor);
        GV = (GridView) findViewById(R.id.profile_photos_list);
        ll = (LinearLayout) findViewById(R.id.linearLayoutForPhoto);
        photo_count = (TextView) findViewById(R.id.photo_count);
        cityCache = (TextView) findViewById(R.id.cityCache);
        friendsCount = (TextView) findViewById(R.id.friendsCount);
        looking_for = (TextView)findViewById(R.id.looking_for);
        lookinForBar = (LinearLayout) findViewById(R.id.lookinForBar);
        celZnakomstvaProfileLL = (LinearLayout)findViewById(R.id.celZnakomstvaProfileLL);
        msgAndFrndBtn = (RelativeLayout) findViewById(R.id.msgAndFrndBtn);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        friendsCountButton = (LinearLayout) findViewById(R.id.friendsCountButton);
        skypeProfile = (TextView) findViewById(R.id.skypeProfile);
        skypeProfileConteiner = (LinearLayout) findViewById(R.id.skypeProfileConteiner);
        spProfile = (TextView) findViewById(R.id.spProfile);
        spProfileContener = (LinearLayout) findViewById(R.id.spProfileContener);
        anketaBar = (LinearLayout) findViewById(R.id.anketaBar);
        autoProfile = (TextView) findViewById(R.id.autoProfile);
        autoProfileContener = (LinearLayout) findViewById(R.id.autoProfileContener);
        dohodProfile = (TextView) findViewById(R.id.dohodProfile);
        dohodProfileContener = (LinearLayout) findViewById(R.id.dohodProfileContener);
        musicProfile = (TextView) findViewById(R.id.musicProfile);
        musicProfileContener = (LinearLayout) findViewById(R.id.musicProfileContener);
        moviesProfile = (TextView) findViewById(R.id.moviesProfile);
        moviesProfileContener = (LinearLayout) findViewById(R.id.moviesProfileContener);
        booksProfile = (TextView) findViewById(R.id.booksProfile);
        booksProfileContener = (LinearLayout) findViewById(R.id.booksProfileContener);
        gamesProfile = (TextView) findViewById(R.id.gamesProfile);
        gamesProfileContener = (LinearLayout) findViewById(R.id.gamesProfileContener);
        intrestingProfile = (TextView) findViewById(R.id.intrestingProfile);
        intrestingProfileContener = (LinearLayout) findViewById(R.id.intrestingProfileContener);
        rostAndVesProfileContener = (LinearLayout) findViewById(R.id.rostAndVesProfileContener);
        rostTitleProfile = (TextView) findViewById(R.id.rostTitleProfile);
        rostProfile = (TextView) findViewById(R.id.rostProfile);
        vesTitleProfile = (TextView) findViewById(R.id.vesTitleProfile);
        vesProfile = (TextView) findViewById(R.id.vesProfile);
        findView = (View) findViewById(R.id.findView);
        friend1 = (ImageView) findViewById(R.id.friend1);
        friend2 = (ImageView) findViewById(R.id.friend2);
        friend3 = (ImageView) findViewById(R.id.friend3);
        friend4 = (ImageView) findViewById(R.id.friend4);
        friend5 = (ImageView) findViewById(R.id.friend5);
        friend6 = (ImageView) findViewById(R.id.friend6);
        friend7 = (ImageView) findViewById(R.id.friend7);
        friend8 = (ImageView) findViewById(R.id.friend8);
        friend9 = (ImageView) findViewById(R.id.friend9);
        friendsBlock = (LinearLayout) findViewById(R.id.friendsBlock);
        friendsLayout1 = (LinearLayout) findViewById(R.id.friendsLayout1);
        friendsLayout2 = (LinearLayout) findViewById(R.id.friendsLayout2);
        friendsLayout3 = (LinearLayout) findViewById(R.id.friendsLayout3);
        giftsCount = (TextView) findViewById(R.id.giftsCount);
        gift1 = (ImageView) findViewById(R.id.gift1);
        gift2 = (ImageView) findViewById(R.id.gift2);
        gift3 = (ImageView) findViewById(R.id.gift3);
        gift4 = (ImageView) findViewById(R.id.gift4);
        gift5 = (ImageView) findViewById(R.id.gift5);
        gift6 = (ImageView) findViewById(R.id.gift6);
        gift7 = (ImageView) findViewById(R.id.gift7);
        gift8 = (ImageView) findViewById(R.id.gift8);
        gift9 = (ImageView) findViewById(R.id.gift9);
        giftsBlock = (LinearLayout) findViewById(R.id.giftsBlock);
        giftsLayout1 = (LinearLayout) findViewById(R.id.giftsLayout1);
        giftsLayout2 = (LinearLayout) findViewById(R.id.giftsLayout2);
        giftsLayout3 = (LinearLayout) findViewById(R.id.giftsLayout3);
        friendsAndGiftsBlock = (LinearLayout) findViewById(R.id.friendsAndGiftsBlock);
        popupView = getLayoutInflater().inflate(R.layout.popup_for_avatar, null);
        popUpWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupView2 = getLayoutInflater().inflate(R.layout.popup_for_new_photo, null);
        popUpWindow2 = new PopupWindow(popupView2, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupView3 = getLayoutInflater().inflate(R.layout.popup_accept_new_photo, null);
        popUpWindow3 = new PopupWindow(popupView3, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setScrimAnimationDuration(1000);
        nameToolbar.setText(extras.getString("profile_nickname"));

        toggle.syncState();

        //Заполнение шапки в меню
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
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
        String id = Integer.toString(stgs.getSettingInt("user_id"));
        user_id_header.setText("Ваш ID " + id);

        //Парсинг юзера
        Profile profile = new Profile(getApplicationContext());
        profile.getProfile(extras.getInt("profile_id"), this, new Profile.GetProfileCallback() {
            @Override
            public void onSuccess(final User user, Activity activity) {
                ///////////////////////////////////////////
                ///Отображение информации о пользователе///
                ///////////////////////////////////////////

                // Является ли открытая анкета, анкетой залогинившегося юзера
                isUserProfile = (stgs.getSettingInt("user_id") == user.id);

                //Устанавливаем имя и возраст в тулбар
                if (user.birth_date != null) {
                    nameToolbar.setText(user.nickname + ", " + convertDateToAge(user.birth_date));
                } else nameToolbar.setText(user.nickname);

                //Устанавливаем дату последнего захода в тулбар
                if (user.is_online) {
                    onlineToolbar.setText("В сети");
                    onlineToolbar.setTextColor(getResources().getColor(R.color.main_green_color_hello));
                } else {
                    onlineToolbar.setText(convertDateForEnter(user.date_log, user.gender));
                    onlineToolbar.setTextColor(getResources().getColor(R.color.main_white_color_hello));
                }

                //Устанавливаем аватар
                DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
                width = displaymetrics.widthPixels;
                ProfileActivity.this.avatar.setMinimumWidth(width);
                ProfileActivity.this.avatar.setMinimumHeight(width);
                //// TODO: 13.08.2017 Сделать проверку наличия пути
                if (user.avatar != null) {
                    Picasso.with(getApplicationContext())
                            .load(Uri.parse(Constant.upload + user.avatar.normal))
                            .resize(width, width)
                            .centerCrop()
                            .error(R.mipmap.avatar)
                            .into(ProfileActivity.this.avatar);
                } else {
                    Picasso.with(getApplicationContext())
                            .load(Constant.default_avatar)
                            .resize(width, width)
                            .centerCrop()
                            .error(R.mipmap.avatar)
                            .into(ProfileActivity.this.avatar);
                }

                //Кнопка загрузки новых фотографий
                if (isUserProfile) newPhotoButton.setVisibility(View.VISIBLE);
                else newPhotoButton.setVisibility(View.GONE);

                //Устанавливаем дополнительные фотки и их кол-во
                if(user.photos != null) {
                    GV.setVisibility(View.VISIBLE);
                    final ArrayList<com.httpso_hello.hello.Structures.Photo> defolt = new ArrayList<com.httpso_hello.hello.Structures.Photo>();
                    Collections.addAll(defolt, user.photos);
                    plAdapter = new PhotosUserAdapter(activity, defolt, R.layout.content_profile);

                    photo_count.setText(defolt.size() + " фото");

                    float density = getApplicationContext().getResources().getDisplayMetrics().density;
                    float llColumnWidth = defolt.size() * 120 * density;
                    float llHorizontalSpacing = (defolt.size() - 1) * 4 * density;
                    float paddingLeftAndRight = 20 * density;
                    ll.getLayoutParams().width = (int) (llColumnWidth + llHorizontalSpacing + paddingLeftAndRight);
                    ll.requestLayout();
                    GV.setNumColumns(defolt.size());
                    GV.setAdapter(plAdapter);

                    // Обработчик клика по фотке
                    GV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            com.httpso_hello.hello.Structures.Photo photo = ((PhotosUserAdapter) parent.getAdapter()).getItem(position);
                            ArrayList<String> photoOrig = new ArrayList<String>();
                            ArrayList<Integer> photoLike = new ArrayList<Integer>();
                            ArrayList<Integer> photoId = new ArrayList<Integer>();
                            ArrayList<String> photoIsVoted = new ArrayList<String>();
                            for (int j = 0; j < user.photos.length; j++){
                                photoOrig.add(j, Constant.upload + user.photos[j].image.original);
                                photoLike.add(j, user.photos[j].rating);
                                photoId.add(j, user.photos[j].id);
                                photoIsVoted.add(j, Boolean.toString(user.photos[j].is_voted));
                            }
                            Intent intent = new Intent(ProfileActivity.this, FullscreenPhotoActivity.class);
                            intent.putStringArrayListExtra("photoOrig", photoOrig);
                            intent.putIntegerArrayListExtra("photoLike", photoLike);
                            intent.putIntegerArrayListExtra("photoId", photoId);
                            intent.putStringArrayListExtra("photoIsVoted", photoIsVoted);
                            intent.putExtra("likeble", true);
                            intent.putExtra("position", position);
                            startActivity(intent);
                        }
                    });
                } else {
                    GV.setVisibility(View.GONE);
                }

                //Устанавливаем город
                if (user.city_cache != null) cityCache.setText(user.city_cache);

                //Семейное положение
                if (user.sp != null) {
                    spProfileContener.setVisibility(View.VISIBLE);
                    if (user.sp.equals("1")) if (user.gender != 1) spProfile.setText("Свободна"); else spProfile.setText("Свободен");
                    if (user.sp.equals("2")) spProfile.setText("В активном поиске");
                    if (user.sp.equals("3")) if (user.gender != 1) spProfile.setText("Есть друг"); else spProfile.setText("Есть подруга");
                    if (user.sp.equals("4")) if (user.gender != 1) spProfile.setText("Влюблёна"); else spProfile.setText("Влюблён");
                    if (user.sp.equals("5")) if (user.gender != 1) spProfile.setText("Замужем"); else spProfile.setText("Женат");
                } else spProfileContener.setVisibility(View.GONE);

                //Устанавливаем телефон
                if(user.phone != null) {
                    phoneNumProfileConteiner.setVisibility(View.VISIBLE);
                    phoneNumProfile.setText(user.phone);
                } else phoneNumProfileConteiner.setVisibility(View.GONE);

                //Устанавливаем скайп
                if(user.skype != null) {
                    skypeProfileConteiner.setVisibility(View.VISIBLE);
                    skypeProfile.setText(user.skype);
                } else skypeProfileConteiner.setVisibility(View.GONE);

                //Материальное положение
                if ((user.mat_poloz == 0) || (user.mat_poloz == 1)) dohodProfileContener.setVisibility(View.GONE);
                if (user.mat_poloz == 2) dohodProfile.setText("Нет дохода");
                if (user.mat_poloz == 3) dohodProfile.setText("Низкий доход");
                if (user.mat_poloz == 4) dohodProfile.setText("Стабильный средний доход");
                if (user.mat_poloz == 5) dohodProfile.setText("Высокий доход");

                //Автомобиль
                if (user.avto != null) {
                    autoProfile.setText(user.avto);
                    autoProfileContener.setVisibility(View.VISIBLE);
                } else autoProfileContener.setVisibility(View.GONE);

                //Рост и вес
                if ((user.rost == 0) && (user.ves == 0)) rostAndVesProfileContener.setVisibility(View.GONE);
                else {
                    rostAndVesProfileContener.setVisibility(View.VISIBLE);
                    if (user.rost != 0) {
                        rostTitleProfile.setVisibility(View.VISIBLE);
                        rostProfile.setVisibility(View.VISIBLE);
                        rostProfile.setText(Integer.toString(user.rost) + " см");
                    } else {
                        rostTitleProfile.setVisibility(View.GONE);
                        rostProfile.setVisibility(View.GONE);
                    }
                    if (user.ves != 0) {
                        vesTitleProfile.setVisibility(View.VISIBLE);
                        vesProfile.setVisibility(View.VISIBLE);
                        vesProfile.setText(Integer.toString(user.ves) + " кг");
                    } else {
                        vesTitleProfile.setVisibility(View.GONE);
                        vesProfile.setVisibility(View.GONE);
                    }
                }

                //скрываем блок с друзьями и подарками если их нет
                if ((user.friends_count != 0) || (user.gifts.length != 0)) friendsAndGiftsBlock.setVisibility(View.VISIBLE);
                else friendsAndGiftsBlock.setVisibility(View.GONE);

                //Кол-во друзей
                if(user.friends_count != 0) {
                    String friendsStr = Integer.toString(user.friends_count);
                    if(user.friends_count == 1) friendsCount.setText(friendsStr + " друг");
                    else if((user.friends_count >= 2) && (user.friends_count < 5)) friendsCount.setText(friendsStr + " друга");
                    else if((user.friends_count >= 5) && (user.friends_count < 21)) friendsCount.setText(friendsStr + " друзей");
                    else if((user.friends_count >= 21) && (user.friends_count % 10 == 1)) friendsCount.setText(friendsStr + " друг");
                    else if((user.friends_count >= 21) && (user.friends_count % 10 == 2)) friendsCount.setText(friendsStr + " друга");
                    else if((user.friends_count >= 21) && (user.friends_count % 10 == 3)) friendsCount.setText(friendsStr + " друга");
                    else if((user.friends_count >= 21) && (user.friends_count % 10 == 4)) friendsCount.setText(friendsStr + " друга");
                    else friendsCount.setText(friendsStr + " друзей");
                } else {
                    friendsCount.setText("Нет друзей");
                }
                if (user.friends.length > 0) {
                    friendsBlock.setVisibility(View.VISIBLE);
                    friend1.setVisibility(View.VISIBLE);
                    friendsLayout1.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[0])
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend1);
                } else friendsBlock.setVisibility(View.GONE);
                if (user.friends.length > 1) {
                    friend2.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[1])
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend2);
                }
                if (user.friends.length > 2) {
                    friend3.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[2])
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend3);
                }
                if (user.friends.length > 3) {
                    friend4.setVisibility(View.VISIBLE);
                    friendsLayout2.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[3])
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend4);
                }
                if (user.friends.length > 4) {
                    friend5.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[4])
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend5);
                }
                if (user.friends.length > 5) {
                    friend6.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[5])
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend6);
                }
                if (user.friends.length > 6) {
                    friend7.setVisibility(View.VISIBLE);
                    friendsLayout3.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[6])
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend7);
                }
                if (user.friends.length > 7) {
                    friend8.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[7])
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend8);
                }
                if (user.friends.length > 8) {
                    friend9.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[8])
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend9);
                }

                //Кол-во подарков
                if(user.gifts.length != 0) {
                    String giftsStr = Integer.toString(user.gifts.length);
                    if(user.gifts.length == 1) giftsCount.setText(giftsStr + " подарок");
                    else if((user.gifts.length >= 2) && (user.gifts.length < 5)) giftsCount.setText(giftsStr + " подарка");
                    else if((user.gifts.length >= 5) && (user.gifts.length < 21)) giftsCount.setText(giftsStr + " подарков");
                    else if((user.gifts.length >= 21) && (user.gifts.length % 10 == 1)) giftsCount.setText(giftsStr + " подарок");
                    else if((user.gifts.length >= 21) && (user.gifts.length % 10 == 2)) giftsCount.setText(giftsStr + " подарка");
                    else if((user.gifts.length >= 21) && (user.gifts.length % 10 == 3)) giftsCount.setText(giftsStr + " подарка");
                    else if((user.gifts.length >= 21) && (user.gifts.length % 10 == 4)) giftsCount.setText(giftsStr + " подарка");
                    else giftsCount.setText(giftsStr + " подарков");
                } else {
                    giftsCount.setText("Нет подарков");
                }
                if (user.gifts.length > 0) {
                    giftsBlock.setVisibility(View.VISIBLE);
                    gift1.setVisibility(View.VISIBLE);
                    giftsLayout1.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.gifts[0])
                            .error(R.mipmap.avatar)
                            .into(gift1);
                } else giftsBlock.setVisibility(View.GONE);
                if (user.gifts.length > 1) {
                    gift2.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.gifts[1])
                            .error(R.mipmap.avatar)
                            .into(gift2);
                }
                if (user.gifts.length > 2) {
                    gift3.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.gifts[2])
                            .error(R.mipmap.avatar)
                            .into(gift3);
                }
                if (user.gifts.length > 3) {
                    gift4.setVisibility(View.VISIBLE);
                    giftsLayout2.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.gifts[3])
                            .error(R.mipmap.avatar)
                            .into(gift4);
                }
                if (user.gifts.length > 4) {
                    gift5.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.gifts[4])
                            .error(R.mipmap.avatar)
                            .into(gift5);
                }
                if (user.gifts.length > 5) {
                    gift6.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.gifts[5])
                            .error(R.mipmap.avatar)
                            .into(gift6);
                }
                if (user.gifts.length > 6) {
                    gift7.setVisibility(View.VISIBLE);
                    giftsLayout3.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.gifts[6])
                            .error(R.mipmap.avatar)
                            .into(gift7);
                }
                if (user.gifts.length > 7) {
                    gift8.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.gifts[7])
                            .error(R.mipmap.avatar)
                            .into(gift8);
                }
                if (user.gifts.length > 8) {
                    gift9.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.gifts[8])
                            .error(R.mipmap.avatar)
                            .into(gift9);
                }

                //Скрыть блок "Ищу", если нет никакой инфы
                if ((user.looking_for == 0) && (user.looking_for_age == 0) && (user.age_do == 0) && (user.reg_cel == null)) lookinForBar.setVisibility(View.GONE);
                else lookinForBar.setVisibility(View.VISIBLE);

                //Скрываем полосочку в блоке Ищу
                if ((user.looking_for_age == 0) && (user.looking_for == 0) && (user.age_do == 0)) findView.setVisibility(View.GONE);
                else findView.setVisibility(View.VISIBLE);

                //С кем хочет познакомиться
                if (user.looking_for != 0) {
                    if (user.looking_for == 1) {
                        looking_for.setVisibility(View.VISIBLE);
                        looking_for.setText("Познакомлюсь с парнем");
                    }
                    if (user.looking_for == 2) {
                        looking_for.setVisibility(View.VISIBLE);
                        looking_for.setText("Познакомлюсь с девушкой");
                    }
                    if (user.looking_for == 3) {
                        looking_for.setVisibility(View.VISIBLE);
                        looking_for.setText("Познакомлюсь со всеми");
                    }
                } else looking_for.setVisibility(View.GONE);

                //Диапазон возраста для поиска
                if ((user.looking_for_age != 0) && (user.age_do != 0)) {
                    ageLookinFor.setVisibility(View.VISIBLE);
                    ageLookinFor.setText("Возраст от " + Integer.toString(user.looking_for_age) + " до " + Integer.toString(user.age_do) + " лет");
                }
                else if (user.looking_for_age != 0) {
                    ageLookinFor.setVisibility(View.VISIBLE);
                    ageLookinFor.setText("Возраст от " + Integer.toString(user.looking_for_age) + " лет");
                }
                else if (user.age_do != 0) {
                    ageLookinFor.setVisibility(View.VISIBLE);
                    ageLookinFor.setText("Возраст до " + Integer.toString(user.age_do) + " лет");
                }
                else ageLookinFor.setVisibility(View.GONE);

                //Цель знакомства
                if (user.reg_cel != null) {
                    celZnakomstvaProfileLL.setVisibility(View.VISIBLE);
                    char[] cel = user.reg_cel.toCharArray();
                    if (cel.length >= 1) if (cel[0] == '1') celZnakomstvaProfile0.setVisibility(View.VISIBLE);
                    if (cel.length >= 2) if (cel[1] == '1') celZnakomstvaProfile1.setVisibility(View.VISIBLE);
                    if (cel.length >= 3) if (cel[2] == '1') celZnakomstvaProfile2.setVisibility(View.VISIBLE);
                    if (cel.length >= 4) if (cel[3] == '1') celZnakomstvaProfile3.setVisibility(View.VISIBLE);
                    if (cel.length >= 5) if (cel[4] == '1') celZnakomstvaProfile4.setVisibility(View.VISIBLE);
                    if (cel.length >= 6) if (cel[5] == '1') celZnakomstvaProfile5.setVisibility(View.VISIBLE);
                    if (cel.length >= 7) if (cel[6] == '1') celZnakomstvaProfile6.setVisibility(View.VISIBLE);
                    if (cel.length >= 8) if (cel[7] == '1') celZnakomstvaProfile7.setVisibility(View.VISIBLE);
                    if (cel.length >= 9) if (cel[8] == '1') celZnakomstvaProfile8.setVisibility(View.VISIBLE);
                    if (cel.length >= 10) if (cel[9] == '1') celZnakomstvaProfile9.setVisibility(View.VISIBLE);
                    if (cel.length >= 11) if (cel[10] == '1') celZnakomstvaProfile10.setVisibility(View.VISIBLE);
                    if (cel.length >= 12) if (cel[11] == '1') celZnakomstvaProfile11.setVisibility(View.VISIBLE);
                } else celZnakomstvaProfileLL.setVisibility(View.GONE);

                //Интересы
                if (    (user.music != null) ||
                        (user.movies != null) ||
                        (user.books != null) ||
                        (user.games != null) ||
                        (user.interes != null)
                        ) anketaBar.setVisibility(View.VISIBLE);
                else anketaBar.setVisibility(View.GONE);

                //Любимая музыка
                if (user.music != null) {
                    musicProfile.setText(user.music);
                    musicProfileContener.setVisibility(View.VISIBLE);
                } else musicProfileContener.setVisibility(View.GONE);

                //Любимые фильмы
                if (user.movies != null) {
                    moviesProfile.setText(user.movies);
                    moviesProfileContener.setVisibility(View.VISIBLE);
                } else moviesProfileContener.setVisibility(View.GONE);

                //Любимые книги
                if (user.books != null) {
                    booksProfile.setText(user.books);
                    booksProfileContener.setVisibility(View.VISIBLE);
                } else booksProfileContener.setVisibility(View.GONE);

                //Любимые игры
                if (user.games != null) {
                    gamesProfile.setText(user.games);
                    gamesProfileContener.setVisibility(View.VISIBLE);
                } else gamesProfileContener.setVisibility(View.GONE);

                //Другие интересы
                if (user.interes != null) {
                    intrestingProfile.setText(user.interes);
                    intrestingProfileContener.setVisibility(View.VISIBLE);
                } else intrestingProfileContener.setVisibility(View.GONE);

                //Скрытия индикатора загрузки и открытие профиля
                progressBarProfile.setVisibility(View.INVISIBLE);
                profile_content.setVisibility(View.VISIBLE);
                profile_content2.setVisibility(View.VISIBLE);

                //Скрыть кнопок "Написать сообщение" и "Добавить в друзья" если профиль юзера
                if (isUserProfile) msgAndFrndBtn.setVisibility(View.GONE);
                else msgAndFrndBtn.setVisibility(View.VISIBLE);

                //Кнопка отправки симпатии, скрыть если профиль юзера или если уже отправлял
                if((isUserProfile) || (user.flirt_state == 4)) fab.setVisibility(View.GONE);
                else fab.setVisibility(View.VISIBLE);

                if (user.flirt_state == 0) { // никто никому не отправлял симпатию
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            progressBarFlirtik.setVisibility(View.VISIBLE);
                            Simpation.getInstance(getApplicationContext()).sendFlirtik(user.id, new Simpation.SendSimpationCallback() {
                                @Override
                                public void onSuccess(String response) {
                                    progressBarFlirtik.setVisibility(View.GONE);
                                    Snackbar
                                            .make(view, "Симпатия отправлена", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }

                                @Override
                                public void onError(int error_code, String error_msg) {
                                    progressBarFlirtik.setVisibility(View.GONE);
                                    Snackbar.make(view, error_msg, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                                @Override
                                public void onInternetError() {
                                    progressBarFlirtik.setVisibility(View.GONE);
                                    Snackbar.make(view, "Ошибка интернет соединения", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });

                        }
                    });
                } else if (user.flirt_state == 1) { // пользователь отправил просматриеваемому пользователю симпатию
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            Snackbar.make(view, "Пользователь уже получал вашу симпатию", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                        }
                    });
                } else if (user.flirt_state == 2) { //просматриваемый пользователь отправил пользователю симпатию
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            progressBarFlirtik.setVisibility(View.VISIBLE);
                            Simpation.getInstance(getApplicationContext()).sendFlirtik(user.id, new Simpation.SendSimpationCallback() {
                                @Override
                                public void onSuccess(String response) {
                                    progressBarFlirtik.setVisibility(View.GONE);
                                    fab.setImageResource(R.drawable.ic_action_two_heart);
                                    Snackbar
                                            .make(view, "Ваша симпатия взаимна", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }

                                @Override
                                public void onError(int error_code, String error_msg) {
                                    progressBarFlirtik.setVisibility(View.GONE);
                                    Snackbar.make(view, error_msg, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                                @Override
                                public void onInternetError() {
                                    progressBarFlirtik.setVisibility(View.GONE);
                                    Snackbar.make(view, "Ошибка интернет соединения", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });
                        }
                    });
                } else if (user.flirt_state == 3) {  //симпатия взаимна
                    fab.setImageResource(R.drawable.ic_action_two_heart);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            Snackbar.make(view, "Ваша симпатия взаимна", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                }

                // клик по аве
                if (!isUserProfile){
                    avatar.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            ArrayList<String> photoOrig = new ArrayList<String>();
                            photoOrig.add(0, Constant.upload + user.avatar.original);
                            Intent intent = new Intent(ProfileActivity.this, FullscreenPhotoActivity.class);
                            intent.putStringArrayListExtra("photoOrig", photoOrig);
                            intent.putExtra("likeble", false);
                            intent.putExtra("position", 0);
                            startActivity(intent);
                        }
                    });
                } else avatar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();

                        popUpWindow.setWidth(displaymetrics.widthPixels);
                        popUpWindow.setHeight(displaymetrics.heightPixels);
                        popUpWindow.setAnimationStyle(Animation_Dialog);
                        popUpWindow.showAtLocation(profile_content2, Gravity.CENTER, 0, 0);

                        // отмена поп-ап-а
                        popUpWindow.getContentView().findViewById(R.id.popup_for_avatar).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUpWindow.dismiss();
                            }
                        });

                        // Загрузить аву из галереи
                        popUpWindow.getContentView().findViewById(R.id.avaOpen).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUpWindow.dismiss();
                                ArrayList<String> photoOrig = new ArrayList<String>();
                                photoOrig.add(0, Constant.upload + user.avatar.original);
                                Intent intent = new Intent(ProfileActivity.this, FullscreenPhotoActivity.class);
                                intent.putStringArrayListExtra("photoOrig", photoOrig);
                                intent.putExtra("likeble", false);
                                intent.putExtra("position", 0);
                                startActivity(intent);
                            }
                        });

                        // Загрузить аву из галереи
                        popUpWindow.getContentView().findViewById(R.id.avaFromGalery).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUpWindow.dismiss();
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, 3);
                            }
                        });

                        // Загрузить аву с камеры
                        popUpWindow.getContentView().findViewById(R.id.avaFromCamera).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUpWindow.dismiss();
                                if(Help.runTaskAfterPermission(
                                        ProfileActivity.this,
                                        new String[] {
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA
                                        },
                                        Help.REQUEST_UPDATE_AVATAR_CAMERA

                                )) {
                                    openCameraWindow(4);
                                }
                            }
                        });
                    }
                });

                // Обработчик клика по кнопке загрузки новых фоток
                newPhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();

                        popUpWindow2.setWidth(displaymetrics.widthPixels);
                        popUpWindow2.setHeight(displaymetrics.heightPixels);
                        popUpWindow2.setAnimationStyle(Animation_Dialog);
                        popUpWindow2.showAtLocation(profile_content2, Gravity.CENTER, 0, 0);

                        // отмена поп-ап-а
                        popUpWindow2.getContentView().findViewById(R.id.popup_for_new_photo).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUpWindow2.dismiss();
                            }
                        });

                        // Загрузка новых фоток из галереи
                        popUpWindow2.getContentView().findViewById(R.id.newPhotoFromGalery).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUpWindow2.dismiss();
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, 1);
                            }
                        });

                        // Загрузка новых фоток с камеры
                        popUpWindow2.getContentView().findViewById(R.id.newPhotoFromCamera).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(Help.runTaskAfterPermission(
                                        ProfileActivity.this,
                                        new String[] {
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA
                                        },
                                        Help.REQUEST_ADD_PHOTO_CAMERA

                                )) {
                                    openCameraWindow(2);
                                }
                            }
                        });
                    }
                });

                // Обработчик клика по кол-ву друзей
                friendsCountButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(ProfileActivity.this, FriendsActivity.class);
                        intent.putExtra("profile_id", user.id);
                        startActivity(intent);
                    }
                });

                //Обработка нажатия кнопки "Сообщение"
                Button writeMes = (Button) findViewById(R.id.write_message);
                writeMes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                        intent.putExtra("contact_id", ProfileActivity.this.profile_id);
                        intent.putExtra("nickname", ProfileActivity.this.profile_nickname);
                        if(user.avatar != null){
                            intent.putExtra("avatar", user.avatar.micro);
                        } else {
                            intent.putExtra("avatar", Constant.default_avatar);
                        }
                        startActivity(intent);
                    }
                });

                //Обработка нажатия кнопки "Добавить в друзья"
                final Button toFriends = (Button) findViewById(R.id.to_friends);
                if (user.friends_state == 0) {
                    toFriends.setHint("Добавить в друзья");
                    toFriends.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toFriends.setBackgroundColor(getResources().getColor(R.color.main_dark_grey_color_hello));
                            toFriends.setHint("Отправляем заявку...");
                            Friend.getInstance(getApplicationContext()).addFriend(user.id, new Friend.AddFriendsCallback() {
                                @Override
                                public void onSuccess() {
                                    toFriends.setBackgroundColor(getResources().getColor(R.color.main_dark_grey_color_hello));
                                    toFriends.setHint("Заявка отправлена");
                                }

                                @Override
                                public void onError(int error_code, String error_msg) {
                                    toFriends.setBackgroundColor(getResources().getColor(R.color.main_blue_color_hello));
                                    toFriends.setHint("Добавить в друзья");
                                    Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onInternetError() {
                                    toFriends.setBackgroundColor(getResources().getColor(R.color.main_blue_color_hello));
                                    toFriends.setHint("Добавить в друзья");
                                    Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
                else if (user.friends_state == 3) {
                    toFriends.setHint("Удалить из друзей");
                    toFriends.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toFriends.setBackgroundColor(getResources().getColor(R.color.main_dark_grey_color_hello));
                            toFriends.setHint("Удаляем из друзей...");
                            Friend.getInstance(getApplicationContext()).deleteFriend(user.id, new Friend.DeleteFriendsCallback() {
                                @Override
                                public void onSuccess() {
                                    toFriends.setBackgroundColor(getResources().getColor(R.color.main_dark_grey_color_hello));
                                    toFriends.setHint("Удален из друзей");
                                }

                                @Override
                                public void onError(int error_code, String error_msg) {
                                    toFriends.setBackgroundColor(getResources().getColor(R.color.main_blue_color_hello));
                                    toFriends.setHint("Удалить из друзей");
                                    Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onInternetError() {
                                    toFriends.setBackgroundColor(getResources().getColor(R.color.main_blue_color_hello));
                                    toFriends.setHint("Удалить из друзей");
                                    Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                } else if (user.friends_state == 2) {
                    toFriends.setHint("Принять заявку");
                    toFriends.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toFriends.setBackgroundColor(getResources().getColor(R.color.main_dark_grey_color_hello));
                            toFriends.setHint("Принимаем заявку...");
                            Friend.getInstance(getApplicationContext()).acceptFriend(user.id, new Friend.AcceptFriendCallback() {
                                @Override
                                public void onSuccess() {
                                    toFriends.setBackgroundColor(getResources().getColor(R.color.main_dark_grey_color_hello));
                                    toFriends.setHint("Добавлен в друзья");
                                }

                                @Override
                                public void onError(int error_code, String error_msg) {
                                    toFriends.setBackgroundColor(getResources().getColor(R.color.main_blue_color_hello));
                                    toFriends.setHint("Принять заявку");
                                    Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onInternetError() {
                                    toFriends.setBackgroundColor(getResources().getColor(R.color.main_blue_color_hello));
                                    toFriends.setHint("Принять заявку");
                                    Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                } else if (user.friends_state == 1) {
                    toFriends.setBackgroundColor(getResources().getColor(R.color.main_dark_grey_color_hello));
                    toFriends.setHint("Заявка отправлена");
                }
            }

            @Override
            public void onError(int error_code, String error_msg) {
                Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onInternetError() {
                Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    //Обрабатываем результат выбора фоток из галереи или с камеры
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            //загрузка нового фото из галереи в ленту
            case 1:
                if (resultCode == RESULT_OK) {
                    progressBarProfile.setVisibility(View.VISIBLE);
                    imageUri = imageReturnedIntent.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Help.runTaskAfterPermission(
                                ProfileActivity.this,
                                new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                },
                                Help.REQUEST_ADD_PHOTO_GALLERY
                        )) {
                            openAvatarUpdateWindow(sendPhotoClick);
                        }
                    } else {
                        openAvatarUpdateWindow(sendPhotoClick);
                    }
                }
                break;
            //загрузка нового фото с камеры в ленту
            case 2:
                if (resultCode == RESULT_OK) {
//                    try {
                    progressBarProfile.setVisibility(View.VISIBLE);
                    imageUri = Uri.fromFile(photoFile);
                    openAvatarUpdateWindow(sendPhotoClick);
                    /*
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if(Help.runTaskAfterPermission(
                                ProfileActivity.this,
                                new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                },
                                Help.REQUEST_READ_EXTERNAL_STORAGE
                        )){
                            openAvatarUpdateWindow(sendPhotoClick);
                        }
                    } else{
                            openAvatarUpdateWindow(sendPhotoClick);
                    }
                    */
                }
                break;
            //загрузка новой аватарки из галереи
            case 3:
                if (resultCode == RESULT_OK) {
                    progressBarProfile.setVisibility(View.VISIBLE);
                    imageUri = imageReturnedIntent.getData();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if(Help.runTaskAfterPermission(
                                ProfileActivity.this,
                                new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                },
                                Help.REQUEST_ADD_PHOTO_GALLERY
                        )){
                            openAvatarUpdateWindow(updateAvatarClick);
                        }
                    } else{
                        openAvatarUpdateWindow(updateAvatarClick);
                    }

                }
                break;
            //загрузка новой аватарки с камеры
            case 4:
                if (resultCode == RESULT_OK) {
                    progressBarProfile.setVisibility(View.VISIBLE);
                    imageUri = Uri.fromFile(photoFile);
                    openAvatarUpdateWindow(updateAvatarClick);
                    /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if(Help.runTaskAfterPermission(
                                ProfileActivity.this,
                                new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                },
                                Help.REQUEST_READ_EXTERNAL_STORAGE
                        )){
                            openAvatarUpdateWindow(updateAvatarClick);
                        }
                    } else{
                        openAvatarUpdateWindow(updateAvatarClick);
                    }
                    */
                }
                break;
            default:
                System.out.println("Какая-то ошибка");
                break;
        }
    }

    public void openAvatarUpdateWindow(View.OnClickListener saveButtonClick){
        try{

            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            selectedImage = BitmapFactory.decodeStream(imageStream);

            DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();

            popUpWindow3.setWidth(displaymetrics.widthPixels);
            popUpWindow3.setHeight(displaymetrics.heightPixels);
            popUpWindow3.setAnimationStyle(Animation_Dialog);
            popUpWindow3.showAtLocation(profile_content2, Gravity.CENTER, 0, 0);

            Picasso.with(getApplicationContext())
                    .load(imageUri)
                    .resize(displaymetrics.widthPixels, displaymetrics.widthPixels)
                    .centerCrop()
                    .into(((ImageView) popUpWindow3.getContentView().findViewById(R.id.newPhoto)));
            ((TextView) popUpWindow3.getContentView().findViewById(R.id.savePhoto)).setText("Сохранить");
            ((LinearLayout) popUpWindow3.getContentView().findViewById(R.id.popup_accept_new_photo)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popUpWindow3.dismiss();
                }
            });

            ((TextView) popUpWindow3.getContentView().findViewById(R.id.savePhoto)).setOnClickListener(saveButtonClick);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener updateAvatarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBarProfile.setVisibility(View.VISIBLE);
            ((TextView) popUpWindow3.getContentView().findViewById(R.id.savePhoto)).setText("Сохраняем...");
            final String base64_code_ava = Help.getBase64FromImage(
                    selectedImage,
                    Bitmap.CompressFormat.JPEG,
                    ProfileActivity.this,
                    Help.getBitmapSize(selectedImage, Bitmap.CompressFormat.JPEG),
                    0
            );
            Profile.getInstance(getApplicationContext()).updateAvatar(
                    base64_code_ava,
                    "jpg",
                    new Profile.UpdateAvatarCallback() {
                        @Override
                        public void onSuccess(Image avatar) {
                            if (avatar != null) {
                                Picasso.with(getApplicationContext())
                                        .load(Uri.parse(Constant.upload + avatar.normal))
                                        .resize(width, width)
                                        .centerCrop()
                                        .error(R.mipmap.avatar)
                                        .into(ProfileActivity.this.avatar);
                            } else {
                                Picasso.with(getApplicationContext())
                                        .load(Constant.default_avatar)
                                        .resize(width, width)
                                        .centerCrop()
                                        .error(R.mipmap.avatar)
                                        .into(ProfileActivity.this.avatar);
                            }
                            progressBarProfile.setVisibility(View.GONE);
                            popUpWindow3.dismiss();
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {
                            progressBarProfile.setVisibility(View.GONE);
                            popUpWindow3.dismiss();
                        }

                        @Override
                        public void onInternetError() {
                            progressBarProfile.setVisibility(View.GONE);
                            popUpWindow3.dismiss();
                        }
                    }
            );
        }
    };

    private View.OnClickListener sendPhotoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBarProfile.setVisibility(View.VISIBLE);
            ((TextView) popUpWindow3.getContentView().findViewById(R.id.savePhoto)).setText("Сохраняем...");
            final String base64_code_ava = Help.getBase64FromImage(selectedImage, Bitmap.CompressFormat.JPEG);
            Photo.getInstance(getApplicationContext()).addPhoto(
                    base64_code_ava,
                    "jpg",
                    new Photo.AddPhotoCallback() {
                        @Override
                        public void onSuccess() {

                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {

                        }

                        @Override
                        public void onInternetError() {

                        }
                    }
            );
        }
    };

    // Обработка запроса получения доступа
    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Help.REQUEST_UPDATE_AVATAR_GALLERY:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted.
                    openAvatarUpdateWindow(updateAvatarClick);
                } else {
                    // User refused to grant permission.
//                    Toast.makeText(this, "Для доступа к фото необходимо ваше разрешение", Toast.LENGTH_LONG).show();

                }
                break;
            case Help.REQUEST_UPDATE_AVATAR_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted.
                    openCameraWindow(4);
                } else {
                    // User refused to grant permission.
//                    Toast.makeText(this, "Для доступа к фото необходимо ваше разрешение", Toast.LENGTH_LONG).show();
                }
                break;
            case Help.REQUEST_ADD_PHOTO_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted.
                    openCameraWindow(2);
                } else {
                    // User refused to grant permission.
//                    Toast.makeText(this, "Для доступа к фото необходимо ваше разрешение", Toast.LENGTH_LONG).show();
                }
                break;
            case Help.REQUEST_ADD_PHOTO_GALLERY:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted.
                    openAvatarUpdateWindow(sendPhotoClick);
                } else {
                    // User refused to grant permission.
//                    Toast.makeText(this, "Для доступа к фото необходимо ваше разрешение", Toast.LENGTH_LONG).show();

                }
                break;

        }

    }

    @Override
    public void onBackPressed() {
        if (popUpWindow.isShowing()) popUpWindow.dismiss();
        else if (popUpWindow2.isShowing()) popUpWindow2.dismiss();
        else if (popUpWindow3.isShowing()) popUpWindow3.dismiss();
        else super.onBackPressed();
    }

    public void openCameraWindow(int request_code){
        Intent photoCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoCameraIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try{
                photoFile = Help.createImageFile(ProfileActivity.this);
            } catch (IOException ex){
                ex.printStackTrace();
            }
            if(photoFile!=null){
                Uri photoUri = FileProvider.getUriForFile(
                        this,
                        "com.httpso_hello.hello.fileprovider",
                        photoFile
                );
                photoCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(photoCameraIntent, request_code);
            }


        }
    }

    // Создание файла для камеры

}
