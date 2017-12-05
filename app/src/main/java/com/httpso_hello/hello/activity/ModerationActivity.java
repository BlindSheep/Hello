package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.adapters.BoardAdapter;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.HBoard;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Collections;

public class ModerationActivity extends AppCompatActivity {

    private int groupId;
    private HBoard hBoard;
    private BoardAdapter bAdapter;
    private ListView LV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionBar actionBar;
    private View header;
    private int page = 1;
    private boolean loading = false;
    private boolean thatsAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderation);

        groupId = getIntent().getExtras().getInt("id");
        LV = (ListView) findViewById(R.id.boardList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        actionBar = getSupportActionBar();
        header = getLayoutInflater().inflate(R.layout.footer6dp, null);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LV.addHeaderView(header);
        LV.addFooterView(header);
        getBoard();

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBoard();
            }
        });
    }

    //Получаем объявления
    public void getBoard(){
        page = 1;
        thatsAll = false;
        loading = false;
        swipeRefreshLayout.setRefreshing(true);
        hBoard = new HBoard(getApplicationContext());
        YandexMetrica.getReporter(getApplicationContext(), Constant.metrika_api_key).reportEvent("get_new_board");
        hBoard.getBoard(this, groupId, page, 1, new HBoard.GetBoardCallback() {
            @Override
            public void onSuccess(final BoardItem[] boardItems, Activity activity) {
                swipeRefreshLayout.setRefreshing(false);
                page = page + 1;
                ArrayList<BoardItem> boardItem = new ArrayList<>();
                Collections.addAll(boardItem, boardItems);
                bAdapter = new BoardAdapter(activity, true, boardItem, 2);
                LV.setAdapter(bAdapter);
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

    public void getNew () {
        if (!thatsAll && !loading) {
            swipeRefreshLayout.setRefreshing(true);
            loading = true;
            HBoard hBoard = new HBoard(getApplicationContext());
            hBoard.getBoard(this, groupId, page, 1, new HBoard.GetBoardCallback() {
                @Override
                public void onSuccess(final BoardItem[] boardItems, Activity activity) {
                    if (boardItems.length == 0) {
                        thatsAll = true;
                    } else {
                        ArrayList<BoardItem> boardItem = new ArrayList<>();
                        Collections.addAll(boardItem, boardItems);
                        bAdapter.add(boardItem);
                        bAdapter.notifyDataSetChanged();
                        page += 1;
                    }
                    loading = false;
                    swipeRefreshLayout.setRefreshing(false);
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

    //Кнопка назад(переход на страницу входа)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }
}
