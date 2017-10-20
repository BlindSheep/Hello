package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.adapters.BoardAdapter;
import com.httpso_hello.hello.helper.HBoard;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Collections;

public class BoardActivity extends SuperMainActivity{

    private HBoard hBoard;
    private BoardAdapter bAdapter;
    private ListView LV;
    private View header;
    private View footerLoading;
    private int page = 1;
    private boolean loading = false;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_board);
        setHeader();

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


//Заполнение контентом
        swipeRefreshLayout.setRefreshing(true);
        LV.addHeaderView(header);
        LV.addFooterView(footerLoading);
        getBoard();
        YandexMetrica.activate(getApplicationContext(), "71dba453-2cd4-4256-b676-77d249383d44");
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
}