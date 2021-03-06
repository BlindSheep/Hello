package com.httpso_hello.hello.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.GiftItem;
import com.httpso_hello.hello.Structures.GiftItemNew;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.NewProfile;
import com.httpso_hello.hello.activity.Super.SocketActivity;
import com.httpso_hello.hello.adapters.GiftsGetGiftsAdapter;
import com.httpso_hello.hello.adapters.GiftsInProfileAdapter;
import com.httpso_hello.hello.adapters.PhotosUserAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Friend;
import com.httpso_hello.hello.helper.Gifts;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Messages;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.Photo;
import com.httpso_hello.hello.helper.Simpation;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.R.style.Animation_Dialog;
import static com.httpso_hello.hello.helper.ConverterDate.convertDateForEnter;
import static com.httpso_hello.hello.helper.ConverterDate.convertDateToAge;

public class ProfileActivity extends SocketActivity {

    private ImageView avatar;
    private int profile_id;
    private String profile_nickname;
    private String profile_avatar;
    private PhotosUserAdapter plAdapter;
    private LinearLayout friendsCountButton;
    private ProgressBar progressBarFlirtik;
    private boolean isUserProfile;
    private FloatingActionButton fab;
    private LinearLayout newPhotoButton;
    private TextView nameToolbar;
    private TextView onlineToolbar;
    private LinearLayout profile_content;
    private CoordinatorLayout profile_content2;
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
    private TextView groupsCount;
    private TextView cityCache;
    private TextView looking_for;
    private LinearLayout lookinForBar;
    private LinearLayout celZnakomstvaProfileLL;
    private RelativeLayout msgAndFrndBtn;
    private TextView skypeProfile;
    private LinearLayout skypeProfileConteiner;
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
    private LinearLayout friendsBlock;
    private LinearLayout friendsLayout1;
    private LinearLayout friendsLayout2;
    private ImageView groups1;
    private ImageView groups2;
    private ImageView groups3;
    private ImageView groups4;
    private ImageView groups5;
    private ImageView groups6;
    private LinearLayout groupsBlock;
    private LinearLayout groupsLayout1;
    private LinearLayout groupsLayout2;
    private TextView giftsCount;
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
    private PopupWindow popUpWindow4;
    private View popupView4;
    private ImageView iconForAva;
    private Bundle extras;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DisplayMetrics displaymetrics;
    private PopupWindow popUpWindowSendGift;
    private View popupViewSendGift;
    private GridView giftsGrid;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_profile);
        setHeader();

        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            final List<String> segments = intent.getData().getPathSegments();
            if (segments.size() > 1) {
                profile_id = Integer.parseInt(segments.get(1));
            }
        } else profile_id = extras.getInt("profile_id");

        profile_nickname = extras.getString("profile_nickname");
        profile_avatar = extras.getString("avatar");
        avatar = (ImageView) findViewById(R.id.avatar);
        progressBarFlirtik = (ProgressBar) findViewById(R.id.progressBarFlirtik);
        newPhotoButton = ((LinearLayout) findViewById(R.id.new_photo_button));
        nameToolbar = (TextView) findViewById(R.id.nameToolbar);
        onlineToolbar = (TextView) findViewById(R.id.onlineToolbar);
        profile_content = (LinearLayout) findViewById(R.id.profile_content);
        profile_content2 = (CoordinatorLayout) findViewById(R.id.coord);
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
        celZnakomstvaProfile10 = (TextView)
                findViewById(R.id.celZnakomstvaProfile10);
        celZnakomstvaProfile11 = (TextView) findViewById(R.id.celZnakomstvaProfile11);
        ageLookinFor = (TextView)findViewById(R.id.ageLookinFor);
        GV = (GridView) findViewById(R.id.profile_photos_list);
        ll = (LinearLayout) findViewById(R.id.linearLayoutForPhoto);
        photo_count = (TextView) findViewById(R.id.photo_count);
        cityCache = (TextView) findViewById(R.id.cityCache);
        friendsCount = (TextView) findViewById(R.id.friendsCount);
        groupsCount = (TextView) findViewById(R.id.groupsCount);
        looking_for = (TextView)findViewById(R.id.looking_for);
        lookinForBar = (LinearLayout) findViewById(R.id.lookinForBar);
        celZnakomstvaProfileLL = (LinearLayout)findViewById(R.id.celZnakomstvaProfileLL);
        msgAndFrndBtn = (RelativeLayout) findViewById(R.id.msgAndFrndBtn);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        friendsCountButton = (LinearLayout) findViewById(R.id.friendsCountButton);
        skypeProfile = (TextView) findViewById(R.id.skypeProfile);
        skypeProfileConteiner = (LinearLayout) findViewById(R.id.skypeProfileConteiner);
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
        friendsBlock = (LinearLayout) findViewById(R.id.friendsBlock);
        friendsLayout1 = (LinearLayout) findViewById(R.id.friendsLayout1);
        friendsLayout2 = (LinearLayout) findViewById(R.id.friendsLayout2);
        groups1 = (ImageView) findViewById(R.id.groups1);
        groups2 = (ImageView) findViewById(R.id.groups2);
        groups3 = (ImageView) findViewById(R.id.groups3);
        groups4 = (ImageView) findViewById(R.id.groups4);
        groups5 = (ImageView) findViewById(R.id.groups5);
        groups6 = (ImageView) findViewById(R.id.groups6);
        groupsBlock = (LinearLayout) findViewById(R.id.groupsBlock);
        groupsLayout1 = (LinearLayout) findViewById(R.id.groupsLayout1);
        groupsLayout2 = (LinearLayout) findViewById(R.id.groupsLayout2);
        giftsCount = (TextView) findViewById(R.id.giftsCount);
        friendsAndGiftsBlock = (LinearLayout) findViewById(R.id.friendsAndGiftsBlock);
        popupView = getLayoutInflater().inflate(R.layout.popup_for_avatar, null);
        popUpWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupView2 = getLayoutInflater().inflate(R.layout.popup_for_new_photo, null);
        popUpWindow2 = new PopupWindow(popupView2, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupView3 = getLayoutInflater().inflate(R.layout.popup_accept_new_photo, null);
        popUpWindow3 = new PopupWindow(popupView3, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupView4 = getLayoutInflater().inflate(R.layout.popup_for_wait, null);
        popUpWindow4 = new PopupWindow(popupView4, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((TextView) popupView4.findViewById(R.id.textForWaiting)).setText("Загружаем фото...");
        displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
        popUpWindow4.setWidth(displaymetrics.widthPixels);
        popUpWindow4.setHeight(displaymetrics.heightPixels);
        popUpWindow4.setAnimationStyle(Animation_Dialog);
        iconForAva = (ImageView) findViewById(R.id.iconForAva);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setScrimAnimationDuration(1000);
        popupViewSendGift = getLayoutInflater().inflate(R.layout.popup_for_send_gift, null);
        popUpWindowSendGift = new PopupWindow(popupViewSendGift, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpWindowSendGift.setWidth(displaymetrics.widthPixels);
        popUpWindowSendGift.setHeight(displaymetrics.heightPixels);
        popUpWindowSendGift.setAnimationStyle(Animation_Dialog);
        giftsGrid = ((GridView) findViewById(R.id.giftsGrid));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        swipeRefreshLayout.setRefreshing(true);
    }

    private void setContent() {
        Profile profile = new Profile(getApplicationContext());
        profile.getProfile(profile_id, this, new Profile.GetProfileCallback() {
            @Override
            public void onSuccess(final NewProfile user, Activity activity) {

                // Является ли открытая анкета, анкетой залогинившегося юзера
                isUserProfile = (stgs.getSettingInt("userId") == user.info.id);

                //Устанавливаем имя и возраст в тулбар
                if (user.info.birthDate != null) {
                    nameToolbar.setText(user.info.nickname + ", " +
                            convertDateToAge(user.info.birthDate));
                } else nameToolbar.setText(user.info.nickname);

                //Устанавливаем дату последнего захода в тулбар
                if (user.info.isOnline) {
                    onlineToolbar.setText("В сети");
                    onlineToolbar.setTextColor(getResources().getColor(R.color.main_green_color_hello));
                } else {
                    if (user.info.dateLog != null) onlineToolbar.setText(convertDateForEnter(user.info.dateLog, user.info.gender));
                    onlineToolbar.setTextColor(getResources().getColor(R.color.main_white_color_hello));
                }

                //Значек загрузки аватарки и подсветка пункта меню
                if (isUserProfile) {
                    iconForAva.setVisibility(View.VISIBLE);
                    setMenuItem("MyProfile");
                }
                else iconForAva.setVisibility(View.GONE);

                //Устанавливаем аватар
                DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
                width = displaymetrics.widthPixels;
                ProfileActivity.this.avatar.setMinimumWidth(width);
                ProfileActivity.this.avatar.setMinimumHeight(width);
                // TODO: 13.08.2017 Сделать проверку наличия пути
                if (user.info.avatar != null) {
                    Picasso.with(getApplicationContext())
                            .load(Uri.parse(Constant.upload + user.info.avatar.normal))
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
                if(user.info.photos != null) {
                    GV.setVisibility(View.VISIBLE);
                    final ArrayList<com.httpso_hello.hello.Structures.Photo> defolt = new ArrayList<com.httpso_hello.hello.Structures.Photo>();
                    Collections.addAll(defolt, user.info.photos);
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
                            ArrayList<Integer> photoComments = new ArrayList<Integer>();
                            for (int j = 0; j < user.info.photos.length; j++){
                                photoOrig.add(j, Constant.upload + user.info.photos[j].image.original);
                                photoLike.add(j, user.info.photos[j].rating);
                                photoId.add(j, user.info.photos[j].id);
                                photoIsVoted.add(j, Boolean.toString(user.info.photos[j].is_voted));
                                photoComments.add(j, user.info.photos[j].comments);
                            }
                            Intent intent;

                            if(isUserProfile){
                                intent = new Intent(ProfileActivity.this, FullscreenPhotoActivityUser.class);
                            } else {
                                intent = new Intent(ProfileActivity.this, FullscreenPhotoActivity.class);
                            }

                            intent.putStringArrayListExtra("photoOrig", photoOrig);
                            intent.putIntegerArrayListExtra("photoLike", photoLike);
                            intent.putIntegerArrayListExtra("photoId", photoId);
                            intent.putStringArrayListExtra("photoIsVoted", photoIsVoted);
                            intent.putIntegerArrayListExtra("photoComments", photoComments);
                            intent.putExtra("likeble", true);
                            intent.putExtra("position", position);
                            startActivity(intent);
                        }
                    });
                } else {
                    GV.setVisibility(View.GONE);
                }

                //Устанавливаем город
                if (user.info.city_cache != null) cityCache.setText(user.info.city_cache);
                //Устанавливаем телефон
                if(user.info.phone != null) {
                    phoneNumProfileConteiner.setVisibility(View.VISIBLE);
                    phoneNumProfile.setText(user.info.phone);
                } else phoneNumProfileConteiner.setVisibility(View.GONE);

                //Устанавливаем скайп
                if(user.info.skype != null) {
                    skypeProfileConteiner.setVisibility(View.VISIBLE);
                    skypeProfile.setText(user.info.skype);
                } else skypeProfileConteiner.setVisibility(View.GONE);

                //Материальное положение
                if ((user.info.matPoloz == 0) || (user.info.matPoloz == 1)) dohodProfileContener.setVisibility(View.GONE);
                if (user.info.matPoloz == 2) dohodProfile.setText("Нет дохода");
                if (user.info.matPoloz == 3) dohodProfile.setText("Низкий доход");
                if (user.info.matPoloz == 4) dohodProfile.setText("Стабильный средний доход");
                if (user.info.matPoloz == 5) dohodProfile.setText("Высокий доход");

                //Автомобиль
                if (user.info.avto != null) {
                    autoProfile.setText(user.info.avto);
                    autoProfileContener.setVisibility(View.VISIBLE);
                } else autoProfileContener.setVisibility(View.GONE);

                //Рост и вес
                if ((user.info.rost == 0) && (user.info.ves == 0)) rostAndVesProfileContener.setVisibility(View.GONE);
                else {
                    rostAndVesProfileContener.setVisibility(View.VISIBLE);
                    if (user.info.rost != 0) {
                        rostTitleProfile.setVisibility(View.VISIBLE);
                        rostProfile.setVisibility(View.VISIBLE);
                        rostProfile.setText(Integer.toString(user.info.rost) + " см");
                    } else {
                        rostTitleProfile.setVisibility(View.GONE);
                        rostProfile.setVisibility(View.GONE);
                    }
                    if (user.info.ves != 0) {
                        vesTitleProfile.setVisibility(View.VISIBLE);
                        vesProfile.setVisibility(View.VISIBLE);
                        vesProfile.setText(Integer.toString(user.info.ves) + " кг");
                    } else {
                        vesTitleProfile.setVisibility(View.GONE);
                        vesProfile.setVisibility(View.GONE);
                    }
                }

                //скрываем блок с друзьями
                if ((user.friends.length != 0) || (user.groups.length != 0)) friendsAndGiftsBlock.setVisibility(View.VISIBLE);
                else friendsAndGiftsBlock.setVisibility(View.GONE);

                //Кол-во друзей
                if(user.friends.length != 0) {
                    friendsCount.setText("Друзья");
                } else {
                    friendsCount.setText("Нет друзей");
                }
                if (user.friends.length > 0) {
                    friendsBlock.setVisibility(View.VISIBLE);
                    friend1.setVisibility(View.VISIBLE);
                    friendsLayout1.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[0].user.avatar.micro)
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend1);
                } else friendsBlock.setVisibility(View.GONE);
                if (user.friends.length > 1) {
                    friend2.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[1].user.avatar.micro)
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend2);
                }
                if (user.friends.length > 2) {
                    friend3.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[2].user.avatar.micro)
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend3);
                }
                if (user.friends.length > 3) {
                    friend4.setVisibility(View.VISIBLE);
                    friendsLayout2.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[3].user.avatar.micro)
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend4);
                }
                if (user.friends.length > 4) {
                    friend5.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[4].user.avatar.micro)
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend5);
                }
                if (user.friends.length > 5) {
                    friend6.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext())
                            .load(Constant.upload + user.friends[5].user.avatar.micro)
                            .transform(new CircularTransformation(0))
                            .error(R.mipmap.avatar)
                            .into(friend6);
                }

                //Кол-во пабликов
                if(user.groups.length != 0) {
                    groupsCount.setText("Сообщества");
                } else {
                    groupsCount.setText("Нет сообществ");
                }
                if (user.groups.length > 0) {
                    groupsBlock.setVisibility(View.VISIBLE);
                    groups1.setVisibility(View.VISIBLE);
                    groupsLayout1.setVisibility(View.VISIBLE);
                    if (user.groups[0].logo != null) {
                        Picasso.with(getApplicationContext())
                                .load(Constant.upload + user.groups[0].logo.micro)
                                .transform(new CircularTransformation(0))
                                .error(R.mipmap.avatar)
                                .into(groups1);
                    }
                } else groupsBlock.setVisibility(View.GONE);
                if (user.groups.length > 1) {
                    groups2.setVisibility(View.VISIBLE);
                    if (user.groups[1].logo != null) {
                        Picasso.with(getApplicationContext())
                                .load(Constant.upload + user.groups[1].logo.micro)
                                .transform(new CircularTransformation(0))
                                .error(R.mipmap.avatar)
                                .into(groups2);
                    }
                }
                if (user.groups.length > 2) {
                    groups3.setVisibility(View.VISIBLE);
                    if (user.groups[2].logo != null) {
                        Picasso.with(getApplicationContext())
                                .load(Constant.upload + user.groups[2].logo.micro)
                                .transform(new CircularTransformation(0))
                                .error(R.mipmap.avatar)
                                .into(groups3);
                    }
                }
                if (user.groups.length > 3) {
                    groups4.setVisibility(View.VISIBLE);
                    groupsLayout2.setVisibility(View.VISIBLE);
                    if (user.groups[3].logo != null) {
                        Picasso.with(getApplicationContext())
                                .load(Constant.upload + user.groups[3].logo.micro)
                                .transform(new CircularTransformation(0))
                                .error(R.mipmap.avatar)
                                .into(groups4);
                    }
                }
                if (user.groups.length > 4) {
                    groups5.setVisibility(View.VISIBLE);
                    if (user.groups[4].logo != null) {
                        Picasso.with(getApplicationContext())
                                .load(Constant.upload + user.groups[4].logo.micro)
                                .transform(new CircularTransformation(0))
                                .error(R.mipmap.avatar)
                                .into(groups5);
                    }
                }
                if (user.groups.length > 5) {
                    groups6.setVisibility(View.VISIBLE);
                    if (user.groups[5].logo != null) {
                        Picasso.with(getApplicationContext())
                                .load(Constant.upload + user.groups[5].logo.micro)
                                .transform(new CircularTransformation(0))
                                .error(R.mipmap.avatar)
                                .into(groups6);
                    }
                }

                //Кол-во подарков
                if (user.gifts != null) {
                    if (user.gifts.length != 0) {
                        String giftsStr = Integer.toString(user.gifts.length);
                        if (user.gifts.length == 1) giftsCount.setText(giftsStr + " подарок");
                        else if ((user.gifts.length >= 2) && (user.gifts.length < 5))
                            giftsCount.setText(giftsStr + " подарка");
                        else if ((user.gifts.length >= 5) && (user.gifts.length < 21))
                            giftsCount.setText(giftsStr + " подарков");
                        else if ((user.gifts.length >= 21) && (user.gifts.length % 10 == 1))
                            giftsCount.setText(giftsStr + " подарок");
                        else if ((user.gifts.length >= 21) && (user.gifts.length % 10 == 2))
                            giftsCount.setText(giftsStr + " подарка");
                        else if ((user.gifts.length >= 21) && (user.gifts.length % 10 == 3))
                            giftsCount.setText(giftsStr + " подарка");
                        else if ((user.gifts.length >= 21) && (user.gifts.length % 10 == 4))
                            giftsCount.setText(giftsStr + " подарка");
                        else giftsCount.setText(giftsStr + " подарков");
                    } else {
                        giftsCount.setText("Нет подарков");
                    }
                }

                ArrayList<GiftItem> defoltNew = new ArrayList<>();
                Collections.addAll(defoltNew, user.gifts);
                GiftsInProfileAdapter giftsInProfileAdapter = new GiftsInProfileAdapter(ProfileActivity.this, defoltNew);
                float llColumnWidth = (defoltNew.size() + 1) * 60 * displaymetrics.density;
                float llHorizontalSpacing = (defoltNew.size()) * 4 * displaymetrics.density;
                float paddingLeftAndRight = 20 * displaymetrics.density;
                ((LinearLayout) findViewById(R.id.giftsLL)).getLayoutParams().width = (int) (llColumnWidth + llHorizontalSpacing + paddingLeftAndRight);
                ((LinearLayout) findViewById(R.id.giftsLL)).requestLayout();
                giftsGrid.setNumColumns(defoltNew.size() + 1);
                giftsGrid.setAdapter(giftsInProfileAdapter);
                giftsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            Intent intent = new Intent(ProfileActivity.this, GiftsUserActivity.class);
                            intent.putExtra("user_id", user.info.id);
                            intent.putExtra("isUserProfile", isUserProfile);
                            startActivity(intent);
                        } else {
                            popUpWindowSendGift.showAtLocation(profile_content2, Gravity.CENTER, 0, 0);
                            Gifts.getInstance(getApplicationContext())
                                    .getGifts(0, new Gifts.GetGiftsCallback() {
                                        @Override
                                        public void onSuccess(GiftItem[] gi) {
                                            ((LinearLayout) popupViewSendGift).findViewById(R.id.waiting).setVisibility(View.GONE);
                                            ListView lvPopup = (ListView) popupViewSendGift.findViewById(R.id.list_gifts_in_popup);
                                            final ArrayList<GiftItem> list = new ArrayList<>();
                                            Collections.addAll(list, gi);
                                            GiftsGetGiftsAdapter ggga = new GiftsGetGiftsAdapter(ProfileActivity.this, list);
                                            lvPopup.setAdapter(ggga);
                                            lvPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    popUpWindowSendGift.dismiss();
                                                    Intent intent = new Intent(ProfileActivity.this, SendGiftActivity.class);
                                                    intent.putExtra("id", list.get(position).gift.id);
                                                    intent.putExtra("photo", list.get(position).gift.photo.small);
                                                    intent.putExtra("price", list.get(position).gift.price);
                                                    intent.putExtra("user_id", user.info.id);
                                                    startActivity(intent);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(int error_code, String error_msg) {
                                            popUpWindowSendGift.dismiss();
                                            Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onInternetError() {
                                            popUpWindowSendGift.dismiss();
                                            Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });

                //Скрыть блок "Ищу", если нет никакой инфы
                if ((user.info.lookingFor == 0) && (user.info.ageStart == 0) && (user.info.ageFinish == 0) && (user.info.regCel == null)) lookinForBar.setVisibility(View.GONE);
                else lookinForBar.setVisibility(View.VISIBLE);

                //Скрываем полосочку в блоке Ищу
                if ((user.info.ageStart == 0) && (user.info.lookingFor == 0) && (user.info.ageFinish == 0)) findView.setVisibility(View.GONE);
                else findView.setVisibility(View.VISIBLE);

                //С кем хочет познакомиться
                looking_for.setVisibility(View.VISIBLE);
                if (user.info.lookingFor == 1) {
                    looking_for.setText("Познакомлюсь с парнем");
                } else if (user.info.lookingFor == 2) {
                    looking_for.setText("Познакомлюсь с девушкой");
                } else if (user.info.lookingFor == 0) {
                    looking_for.setText("Познакомлюсь со всеми");
                }

                //Диапазон возраста для поиска
                if ((user.info.ageStart != 0) && (user.info.ageFinish != 0)) {
                    ageLookinFor.setVisibility(View.VISIBLE);
                    ageLookinFor.setText("Возраст от " + Integer.toString(user.info.ageStart) + " до " + Integer.toString(user.info.ageFinish) + " лет");
                }
                else if (user.info.ageStart != 0) {
                    ageLookinFor.setVisibility(View.VISIBLE);
                    ageLookinFor.setText("Возраст от " + Integer.toString(user.info.ageStart) + " лет");
                }
                else if (user.info.ageFinish != 0) {
                    ageLookinFor.setVisibility(View.VISIBLE);
                    ageLookinFor.setText("Возраст до " + Integer.toString(user.info.ageFinish) + " лет");
                }
                else ageLookinFor.setVisibility(View.GONE);

                //Цель знакомства
                if (user.info.regCel != null) {
                    celZnakomstvaProfileLL.setVisibility(View.VISIBLE);
                    char[] cel = user.info.regCel.toCharArray();
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
                if ( (user.info.music != null) ||
                        (user.info.movies != null) ||
                        (user.info.books != null) ||
                        (user.info.games != null) ||
                        (user.info.interes != null)
                        ) anketaBar.setVisibility(View.VISIBLE);
                else anketaBar.setVisibility(View.GONE);

                //Любимая музыка
                if (user.info.music != null) {
                    musicProfile.setText(user.info.music);
                    musicProfileContener.setVisibility(View.VISIBLE);
                } else musicProfileContener.setVisibility(View.GONE);
                //Любимые фильмы
                if (user.info.movies != null) {
                    moviesProfile.setText(user.info.movies);
                    moviesProfileContener.setVisibility(View.VISIBLE);
                } else moviesProfileContener.setVisibility(View.GONE);

                //Любимые книги
                if (user.info.books != null) {
                    booksProfile.setText(user.info.books);
                    booksProfileContener.setVisibility(View.VISIBLE);
                } else booksProfileContener.setVisibility(View.GONE);

                //Любимые игры
                if (user.info.games != null) {
                    gamesProfile.setText(user.info.games);
                    gamesProfileContener.setVisibility(View.VISIBLE);
                } else gamesProfileContener.setVisibility(View.GONE);

                //Другие интересы
                if (user.info.interes != null) {
                    intrestingProfile.setText(user.info.interes);
                    intrestingProfileContener.setVisibility(View.VISIBLE);
                } else intrestingProfileContener.setVisibility(View.GONE);

                //Скрыть кнопок "Написать сообщение" и "Добавить в друзья" если профиль юзера
                if (isUserProfile) msgAndFrndBtn.setVisibility(View.GONE);
                else msgAndFrndBtn.setVisibility(View.VISIBLE);

                //Кнопка отправки симпатии, скрыть если профиль юзера или если уже отправлял
                if((isUserProfile) || (user.simpationState == 4)) fab.setVisibility(View.GONE);
                else fab.setVisibility(View.VISIBLE);

                if (user.simpationState == 0) { // никто никому не отправлял симпатию
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            progressBarFlirtik.setVisibility(View.VISIBLE);
                            Simpation.getInstance(getApplicationContext()).sendFlirtik(user.info.id, new Simpation.SendSimpationCallback() {
                                @Override
                                public void onSuccess(String response) {
                                    progressBarFlirtik.setVisibility(View.GONE);
                                    Snackbar
                                            .make(view, "Симпатия отправлена", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }
                            }, new Help.ErrorCallback() {
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
                } else if (user.simpationState == 1) { // пользователь отправил просматриеваемому пользователю симпатию
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            Snackbar.make(view, "Пользователь уже получал вашу симпатию", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                        }
                    });
                } else if (user.simpationState == 2) { //просматриваемый пользователь отправил пользователю симпатию
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            progressBarFlirtik.setVisibility(View.VISIBLE);
                            Simpation.getInstance(getApplicationContext()).sendFlirtik(user.info.id, new Simpation.SendSimpationCallback() {
                                @Override
                                public void onSuccess(String response) {
                                    progressBarFlirtik.setVisibility(View.GONE);
                                    fab.setImageResource(R.drawable.ic_action_two_heart);
                                    Snackbar
                                            .make(view, "Ваша симпатия взаимна", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }
                            }, new Help.ErrorCallback() {
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
                } else if (user.simpationState == 3) { //симпатия взаимна
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
                            photoOrig.add(0, Constant.upload + user.info.avatar.original);
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

                        // Открыть аву
                        popUpWindow.getContentView().findViewById(R.id.avaOpen).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUpWindow.dismiss();
                                ArrayList<String> photoOrig = new ArrayList<String>();
                                photoOrig.add(0, Constant.upload + user.info.avatar.original);
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
                        intent.putExtra("profile_id", user.info.id);
                        startActivity(intent);
                    }
                });

                //Обработка нажатия кнопки "Сообщение"
                TextView writeMes = (TextView) findViewById(R.id.write_message);
                writeMes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
//                        intent.putExtra("contact_id", null);
                        intent.putExtra("nickname", user.info.nickname);
                        intent.putExtra("recipientId", user.info.id);
                        if(user.info.avatar != null){
                            intent.putExtra("avatar", user.info.avatar.micro);
                        } else {
                            intent.putExtra("avatar", Constant.default_avatar);
                        }
                        startActivity(intent);
                    }
                });

                //Обработка нажатия кнопки "Добавить в друзья"
                final TextView toFriends = (TextView) findViewById(R.id.to_friends);
                if (user.friendsState == 0) {
                    toFriends.setText("Добавить в друзья");
                    toFriends.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toFriends.setText("Отправляем заявку...");
                            Friend.getInstance(getApplicationContext()).addFriend(user.info.id, new Friend.AddFriendsCallback() {
                                @Override
                                public void onSuccess() {
                                    toFriends.setText("Заявка отправлена");
                                }

                                @Override
                                public void onError(int error_code, String error_msg) {
                                    toFriends.setText("Добавить в друзья");
                                    Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onInternetError() {
                                    toFriends.setText("Добавить в друзья");
                                    Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
                else if (user.friendsState == 3) {
                    toFriends.setText("Удалить из друзей");
                    toFriends.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toFriends.setText("Удаляем из друзей...");
                            Friend.getInstance(getApplicationContext()).deleteFriend(user.info.id, new Friend.DeleteFriendsCallback() {
                                @Override
                                public void onSuccess() {
                                    toFriends.setText("Удален из друзей");
                                }

                                @Override
                                public void onError(int error_code, String error_msg) {
                                    toFriends.setText("Удалить из друзей");
                                    Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onInternetError() {
                                    toFriends.setText("Удалить из друзей");
                                    Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                } else if (user.friendsState == 2) {
                    toFriends.setText("Принять заявку");
                    toFriends.setOnClickListener(new
                                                         View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View view) {
                                                                 toFriends.setText("Принимаем заявку...");
                                                                 Friend.getInstance(getApplicationContext()).addFriend(user.info.id, new Friend.AddFriendsCallback() {
                                                                     @Override
                                                                     public void onSuccess() {
                                                                         toFriends.setText("Добавлен в друзья");
                                                                     }

                                                                     @Override
                                                                     public void onError(int error_code, String error_msg) {
                                                                         toFriends.setText("Принять заявку");
                                                                         Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                                                     }

                                                                     @Override
                                                                     public void onInternetError() {
                                                                         toFriends.setText("Принять заявку");
                                                                         Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                                                     }
                                                                 });
                                                             }
                                                         });
                } else if (user.friendsState == 1) {
                    toFriends.setText("Заявка отправлена");
                }

                //Скрытия индикатора загрузки и открытие профиля
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
                profile_content.setVisibility(View.VISIBLE);
                profile_content2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(int error_code, String error_msg) {
                Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        setContent();
                    }
                }, 5000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!(stgs.getSettingInt("user_id") == profile_id)) {
            getMenuInflater().inflate(R.menu.profile, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ignor) {
            Messages ms = new Messages(getApplicationContext(), this);
            ms.ignorContact(profile_id, new Messages.IgnorContactCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(ProfileActivity.this, "Этот пользователь теперь у Вас в черном списке", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(int error_code, String error_msg) {
                    Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onInternetError() {
                    Toast.makeText(ProfileActivity.this, "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        } else if (id == R.id.action_user_link) {
            ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", "https://o-hello.com/users/" + Integer.toString(profile_id));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ProfileActivity.this, "Ссылка скопирована в буфер обмена", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Обрабатываем результат выбора фоток из галереи или с камеры
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
//загрузка нового фото из галереи в ленту
            case 1:
                if (resultCode == RESULT_OK) {
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
                    imageUri = Uri.fromFile(photoFile);
                    openAvatarUpdateWindow(sendPhotoClick);
                }
                break;
//загрузка новой аватарки из галереи
            case 3:
                if (resultCode == RESULT_OK) {
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
                    imageUri = Uri.fromFile(photoFile);
                    openAvatarUpdateWindow(updateAvatarClick);
                }
                break;
            default:
                System.out.println("Какая-то ошибка");
                break;
        }
    }

    public void openAvatarUpdateWindow(View.OnClickListener
                                               saveButtonClick){
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
                    .resize((int) (displaymetrics.widthPixels - displaymetrics.density*30), (int) (displaymetrics.widthPixels - displaymetrics.density*30))
                    .centerCrop()
                    .into(((ImageView) popUpWindow3.getContentView().findViewById(R.id.newPhoto)));
            ((TextView) popUpWindow3.getContentView().findViewById(R.id.cancelPhoto)).setOnClickListener(new View.OnClickListener() {
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
            popUpWindow3.dismiss();
            popUpWindow4.showAtLocation(profile_content2, Gravity.CENTER, 0, 0);


            final String base64_code_ava = Help.getBase64FromImage(
                    selectedImage,
                    Bitmap.CompressFormat.JPEG,
                    Help.getFileSize(imageUri, getApplicationContext()),
                    0
            );
            Profile.getInstance(getApplicationContext()).updateAvatar(
                    base64_code_ava,
                    "jpg",
                    new Profile.UpdateAvatarCallback() {
                        @Override
                        public void onSuccess(Image avatar) {
                            popUpWindow4.dismiss();
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            intent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                            startActivity(intent);
                            finish();
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {
                            popUpWindow4.dismiss();
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            intent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onInternetError() {
                            popUpWindow4.dismiss();
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            intent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                            startActivity(intent);
                            finish();
                        }
                    }
            );
        }
    };

    private View.OnClickListener sendPhotoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popUpWindow3.dismiss();
            popUpWindow4.showAtLocation(profile_content2, Gravity.CENTER, 0, 0);
            File defolt_file = new File(Help.getFileByUri(imageUri, getApplicationContext()));

            final String base64_code_ava = Help.getBase64FromImage(
                    selectedImage,
                    Bitmap.CompressFormat.JPEG,
                    Help.getFileSize(imageUri, getApplicationContext()),
                    0
            );
            Photo.getInstance(getApplicationContext()).addPhoto(
                    base64_code_ava,
                    "jpg",
                    new Photo.AddPhotoCallback() {
                        @Override
                        public void onSuccess() {
                            popUpWindow4.dismiss();
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            intent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                            startActivity(intent);
                            finish();
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {
                            popUpWindow4.dismiss();
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            intent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onInternetError() {
                            popUpWindow4.dismiss();
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            intent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                            startActivity(intent);
                            finish();
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
// Toast.makeText(this, "Для доступа к фото необходимо ваше разрешение", Toast.LENGTH_LONG).show();

                }
                break;
            case Help.REQUEST_UPDATE_AVATAR_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// Permission granted.
                    openCameraWindow(4);
                } else {
// User refused to grant permission.
// Toast.makeText(this, "Для доступа к фото необходимо ваше разрешение", Toast.LENGTH_LONG).show();
                }
                break;
            case Help.REQUEST_ADD_PHOTO_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// Permission granted.
                    openCameraWindow(2);
                } else {
// User refused to grant permission.
// Toast.makeText(this, "Для доступа к фото необходимо ваше разрешение", Toast.LENGTH_LONG).show();
                }
                break;
            case Help.REQUEST_ADD_PHOTO_GALLERY:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// Permission granted.
                    openAvatarUpdateWindow(sendPhotoClick);
                } else {
// User refused to grant permission.
// Toast.makeText(this, "Для доступа к фото необходимо ваше разрешение", Toast.LENGTH_LONG).show();

                }
                break;

        }

    }

    @Override
    public void onBackPressed() {
        if (popUpWindow.isShowing()) popUpWindow.dismiss();
        else if (popUpWindow2.isShowing()) popUpWindow2.dismiss();
        else if (popUpWindow3.isShowing()) popUpWindow3.dismiss();
        else if (popUpWindowSendGift.isShowing()) popUpWindowSendGift.dismiss();
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

    @Override
    public void onResume(){
        setContent();
        super.onResume();
    }

}