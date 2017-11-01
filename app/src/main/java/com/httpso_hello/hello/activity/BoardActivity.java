package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.adapters.BoardAdapter;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.HBoard;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Collections;

import static android.R.style.Animation_Dialog;

public class BoardActivity extends SuperMainActivity{

    private HBoard hBoard;
    private BoardAdapter bAdapter;
    private ListView LV;
    private View header;
    private View footerLoading;
    private int page = 1;
    private boolean loading = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View popupViewUser;
    private PopupWindow popUpWindowUser;
    private View popupViewOther;
    private PopupWindow popUpWindowOther;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        setHeader();
        setMenuItem("BoardActivity");

        LV = (ListView) findViewById(R.id.boardList);
        header = getLayoutInflater().inflate(R.layout.header_for_board, null);
        footerLoading = getLayoutInflater().inflate(R.layout.footer_loading, null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        popupViewUser = getLayoutInflater().inflate(R.layout.popup_for_board_user, null);
        popUpWindowUser = new PopupWindow(popupViewUser, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupViewOther = getLayoutInflater().inflate(R.layout.popup_for_board_other, null);
        popUpWindowOther = new PopupWindow(popupViewOther, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


//Заполнение контентом
        swipeRefreshLayout.setRefreshing(true);
        LV.addHeaderView(header);
        LV.addFooterView(footerLoading);
        getBoard();

//Кнопка "Добавить объявление"
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardActivity.this, AddBoardActivity.class);
                startActivity(intent);
            }
        });

// Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                page = 1;
                getBoard();
            }
        });
    }

    //Получаем объявления
    public void getBoard(){
        hBoard = new HBoard(getApplicationContext());
        hBoard.getBoard(this, page, new HBoard.GetBoardCallback() {
            @Override
            public void onSuccess(final BoardItem[] boardItems, Activity activity) {
                swipeRefreshLayout.setRefreshing(false);
                ArrayList<BoardItem> boardItem = new ArrayList<>();
                Collections.addAll(boardItem, boardItems);
                bAdapter = new BoardAdapter(activity, boardItem);
                LV.setAdapter(bAdapter);
                page += 1;
            }

            @Override
            public void onError(int error_code, String error_message) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getBoard();
                    }
                }, 5000);
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getBoard();
                    }
                }, 5000);
            }
        });
    }

    //Автоподгрузка объявлений
    public void getNew(){

        if (!loading) {
            YandexMetrica.getReporter(getApplicationContext(), Constant.metrika_api_key).reportEvent("get_new_board");
            loading = true;
            HBoard hBoard = new HBoard(getApplicationContext());
            hBoard.getBoard(this, page, new HBoard.GetBoardCallback() {
                @Override
                public void onSuccess(final BoardItem[] boardItems, Activity activity) {
                    loading = false;
                    ArrayList<BoardItem> boardItem = new ArrayList<>();
                    Collections.addAll(boardItem, boardItems);
                    bAdapter.add(boardItem);
                    bAdapter.notifyDataSetChanged();
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

    //Попапы
    public void showPopup(boolean isUserContent, final int userId, final String nickname, final Image avatar) {
        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
        if (isUserContent) {
            popUpWindowUser.setWidth(displaymetrics.widthPixels);
            popUpWindowUser.setHeight(displaymetrics.heightPixels);
            popUpWindowUser.setAnimationStyle(Animation_Dialog);
            popUpWindowUser.showAtLocation(LV, Gravity.CENTER, 0, 0);
            (popupViewUser.findViewById(R.id.write)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BoardActivity.this, ChatActivity.class);
                    intent.putExtra("contact_id", userId);
                    intent.putExtra("nickname", nickname);
                    if (avatar == null) {
                        intent.putExtra("avatar", Constant.default_avatar);
                    } else {
                        intent.putExtra("avatar", avatar.micro);
                    }
                    startActivity(intent);
                }
            });
            (popupViewUser.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //УДАЛЕНИЕ ОБЪЯВЛЕНИЯ
                    popUpWindowUser.dismiss();
                }
            });
        } else {
            popUpWindowOther.setWidth(displaymetrics.widthPixels);
            popUpWindowOther.setHeight(displaymetrics.heightPixels);
            popUpWindowOther.setAnimationStyle(Animation_Dialog);
            popUpWindowOther.showAtLocation(LV, Gravity.CENTER, 0, 0);
            (popupViewOther.findViewById(R.id.write)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BoardActivity.this, ChatActivity.class);
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

    @Override
    public void onPause(){
        popUpWindowUser.dismiss();
        popUpWindowOther.dismiss();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (popUpWindowUser.isShowing()) popUpWindowUser.dismiss();
        else if (popUpWindowOther.isShowing()) popUpWindowOther.dismiss();
        else super.onBackPressed();
    }
}