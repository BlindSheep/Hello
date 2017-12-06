package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Attachment;
import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.Structures.Coment;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.Structures.Photo;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.adapters.CommentsAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Comments;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Content;
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

import static android.R.style.Animation_Dialog;

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
    private View popupViewDelete;
    private PopupWindow popUpWindowDelete;
    private int idAnswer;
    private String nicknameAnswer;
    private TextView textAns;
    private Handler refreshAtError;
    private Runnable refreshAtErrorRunnable;
    private ImageView answer;
    private ImageView firstPhotoBoard;
    private View popupViewSend;
    private PopupWindow popUpWindowSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_board_content);
        setHeader();
        setMenuItem("BoardActivity");

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
        answer = (ImageView) header.findViewById(R.id.answer);
        contentBoardContentBlock = (RelativeLayout) findViewById(R.id.content_board_content_block);
        messageContent = (EmojiconEditText) findViewById(R.id.messageContent);
        emojiKeyboard = (ImageButton) findViewById(R.id.emojiKeyboard);
        emojIcon = new EmojIconActions(this, contentBoardContentBlock, messageContent, emojiKeyboard);
        emojIcon.ShowEmojIcon();
        messageSend = (ImageView) findViewById(R.id.messageSend);
        textAns = (TextView) findViewById(R.id.textAns);
        firstPhotoBoard = (ImageView) header.findViewById(R.id.firstPhotoBoard);

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

        //Заполняем комментами
        LV.addHeaderView(header);
        LV.addFooterView(footer);
        getBoardItem(extras.getInt("id"));
        getComments();

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
                            .sendComments("content",
                                    "board",
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
                        "content",
                        "board",
                        extras.getInt("id"),
                        this,
                        new Comments.GetCommentsCallback() {
                            @Override
                            public void onSuccess(Coment[] commentsStructure, Activity activity) {
                                final ArrayList<Coment> defolt = new ArrayList<Coment>();
                                Collections.addAll(defolt, commentsStructure);
                                ca = new CommentsAdapter(activity, defolt, "board", extras.getInt("id"), false);
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
                            "content",
                            "board",
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
    private void getBoardItem(final int id) {
        Content
                .getInstance(getApplicationContext())
                .getContentItem(
                        id,
                        new Content.GetContentItemCallback() {
                            @Override
                            public void onSuccess(final BoardItem item) {
                                boolean anonim = true;
                                if(item.is_anonim.equals("1")) anonim = true;
                                else anonim = false;
                                if (!anonim) {
                                    answer.setVisibility(View.VISIBLE);
                                    answer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            setAnswer(item.user_id, item.user_nickname);
                                        }
                                    });
                                    if (item.avatar != null) {
                                        Picasso
                                                .with(getApplicationContext())
                                                .load(Uri.parse(Constant.upload + item.avatar.micro))
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

                                    if (item.photos != null) {
                                        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
                                        firstPhotoBoard.setVisibility(View.VISIBLE);

                                        if ((item.photos[0].sizes != null) && (item.photos[0].original != null) && (item.photos[0].sizes.original.width != 0) && (item.photos[0].sizes.original.height != 0)) {
                                            int widthScreen = (int) (displaymetrics.widthPixels);
                                            double heightCooeficient = (double) widthScreen / (double) item.photos[0].sizes.original.width;
                                            double heightDouble = item.photos[0].sizes.original.height * heightCooeficient;
                                            int height = (int) (heightDouble);
                                            firstPhotoBoard.setMinimumWidth(widthScreen);
                                            firstPhotoBoard.setMinimumHeight(height);
                                            Picasso
                                                    .with(getApplicationContext())
                                                    .load(Uri.parse(ConverterDate.convertUrlAvatar(item.photos[0].original)))
                                                    .resize(widthScreen, height)
                                                    .into(firstPhotoBoard);
                                        } else {
                                            int width = (int) (displaymetrics.widthPixels);
                                            firstPhotoBoard.setMinimumWidth(width);
                                            firstPhotoBoard.setMinimumHeight(width);
                                            Picasso
                                                    .with(getApplicationContext())
                                                    .load(Uri.parse(ConverterDate.convertUrlAvatar(item.photos[0].normal)))
                                                    .resize(width, width)
                                                    .centerCrop()
                                                    .into(firstPhotoBoard);
                                        }
                                        firstPhotoBoard.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ArrayList<String> photoOrig = new ArrayList<String>();
                                                Collections.addAll(photoOrig, ConverterDate.convertUrlAvatar(item.photos[0].original));
                                                Intent intent = new Intent(getApplicationContext(), FullscreenPhotoActivity.class);
                                                intent.putStringArrayListExtra("photoOrig", photoOrig);
                                                intent.putExtra("likeble", false);
                                                intent.putExtra("position", 0);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    userNameBoardItem.setText(item.user_nickname);
                                    datePubBoardItem.setText(ConverterDate.convertDateForGuest(item.date_pub));

                                    if (!item.content.equals("-")) {
                                        boardTextItem.setText(Html.fromHtml(item.content));
                                        boardTextItem.setVisibility(View.VISIBLE);
                                    } else {
                                        boardTextItem.setVisibility(View.GONE);
                                    }

                                    boardLikeItem.setText(ConverterDate.likeStr(item.rating));
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
                                    //Клик по шапке
                                    userAvatarBoardItem.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(BoardContentActivity.this, ProfileActivity.class);
                                                intent.putExtra("profile_id", item.user_id);
                                                startActivity(intent);
                                            }
                                        });
                                } else {
                                    answer.setVisibility(View.GONE);
                                    Picasso
                                            .with(getApplicationContext())
                                            .load(R.drawable.ic_action_anonimnost)
                                            .transform(new CircularTransformation(0))
                                            .into(userAvatarBoardItem);
                                    userNameBoardItem.setText("Анонимно");
                                    datePubBoardItem.setText(ConverterDate.convertDateForGuest(item.date_pub));

                                    if (item.photos != null) {
                                        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
                                        firstPhotoBoard.setVisibility(View.VISIBLE);

                                        if ((item.photos[0].sizes != null) && (item.photos[0].original != null) && (item.photos[0].sizes.original.width != 0) && (item.photos[0].sizes.original.height != 0)) {
                                            int widthScreen = (int) (displaymetrics.widthPixels);
                                            double heightCooeficient = (double) widthScreen / (double) item.photos[0].sizes.original.width;
                                            double heightDouble = item.photos[0].sizes.original.height * heightCooeficient;
                                            int height = (int) (heightDouble);
                                            firstPhotoBoard.setMinimumWidth(widthScreen);
                                            firstPhotoBoard.setMinimumHeight(height);
                                            Picasso
                                                    .with(getApplicationContext())
                                                    .load(Uri.parse(ConverterDate.convertUrlAvatar(item.photos[0].original)))
                                                    .resize(widthScreen, height)
                                                    .into(firstPhotoBoard);
                                        } else {
                                            int width = (int) (displaymetrics.widthPixels);
                                            firstPhotoBoard.setMinimumWidth(width);
                                            firstPhotoBoard.setMinimumHeight(width);
                                            Picasso
                                                    .with(getApplicationContext())
                                                    .load(Uri.parse(ConverterDate.convertUrlAvatar(item.photos[0].normal)))
                                                    .resize(width, width)
                                                    .centerCrop()
                                                    .into(firstPhotoBoard);
                                        }
                                        firstPhotoBoard.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ArrayList<String> photoOrig = new ArrayList<String>();
                                                Collections.addAll(photoOrig, ConverterDate.convertUrlAvatar(item.photos[0].original));
                                                Intent intent = new Intent(getApplicationContext(), FullscreenPhotoActivity.class);
                                                intent.putStringArrayListExtra("photoOrig", photoOrig);
                                                intent.putExtra("likeble", false);
                                                intent.putExtra("position", 0);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    if (!item.content.equals("-")) {
                                        boardTextItem.setText(Html.fromHtml(item.content));
                                        boardTextItem.setVisibility(View.VISIBLE);
                                    } else {
                                        boardTextItem.setVisibility(View.GONE);
                                    }

                                    boardLikeItem.setText(ConverterDate.likeStr(item.rating));
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
                                    userAvatarBoardItem.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                refreshAtError = new Handler();
                                refreshAtErrorRunnable = new Runnable() {
                                    @Override public void run() {
                                        getBoardItem(id);
                                    }
                                };
                                refreshAtError.postDelayed(refreshAtErrorRunnable,5000);
                            }

                            @Override
                            public void onInternetError() {
                                refreshAtError = new Handler();
                                refreshAtErrorRunnable = new Runnable() {
                                    @Override public void run() {
                                        getBoardItem(id);
                                    }
                                };
                                refreshAtError.postDelayed(refreshAtErrorRunnable,5000);
                            }
                        });
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

    //Проверка новых комментов
    private void getCountsComments() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {public void run() {
                        new Comments(getApplicationContext())
                                .getCountsComments("content",
                                        "board",
                                        extras.getInt("id"),
                                        new Comments.GetCountsCommentsCallback() {
                                            @Override
                                            public void onSuccess(int count_comments) {
                                                if (counts != count_comments) {
                                                    avtoRefresh();
                                                }
                                            }
                                        });
                }});
            }
        }, 1000, 10000);
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
        else if(popUpWindowSend.isShowing()) popUpWindowSend.dismiss();
        else if(idAnswer != 0) deleteAnswer();
        else super.onBackPressed();
    }

    @Override
    public void onResume() {
        getCountsComments();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (popUpWindowDelete != null) popUpWindowDelete.dismiss();
        if (popUpWindowSend != null) popUpWindowSend.dismiss();
        timer.cancel();
        super.onPause();
        if(refreshAtError!=null) {
            refreshAtError.removeCallbacks(refreshAtErrorRunnable);
        }
    }

    @Override
    public void onDestroy() {
        if (popUpWindowDelete != null) popUpWindowDelete.dismiss();
        if (popUpWindowSend != null) popUpWindowSend.dismiss();
        timer.cancel();
        super.onDestroy();
        if(refreshAtError!=null) {
            refreshAtError.removeCallbacks(refreshAtErrorRunnable);
        }
    }
}