package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Attachment;
import com.httpso_hello.hello.Structures.Coment;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.Structures.Photo;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.adapters.CommentsAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Comments;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Messages;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class BoardContentActivity extends SuperMainActivity {

    private Bundle extras;
    private ListView LV;
    private View header;
    private View footer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView boardLikeItem;
    private ImageView userAvatarBoardItem;
    private TextView userNameBoardItem;
    private TextView datePubBoardItem;
    private TextView boardTextItem;
    private EmojIconActions emojIcon;
    private ImageButton emojiKeyboard;
    private EmojiconEditText messageContent;
    private RelativeLayout contentBoardContentBlock;
    private ImageView messageSend;
    private CommentsAdapter ca;
    private static int counts;
    private static Handler handler = new Handler();
    private Timer timer = new Timer();
    private boolean launching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();

        setContentView(R.layout.activity_board_content);
        setHeader();

        LV = (ListView) findViewById(R.id.commentsList);
        header = getLayoutInflater().inflate(R.layout.content_board_header, null);
        footer = getLayoutInflater().inflate(R.layout.footer, null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        boardLikeItem = (TextView) header.findViewById(R.id.boardLikeItem);
        userAvatarBoardItem = (ImageView) header.findViewById(R.id.userAvatarBoardItem);
        userNameBoardItem = (TextView) header.findViewById(R.id.userNameBoardItem);
        datePubBoardItem = (TextView) header.findViewById(R.id.datePubBoardItem);
        boardTextItem = (TextView) header.findViewById(R.id.boardTextItem);
        contentBoardContentBlock = (RelativeLayout) findViewById(R.id.content_board_content_block);
        messageContent = (EmojiconEditText) findViewById(R.id.messageContent);
        emojiKeyboard = (ImageButton) findViewById(R.id.emojiKeyboard);
        emojIcon = new EmojIconActions(this, contentBoardContentBlock, messageContent, emojiKeyboard);
        emojIcon.ShowEmojIcon();
        messageSend = (ImageView) findViewById(R.id.messageSend);

        //Заполняем комментами
        getComments();
        LV.addHeaderView(header);
        LV.addFooterView(footer);
        getBoardItem(extras.getString("avatar"), extras.getString("user_nickname"), extras.getString("date_pub"), extras.getString("content"), extras.getInt("likes"), extras.getInt("id"), extras.getBoolean("anonim"));

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComments();
            }
        });

        //Клик по шапке
        if (!extras.getBoolean("anonim")) {
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BoardContentActivity.this, ProfileActivity.class);
                    intent.putExtra("profile_id", extras.getInt("user_id"));
                    startActivity(intent);
                }
            });
        } else {
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        //Отправка комментария
        messageSend.setOnClickListener(getOnClick());
    }


    //Отправка комментария
    private View.OnClickListener getOnClick() {
        return  new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String messageContentString = messageContent.getText().toString();
                if (!messageContentString.isEmpty()) {
                    messageContent.setText(null);

                    Coment coment = new Coment();
                    User user = new User();
                    Image avatar = new Image();

                    coment.content = messageContentString ;
                    coment.date_pub = "Только что";
                    coment.user_id = stgs.getSettingInt("user_id");
                    user.is_online = true;
                    user.nickname = stgs.getSettingStr("user_nickname");
                    avatar.micro = stgs.getSettingStr("user_avatar.micro");

                    coment.user = user;
                    user.avatar = avatar;
                    ca.add(coment);
                    ca.notifyDataSetChanged();

                    new Comments(getApplicationContext())
                            .sendComments("content",
                                    "board",
                                    extras.getInt("id"),
                                    messageContentString,
                                    new Comments.SendCommentsCallback() {
                                        @Override
                                        public void onSuccess(boolean response) {

                                        }

                                        @Override
                                        public void onError(int error_code, String error_message) {

                                        }

                                        @Override
                                        public void onInternetError() {

                                        }
                                    });
                }
            }
        };
    }

    //Получение комментов
    private void getComments(){
        launching = true;
        swipeRefreshLayout.setRefreshing(true);
        new Comments(getApplicationContext())
                .getComments(
                        "content",
                        "board",
                        extras.getInt("id"),
                        this,
                        new Comments.GetCommentsCallback() {
            @Override
            public void onSuccess(Coment[] commentsStructure, Activity activity) {
                final ArrayList<Coment> defolt = new ArrayList<Coment>();
                Collections.addAll(defolt, commentsStructure);
                ca = new CommentsAdapter(activity, defolt);
                counts = defolt.size();
                LV.setAdapter(ca);
                launching = false;
                LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(BoardContentActivity.this, ProfileActivity.class);
                        intent.putExtra("profile_id", defolt.get(position - 1).user_id);
                        startActivity(intent);
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int error_code, String error_message) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getComments();
                    }
                }, 5000);
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getComments();
                    }
                }, 5000);
            }
        });
    }

    //Заполнение карточку объявления
    private void getBoardItem(String avatar, String name, String date_pub, String content, int likes, final int id, boolean anonim) {
        if (!anonim) {
            if (avatar != null) {
                Picasso
                        .with(getApplicationContext())
                        .load(Uri.parse(Constant.upload + avatar))
                        .transform(new CircularTransformation(0))
                        .into(userAvatarBoardItem, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso
                                        .with(getApplicationContext())
                                        .load(Uri.parse(Constant.default_avatar))
                                        .transform(new CircularTransformation(0))
                                        .into(userAvatarBoardItem);
                            }
                        });
            } else {
                Picasso
                        .with(getApplicationContext())
                        .load(Uri.parse(Constant.default_avatar))
                        .transform(new CircularTransformation(0))
                        .into(userAvatarBoardItem);
            }
            userNameBoardItem.setText(name);
            datePubBoardItem.setText(date_pub);
            boardTextItem.setText(Html.fromHtml(content));
            boardLikeItem.setText(ConverterDate.likeStr(likes));
            boardLikeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BoardContentActivity.this, LikeActivity.class);
                    intent.putExtra("target_id", id);
                    intent.putExtra("subject", "board");
                    intent.putExtra("target_controller", "content");
                    startActivity(intent);
                }
            });
        } else {
            Picasso
                    .with(getApplicationContext())
                    .load(R.drawable.ic_action_anonimnost)
                    .transform(new CircularTransformation(0))
                    .into(userAvatarBoardItem);
            userNameBoardItem.setText("Анонимно");
            datePubBoardItem.setText(date_pub);
            boardTextItem.setText(Html.fromHtml(content));
            boardLikeItem.setText(ConverterDate.likeStr(likes));
            boardLikeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BoardContentActivity.this, LikeActivity.class);
                    intent.putExtra("target_id", id);
                    intent.putExtra("subject", "board");
                    intent.putExtra("target_controller", "content");
                    startActivity(intent);
                }
            });
        }
    }


    //Проверка новых комментов
    private void getCountsComments() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {public void run() {
                    if (!launching) {
                        new Comments(getApplicationContext())
                                .getCountsComments("content",
                                        "board",
                                        extras.getInt("id"),
                                        new Comments.GetCountsCommentsCallback() {
                                            @Override
                                            public void onSuccess(int count_comments) {
                                                if (counts != count_comments) {
                                                    getComments();
                                                }
                                            }
                                        });
                    }
                }});
            }
        }, 1000, 10000);
    }

    @Override
    public void onResume() {
        getCountsComments();
        super.onResume();
    }

    @Override
    public void onPause() {
        timer.cancel();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}