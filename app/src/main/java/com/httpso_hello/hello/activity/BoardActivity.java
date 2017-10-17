package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.adapters.BoardAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.HBoard;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Messages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class BoardActivity extends SuperMainActivity{

    private HBoard hBoard;
    private BoardAdapter bAdapter;
    private ProgressBar progressBarBoard;
    private ListView LV;
    private View header;
    private View footerLoading;
    private int page = 1;
    private boolean loading = false;
    private static Handler Tread1_Handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_board);
        progressBarBoard = (ProgressBar) findViewById(R.id.progressBarBoard);
        LV = (ListView) findViewById(R.id.boardList);
        header = getLayoutInflater().inflate(R.layout.header_for_board, null);
        footerLoading = getLayoutInflater().inflate(R.layout.footer_loading, null);

        LV.addHeaderView(header);
        LV.addFooterView(footerLoading);

        getBoard();

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardActivity.this, AddBoardActivity.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//Во все активности перенести, заполнение шапки в меню
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardActivity.this, ProfileActivity.class);
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
        user_id_header.setText("Ваш ID " +
                Integer.toString(stgs.getSettingInt("user_id")));
    }

    public void getBoard(){
        timer.cancel();
        hBoard = new HBoard(getApplicationContext());
        hBoard.getBoard(this, page, new HBoard.GetBoardCallback() {
            @Override
            public void onSuccess(final BoardItem[] boardItems, Activity activity) {
                ArrayList<BoardItem> boardItem = new ArrayList<>();
                Collections.addAll(boardItem, boardItems);
                progressBarBoard.setVisibility(View.GONE);
                bAdapter = new BoardAdapter(activity, boardItem);
                LV.setAdapter(bAdapter);
                page += 1;
            }

            @Override
            public void onError(int error_code, String error_message) {
            }

            @Override
            public void onInternetError() {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Tread1_Handler.post(new Runnable() {public void run() {
                            System.out.println("Таймер");
                            getBoard();
                        }});
                    }
                }, 3000, 3000);
            }
        });
    }

    public void getNew(){
        if (!loading) {
            timer.cancel();
            loading = true;
            HBoard hBoard = new HBoard(getApplicationContext());
            hBoard.getBoard(this, page, new HBoard.GetBoardCallback() {
                @Override
                public void onSuccess(final BoardItem[] boardItems, Activity activity) {
                    loading = false;
                    ArrayList<BoardItem> boardItem = new ArrayList<>();
                    Collections.addAll(boardItem, boardItems);
                    progressBarBoard.setVisibility(View.GONE);
                    bAdapter.add(boardItem);
                    bAdapter.notifyDataSetChanged();
                    page += 1;
                }

                @Override
                public void onError(int error_code, String error_message) {
                    loading = false;
                }

                @Override
                public void onInternetError() {
                    loading = false;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Tread1_Handler.post(new Runnable() {public void run() {
                                System.out.println("Таймер");
                                getNew();
                            }});
                        }
                    }, 3000, 3000);
                }
            });
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        timer.cancel();
    }
}