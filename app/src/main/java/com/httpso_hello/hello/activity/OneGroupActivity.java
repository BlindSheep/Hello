package com.httpso_hello.hello.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.Structures.Groups;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.adapters.BoardAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Content;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.HBoard;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Photo;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import static android.R.style.Animation_Dialog;

public class OneGroupActivity extends AppCompatActivity {

    private Settings stgs;
    private int groupId;
    private int page = 1;
    private ListView groupContent;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View header;
    private View footer;
    private ImageView logotip;
    private TextView name;
    private TextView descr;
    private TextView members;
    private TextView following;
    private LinearLayout adminBar;
    private LinearLayout moderation;
    private LinearLayout sendMsg;
    private ActionBar actionBar;
    private BoardAdapter groupContentAdapter;
    private LinearLayout adminLayout;
    private boolean isFollowing;
    private boolean loading = false;
    private boolean thatsAll = false;
    private LinearLayout membersLayout;
    private PopupWindow popUpWindow;
    private View popupView;
    private PopupWindow popUpWindow2;
    private View popupView2;
    private PopupWindow popUpWindow3;
    private View popupView3;
    private File photoFile;
    private Uri imageUri;
    private Bitmap selectedImage;
    private TextView countOfModerate;
    private LinearLayout isModer;
    private PopupWindow popUpWindowUser;
    private View popupViewUser;
    private View popupViewOther;
    private PopupWindow popUpWindowOther;
    private RelativeLayout parrentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_group);

        groupId = getIntent().getExtras().getInt("id");

        stgs = new Settings(getApplicationContext());
        groupContent = (ListView) findViewById(R.id.groupContent);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        header = getLayoutInflater().inflate(R.layout.header_for_group, null);
        footer = getLayoutInflater().inflate(R.layout.footer_loading, null);
        logotip = (ImageView) header.findViewById(R.id.logotip);
        name = (TextView) header.findViewById(R.id.name);
        descr = (TextView) header.findViewById(R.id.descr);
        members = (TextView) header.findViewById(R.id.members);
        following = (TextView) header.findViewById(R.id.following);
        adminBar = (LinearLayout) header.findViewById(R.id.adminBar);
        moderation = (LinearLayout) header.findViewById(R.id.moderation);
        sendMsg = (LinearLayout) header.findViewById(R.id.sendMsg);
        actionBar = getSupportActionBar();
        adminLayout = (LinearLayout) header.findViewById(R.id.adminLayout);
        membersLayout = (LinearLayout) header.findViewById(R.id.membersLayout);
        popupView = getLayoutInflater().inflate(R.layout.popup_for_avatar, null);
        popUpWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupView2 = getLayoutInflater().inflate(R.layout.popup_accept_new_photo, null);
        popUpWindow2 = new PopupWindow(popupView2, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupView3 = getLayoutInflater().inflate(R.layout.popup_for_wait, null);
        popUpWindow3 = new PopupWindow(popupView3, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupViewUser = getLayoutInflater().inflate(R.layout.popup_for_board_user, null);
        popUpWindowUser = new PopupWindow(popupViewUser, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupViewOther = getLayoutInflater().inflate(R.layout.popup_for_board_other, null);
        popUpWindowOther = new PopupWindow(popupViewOther, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        countOfModerate = (TextView) header.findViewById(R.id.countOfModerate);
        isModer = (LinearLayout) header.findViewById(R.id.isModer);
        ((TextView) popupView3.findViewById(R.id.textForWaiting)).setText("Загружаем...");
        parrentView = (RelativeLayout) findViewById(R.id.parrentView);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        groupContent.addHeaderView(header);
        groupContent.addFooterView(footer);
        getGroupHeader();
        getGroupContent();

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGroupHeader();
                getGroupContent();
            }
        });


    }

    private void getGroupHeader () {
        com.httpso_hello.hello.helper.Groups groups = new com.httpso_hello.hello.helper.Groups(getApplicationContext());
        groups.getGroups(2, groupId, null, new com.httpso_hello.hello.helper.Groups.GetGroupsCallback() {
            @Override
            public void onSuccess(Groups[] groupItems) {
                final Groups groupItem = groupItems[0];
                OneGroupActivity.this.isFollowing = groupItem.isFollowing;
                if(groupItem.logo != null) {
                    Picasso
                            .with(getApplicationContext())
                            .load(Uri.parse(Constant.upload + groupItem.logo.small))
                            .transform(new CircularTransformation(0))
                            .into(logotip, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso
                                            .with(getApplicationContext())
                                            .load(Uri.parse(Constant.default_avatar))
                                            .transform(new CircularTransformation(0))
                                            .into(logotip);
                                }
                            });
                } else {
                    Picasso
                            .with(getApplicationContext())
                            .load(Uri.parse(Constant.default_avatar))
                            .transform(new CircularTransformation(0))
                            .into(logotip);
                }
                actionBar.setTitle(groupItem.title);
                name.setText(groupItem.title);
                descr.setText(groupItem.description);
                members.setText(ConverterDate.getFollowers(groupItem.members_count));
                membersLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OneGroupActivity.this, FollowersActivity.class);
                        intent.putExtra("id", groupId);
                        intent.putExtra("isAdmin", false);
                        startActivity(intent);
                    }
                });

                if (groupItem.moderate == 1) {
                    isModer.setVisibility(View.VISIBLE);
                } else {
                    isModer.setVisibility(View.GONE);
                }

                if (groupItem.owner_id == stgs.getSettingInt("user_id")) {
                    adminLayout.setVisibility(View.VISIBLE);
                    adminBar.setVisibility(View.VISIBLE);
                    adminBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(OneGroupActivity.this, SettingsOfGroupActivity.class);
                            intent.putExtra("id", groupId);
                            startActivity(intent);
                        }
                    });
                    if (groupItem.moderate == 1) {
                        moderation.setVisibility(View.VISIBLE);
                        moderation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(OneGroupActivity.this, ModerationActivity.class);
                                intent.putExtra("id", groupId);
                                startActivity(intent);
                            }
                        });
                        if (groupItem.count_of_moderate != 0) {
                            countOfModerate.setVisibility(View.VISIBLE);
                            countOfModerate.setText(Integer.toString(groupItem.count_of_moderate));
                        } else {
                            countOfModerate.setVisibility(View.GONE);
                            countOfModerate.setText(Integer.toString(groupItem.count_of_moderate));
                        }
                    } else {
                        moderation.setVisibility(View.GONE);
                    }
                    following.setVisibility(View.GONE);
                    logotip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
                            popUpWindow.setWidth(displaymetrics.widthPixels);
                            popUpWindow.setHeight(displaymetrics.heightPixels);
                            popUpWindow.setAnimationStyle(Animation_Dialog);
                            popUpWindow.showAtLocation(parrentView, Gravity.CENTER, 0, 0);
                            // Открыть лого
                            popUpWindow.getContentView().findViewById(R.id.avaOpen).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popUpWindow.dismiss();
                                    ArrayList<String> photoOrig = new ArrayList<String>();
                                    photoOrig.add(0, Constant.upload + groupItem.logo.original);
                                    Intent intent = new Intent(OneGroupActivity.this, FullscreenPhotoActivity.class);
                                    intent.putStringArrayListExtra("photoOrig", photoOrig);
                                    intent.putExtra("likeble", false);
                                    intent.putExtra("position", 0);
                                    startActivity(intent);
                                }
                            });

                            // Загрузить лого из галереи
                            popUpWindow.getContentView().findViewById(R.id.avaFromGalery).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popUpWindow.dismiss();
                                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                    photoPickerIntent.setType("image/*");
                                    startActivityForResult(photoPickerIntent, 1);
                                }
                            });

                            // Загрузить лого с камеры
                            popUpWindow.getContentView().findViewById(R.id.avaFromCamera).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popUpWindow.dismiss();
                                    if(Help.runTaskAfterPermission(
                                            OneGroupActivity.this,
                                            new String[] {
                                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.CAMERA
                                            },
                                            Help.REQUEST_UPDATE_AVATAR_CAMERA

                                    )) {
                                        openCameraWindow(2);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    adminLayout.setVisibility(View.GONE);
                    adminBar.setVisibility(View.GONE);
                    moderation.setVisibility(View.GONE);
                    if (groupItem.isFollowing) {
                        following.setVisibility(View.VISIBLE);
                        following.setText("Отписаться");
                        following.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setSubscribe();
                            }
                        });
                    } else {
                        following.setVisibility(View.VISIBLE);
                        following.setText("Подписаться");
                        following.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setSubscribe();
                            }
                        });
                    }
                    logotip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<String> photoOrig = new ArrayList<String>();
                            photoOrig.add(0, Constant.upload + groupItem.logo.original);
                            Intent intent = new Intent(OneGroupActivity.this, FullscreenPhotoActivity.class);
                            intent.putStringArrayListExtra("photoOrig", photoOrig);
                            intent.putExtra("likeble", false);
                            intent.putExtra("position", 0);
                            startActivity(intent);
                        }
                    });
                }

                //Написать сообщение
                sendMsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OneGroupActivity.this, AddBoardActivity.class);
                        intent.putExtra("groupId", groupItem.id);
                        if (groupItem.moderate == 1) intent.putExtra("isModeratble", true);
                        else intent.putExtra("isModeratble", false);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(int error_code, String error_msg) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getGroupHeader();
                    }
                }, 5000);
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getGroupHeader();
                    }
                }, 5000);
            }
        });
    }

    //Получение контента - 1 страница
    private void getGroupContent () {
        swipeRefreshLayout.setRefreshing(true);
        page = 1;
        loading = true;
        thatsAll = false;
        if (groupContent.getFooterViewsCount() == 0) groupContent.addFooterView(footer);
        HBoard hd = new HBoard(getApplicationContext());
        hd.getBoard(OneGroupActivity.this, groupId, page, 0, new HBoard.GetBoardCallback() {
            @Override
            public void onSuccess(BoardItem[] boardItems, Activity activity) {
                page += 1;
                ArrayList<BoardItem> result = new ArrayList<BoardItem>();
                Collections.addAll(result, boardItems);
                groupContentAdapter = new BoardAdapter(OneGroupActivity.this, false, result, 1);
                groupContent.setAdapter(groupContentAdapter);
                if (boardItems.length < 10) {
                    thatsAll = true;
                    groupContent.removeFooterView(footer);
                }
                swipeRefreshLayout.setRefreshing(false);
                loading = false;
            }

            @Override
            public void onError(int error_code, String error_message) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getGroupContent();
                    }
                }, 5000);
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getGroupContent();
                    }
                }, 5000);
            }
        });
    }

    //Автоподгрузка контента
    public void getNew(){
        if (!thatsAll && !loading) {
            loading = true;
            HBoard hBoard = new HBoard(getApplicationContext());
            hBoard.getBoard(this, groupId, page, 0, new HBoard.GetBoardCallback() {
                @Override
                public void onSuccess(final BoardItem[] boardItems, Activity activity) {
                    loading = false;
                    if (boardItems.length != 0) {
                        ArrayList<BoardItem> boardItem = new ArrayList<>();
                        Collections.addAll(boardItem, boardItems);
                        groupContentAdapter.add(boardItem);
                        groupContentAdapter.notifyDataSetChanged();
                    } else {
                        thatsAll = true;
                        groupContent.removeFooterView(footer);
                    }
                    page += 1;
                }

                @Override
                public void onError(int error_code, String error_message) {
                    loading = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            getNew();
                        }
                    }, 5000);
                }

                @Override
                public void onInternetError() {
                    loading = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            getNew();
                        }
                    }, 5000);
                }
            });
        }
    }

    //Подписка / отписка
    private void setSubscribe() {
        swipeRefreshLayout.setRefreshing(true);
        com.httpso_hello.hello.helper.Groups groups = new com.httpso_hello.hello.helper.Groups(getApplicationContext());
        int action;
        if (isFollowing) action = 0;
        else action = 1;
        groups.subscribe(action, groupId, 0, new com.httpso_hello.hello.helper.Groups.GetSubscribeCallback() {
            @Override
            public void onSuccess() {
                if (isFollowing) Toast.makeText(getApplicationContext().getApplicationContext(), "Вы успешно отписались от сообщества", Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext().getApplicationContext(), "Вы успешно подписались на сообщество", Toast.LENGTH_LONG).show();
                getGroupHeader();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int error_code, String error_msg) {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onInternetError() {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Попапы
    public void showPopup(boolean isUserContent, final int userId, final String nickname, final Image avatar, final int boardId, final int group_id) {
        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
        if (isUserContent) {
            popUpWindowUser.setWidth(displaymetrics.widthPixels);
            popUpWindowUser.setHeight(displaymetrics.heightPixels);
            popUpWindowUser.setAnimationStyle(Animation_Dialog);
            popUpWindowUser.showAtLocation(parrentView, Gravity.CENTER, 0, 0);
            (popupViewUser.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeRefreshLayout.setRefreshing(true);
                    Content.getInstance(getApplicationContext())
                            .deleteContent(boardId, "board", group_id, new Content.DeleteContentCallback() {
                                @Override
                                public void onSuccess() {
                                    page = 1;
                                    getGroupContent();
                                    Toast.makeText(getApplicationContext(), "Сообщение удалено", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(int error_code, String error_msg) {
                                    swipeRefreshLayout.setRefreshing(false);
                                    Toast.makeText(getApplicationContext(), "Произошла какая-то ошибка, попробуйте позже.", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onInternetError() {
                                    swipeRefreshLayout.setRefreshing(false);
                                    Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                }
                            });
                    popUpWindowUser.dismiss();
                }
            });
        } else {
            popUpWindowOther.setWidth(displaymetrics.widthPixels);
            popUpWindowOther.setHeight(displaymetrics.heightPixels);
            popUpWindowOther.setAnimationStyle(Animation_Dialog);
            popUpWindowOther.showAtLocation(parrentView, Gravity.CENTER, 0, 0);
            (popupViewOther.findViewById(R.id.write)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OneGroupActivity.this, ChatActivity.class);
                    intent.putExtra("contact_id", userId);
                    intent.putExtra("nickname", nickname);
                    if (avatar == null) {
                        intent.putExtra("avatar", Constant.default_avatar);
                    } else {
                        intent.putExtra("avatar", avatar.micro);
                    }
                    startActivity(intent);
                    popUpWindowOther.dismiss();
                }
            });
            (popupViewOther.findViewById(R.id.badContent)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ПОЖАЛОВАТЬСЯ НА ОБЪЯВЛЕНИЕ
                    Toast.makeText(getApplicationContext(), "Жалоба отправлена", Toast.LENGTH_LONG).show();
                    popUpWindowOther.dismiss();
                }
            });
        }
    }

    //Обрабатываем результат выбора фоток из галереи или с камеры
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
//загрузка нового логотипа из галереи
            case 1:
                if (resultCode == RESULT_OK) {
                    imageUri = imageReturnedIntent.getData();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if(Help.runTaskAfterPermission(
                                OneGroupActivity.this,
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
//загрузка нового логотипа с камеры
            case 2:
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

            popUpWindow2.setWidth(displaymetrics.widthPixels);
            popUpWindow2.setHeight(displaymetrics.heightPixels);
            popUpWindow2.setAnimationStyle(Animation_Dialog);
            popUpWindow2.showAtLocation(parrentView, Gravity.CENTER, 0, 0);

            Picasso.with(getApplicationContext())
                    .load(imageUri)
                    .resize((int) (displaymetrics.widthPixels - displaymetrics.density*30), (int) (displaymetrics.widthPixels - displaymetrics.density*30))
                    .centerCrop()
                    .into(((ImageView) popUpWindow2.getContentView().findViewById(R.id.newPhoto)));
            ((TextView) popUpWindow2.getContentView().findViewById(R.id.cancelPhoto)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popUpWindow2.dismiss();
                }
            });

            ((TextView) popUpWindow2.getContentView().findViewById(R.id.savePhoto)).setOnClickListener(saveButtonClick);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener updateAvatarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popUpWindow2.dismiss();
            popUpWindow3.showAtLocation(parrentView, Gravity.CENTER, 0, 0);


            final String base64_code_ava = Help.getBase64FromImage(
                    selectedImage,
                    Bitmap.CompressFormat.JPEG,
                    Help.getFileSize(imageUri, getApplicationContext()),
                    0
            );
            com.httpso_hello.hello.helper.Groups.getInstance(getApplicationContext()).updateGroupAvatar(
                    base64_code_ava,
                    "jpg",
                    groupId,
                    new com.httpso_hello.hello.helper.Groups.UpdateAvatarCallback() {
                        @Override
                        public void onSuccess() {
                            popUpWindow3.dismiss();
                            getGroupHeader();
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {
                            popUpWindow3.dismiss();
                            getGroupHeader();
                        }

                        @Override
                        public void onInternetError() {
                            popUpWindow3.dismiss();
                            getGroupHeader();
                        }
                    }
            );
        }
    };

    private View.OnClickListener sendPhotoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popUpWindow2.dismiss();
            popUpWindow3.showAtLocation(parrentView, Gravity.CENTER, 0, 0);
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
                            popUpWindow3.dismiss();
                            getGroupHeader();
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {
                            popUpWindow3.dismiss();
                            getGroupHeader();
                        }

                        @Override
                        public void onInternetError() {
                            popUpWindow3.dismiss();
                            getGroupHeader();
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
                    openAvatarUpdateWindow(updateAvatarClick);
                } else {

                }
                break;
            case Help.REQUEST_UPDATE_AVATAR_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraWindow(4);
                } else {

                }
                break;
            case Help.REQUEST_ADD_PHOTO_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraWindow(2);
                } else {

                }
                break;
            case Help.REQUEST_ADD_PHOTO_GALLERY:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAvatarUpdateWindow(sendPhotoClick);
                } else {

                }
                break;
        }
    }

    public void openCameraWindow(int request_code){
        Intent photoCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoCameraIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try{
                photoFile = Help.createImageFile(OneGroupActivity.this);
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

    //Кнопка назад(переход на страницу всех сообществ)
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
    public void onBackPressed() {
        if (popUpWindow.isShowing()) popUpWindow.dismiss();
        else if (popUpWindow2.isShowing()) popUpWindow2.dismiss();
        else if (popUpWindow3.isShowing()) popUpWindow3.dismiss();
        else if (popUpWindowUser.isShowing()) popUpWindowUser.dismiss();
        else if (popUpWindowOther.isShowing()) popUpWindowOther.dismiss();
        else super.onBackPressed();
    }

    @Override
    public void onPause() {
        if (popUpWindow != null) popUpWindow.dismiss();
        if (popUpWindow2 != null) popUpWindow.dismiss();
        if (popUpWindow3 != null) popUpWindow.dismiss();
        if (popUpWindowUser != null) popUpWindowUser.dismiss();
        if (popUpWindowOther != null) popUpWindowOther.dismiss();
        super.onPause();
    }
}
