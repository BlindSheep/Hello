package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Coment;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.adapters.CommentsAdapter;
import com.httpso_hello.hello.helper.Comments;
import com.httpso_hello.hello.helper.ConverterDate;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.R.style.Animation_Dialog;

public class PhotoCommentsActivity extends SuperMainActivity {

    private Bundle extras;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView LV;
    private View header;
    private View footer;
    private boolean launching;
    private ImageView messageSend;
    private EmojiconEditText messageContent;
    private CommentsAdapter ca;
    private static int counts;
    private static Handler handler = new Handler();
    private Timer timer = new Timer();
    private ImageView photo;
    private TextView like;
    private EmojIconActions emojIcon;
    private ImageButton emojiKeyboard;
    private RelativeLayout content_photo_comments_block;
    private View popupViewDelete;
    private PopupWindow popUpWindowDelete;
    private View popupViewSend;
    private PopupWindow popUpWindowSend;
    private int idAnswer;
    private String nicknameAnswer;
    private TextView textAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_photo_comments);
        setHeader();

        LV = (ListView) findViewById(R.id.commentsList);
        header = getLayoutInflater().inflate(R.layout.content_photo_header, null);
        footer = getLayoutInflater().inflate(R.layout.footer, null);
        photo = (ImageView) header.findViewById(R.id.photoImageItem);
        like = (TextView) header.findViewById(R.id.photoLikeItem);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        messageSend = (ImageView) findViewById(R.id.messageSend);
        content_photo_comments_block = (RelativeLayout) findViewById(R.id.content_photo_comments_block);
        messageContent = (EmojiconEditText) findViewById(R.id.messageContent);
        emojiKeyboard = (ImageButton) findViewById(R.id.emojiKeyboard);
        emojIcon = new EmojIconActions(this, content_photo_comments_block, messageContent, emojiKeyboard);
        emojIcon.ShowEmojIcon();

        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
        popupViewDelete = getLayoutInflater().inflate(R.layout.popup_for_delete_comment, null);
        popUpWindowDelete = new PopupWindow(popupViewDelete, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpWindowDelete.setWidth(displaymetrics.widthPixels);
        popUpWindowDelete.setHeight(displaymetrics.heightPixels);
        popUpWindowDelete.setAnimationStyle(Animation_Dialog);
        popupViewSend = getLayoutInflater().inflate(R.layout.popup_for_wait, null);
        popUpWindowSend = new PopupWindow(popupViewSend, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpWindowSend.setWidth(displaymetrics.widthPixels);
        popUpWindowSend.setHeight(displaymetrics.heightPixels);
        popUpWindowSend.setAnimationStyle(Animation_Dialog);
        ((TextView) popupViewSend.findViewById(R.id.textForWaiting)).setText("Отправляем комментарий...");
        textAns = (TextView) findViewById(R.id.textAns);

        //Заполняем комментами
        getComments();
        LV.addHeaderView(header);
        LV.addFooterView(footer);
        getPhotoItem(extras.getString("photo"), extras.getInt("likes"), extras.getInt("id"));

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComments();
            }
        });

        //Отправка комментария
        messageSend.setOnClickListener(getOnClick());
    }

    //Отправка комментария
    private View.OnClickListener getOnClick() {
        return  new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                popUpWindowSend.showAtLocation(LV, Gravity.CENTER, 0, 0);
                String messageContentString = messageContent.getText().toString();
                if (!messageContentString.isEmpty()) {
                    new Comments(getApplicationContext())
                            .sendComments("photos",
                                    "photo",
                                    extras.getInt("id"),
                                    idAnswer,
                                    messageContentString,
                                    new Comments.SendCommentsCallback() {
                                        @Override
                                        public void onSuccess(boolean response) {
                                            avtoRefresh();
                                            messageContent.setText(null);
                                            popUpWindowSend.dismiss();
                                            Toast.makeText(getApplicationContext(), "Комментарий отправлен", Toast.LENGTH_LONG).show();
                                            deleteAnswer();
                                        }

                                        @Override
                                        public void onError(int error_code, String error_message) {
                                            popUpWindowSend.dismiss();
                                            Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onInternetError() {
                                            popUpWindowSend.dismiss();
                                            Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
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
                        "photos",
                        "photo",
                        extras.getInt("id"),
                        this,
                        new Comments.GetCommentsCallback() {
                            @Override
                            public void onSuccess(Coment[] commentsStructure, Activity activity) {
                                final ArrayList<Coment> defolt = new ArrayList<Coment>();
                                Collections.addAll(defolt, commentsStructure);
                                ca = new CommentsAdapter(activity, defolt, "photo", extras.getInt("id"), extras.getBoolean("isMyphoto"));
                                counts = defolt.size();
                                LV.setAdapter(ca);
                                launching = false;
                                LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if ((position != 0) && (position != (defolt.size() + 1))) {
                                            setAnswer(defolt.get(position - 1).user_id, defolt.get(position-1).user.nickname);
                                        }
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

    //Автоообновление комментов
    private void avtoRefresh() {
        if (!launching) {
            launching = true;
            swipeRefreshLayout.setRefreshing(true);
            new Comments(getApplicationContext())
                    .getComments(
                            "photos",
                            "photo",
                            extras.getInt("id"),
                            this,
                            new Comments.GetCommentsCallback() {
                                @Override
                                public void onSuccess(Coment[] commentsStructure, Activity activity) {
                                    final ArrayList<Coment> defolt = new ArrayList<Coment>();
                                    Collections.addAll(defolt, commentsStructure);
                                    ca.addComments(defolt);
                                    counts = defolt.size();
                                    launching = false;
                                    swipeRefreshLayout.setRefreshing(false);
                                }

                                @Override
                                public void onError(int error_code, String error_message) {
                                    launching = false;
                                }

                                @Override
                                public void onInternetError() {
                                    launching = false;
                                }
                            });
        }
    }

    //Заполнение карточку объявления
    private void getPhotoItem(String url, int likes, final int id) {
        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
        final int width = (int) (displaymetrics.widthPixels);
        if (url != null) {
            Picasso
                    .with(getApplicationContext())
                    .load(url)
                    .resize(width, 0)
                    .into(photo, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso
                                    .with(getApplicationContext())
                                    .load(R.mipmap.avatar)
                                    .resize(width, 0)
                                    .into(photo);
                        }
                    });
        } else {
            Picasso
                    .with(getApplicationContext())
                    .load(R.mipmap.avatar)
                    .resize(width, 0)
                    .into(photo);
        }
        like.setText(ConverterDate.likeStr(likes));
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoCommentsActivity.this, LikeActivity.class);
                intent.putExtra("target_id", id);
                intent.putExtra("subject", "photo");
                intent.putExtra("target_controller", "photos");
                startActivity(intent);
            }
        });
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
                                .getCountsComments("photos",
                                        "photo",
                                        extras.getInt("id"),
                                        new Comments.GetCountsCommentsCallback() {
                                            @Override
                                            public void onSuccess(int count_comments) {
                                                if (counts != count_comments) {
                                                    avtoRefresh();
                                                }
                                            }
                                        });
                    }
                }});
            }
        }, 1000, 10000);
    }

    private void setAnswer(int id, String nickname) {
        ((RelativeLayout) findViewById(R.id.ansBLock)).setVisibility(View.VISIBLE);
        this.idAnswer = id;
        textAns.setText(nickname + " получит ответ");
        messageContent.setText(nickname + ", ");
        nicknameAnswer = nickname;
        ((ImageView) findViewById(R.id.ansExit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAnswer();
            }
        });
    }
    private void deleteAnswer() {
        idAnswer = 0;
        textAns.setText("");
        messageContent.setText("");
        nicknameAnswer = null;
        ((RelativeLayout) findViewById(R.id.ansBLock)).setVisibility(View.GONE);
    }

    public void popupComment(final int id, final String target_controller, final String content_type, final int contentId) {
        popUpWindowDelete.showAtLocation(LV, Gravity.CENTER, 0, 0);
        ((TextView) popupViewDelete.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Comments(getApplicationContext())
                        .deleteComments(id, target_controller, content_type, contentId, new Comments.DeleteCommentCallback() {
                            @Override
                            public void onSuccess() {
                                popUpWindowDelete.dismiss();
                                getComments();
                                Toast.makeText(getApplicationContext(), "Комментарий удален", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(int error_code, String error_message) {
                                popUpWindowDelete.dismiss();
                                Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onInternetError() {
                                popUpWindowDelete.dismiss();
                                Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(popUpWindowDelete.isShowing()) popUpWindowDelete.dismiss();
        else super.onBackPressed();
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
