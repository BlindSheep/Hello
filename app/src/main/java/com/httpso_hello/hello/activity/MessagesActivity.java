package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Contact;
import com.httpso_hello.hello.adapters.MessagesContactsAdapter;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.style.Animation_Dialog;


public class MessagesActivity extends SuperMainActivity{

    private Messages messages;
    private ListView lv;
    private View header;
    private MessagesContactsAdapter mcAdapter;
    private static Handler Tread1_Handler = new Handler();
    private Timer timer;
    private PopupWindow popUpWindow;
    private View popupView;
    private Uri imageUri;
    private String dateLastUpdate;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messages);
        setHeader();

        header = getLayoutInflater().inflate(R.layout.header, null);
        popupView = getLayoutInflater().inflate(R.layout.popup_for_msg, null);
        popUpWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        lv = (ListView) findViewById(R.id.listContacts);
        lv.addHeaderView(header);
        lv.addFooterView(header);
        swipeRefreshLayout.setRefreshing(true);

// Если хотят отправить фото из другого приложения через наше приложение, получаем фото
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && (type.startsWith("image/"))) {
            imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        }
        if (imageUri != null) {
            getSupportActionBar().setTitle("Выберите получателя");
        }
        else {
            getSupportActionBar().setTitle("Сообщения");
        }
    }

    // Получение списка контактов
    public void getContacts() {
        messages = new Messages(getApplicationContext(), this);
        messages.getContacts(new Messages.GetContactsCallback() {
            @Override
            public void onSuccess(final Contact[] contacts, MessagesActivity activity, String dateLU) {
                swipeRefreshLayout.setRefreshing(false);
                dateLastUpdate = dateLU;
                ArrayList<Contact> defolt = new ArrayList<Contact>();
                Collections.addAll(defolt, contacts);
                activity.mcAdapter = new MessagesContactsAdapter(activity, defolt);
                lv.setAdapter(mcAdapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// Обработчик клика по контакту
                        Contact contact = contacts[position - 1];
// Открытие чата
                        Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
                        intent.putExtra("contact_id", contact.contact_id);
                        intent.putExtra("nickname", contact.nickname);
                        if (contact.avatar == null) {
                            intent.putExtra("avatar", Constant.default_avatar);
                        } else {
                            intent.putExtra("avatar", contact.avatar.micro);
                        }
                        if (imageUri != null) {
                            String imageStr = imageUri.toString();
                            intent.putExtra("imageForSend", imageStr);
                        }
                        startActivity(intent);
                    }
                });

// длинный клик по контакту
                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();

                        popUpWindow.setWidth(displaymetrics.widthPixels);
                        popUpWindow.setHeight(displaymetrics.heightPixels);
                        popUpWindow.setAnimationStyle(Animation_Dialog);
                        popUpWindow.showAtLocation(lv, Gravity.CENTER, 0, 0);

// отмена удаления
                        popUpWindow.getContentView().findViewById(R.id.popup_msg).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUpWindow.dismiss();
                            }
                        });

// подтверждение удаления
                        popUpWindow.getContentView().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Contact contact = contacts[position - 1];
                                popUpWindow.dismiss();
                                messages.deleteContacts(contact.id, new Messages.DeleteContactsCallback() {
                                    @Override
                                    public void onSuccess() {
                                        onResume();
                                    }

                                    @Override
                                    public void onError(int error_code, String error_msg) {
                                        Toast.makeText(MessagesActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onInternetError() {
                                        Toast.makeText(MessagesActivity.this, "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });

                        return true;
                    }
                });
            }

            @Override
            public void onError(int error_code, String error_msg) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getContacts();
                    }
                }, 5000);
            }

            @Override
            public void onInternetError(String error_msg) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getContacts();
                    }
                }, 5000);
            }
        });
    }

    public void getNewContacts() {
//автообновление
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Tread1_Handler.post(new Runnable() {public void run() {

                    messages.refreshContacts(dateLastUpdate, new Messages.RefreshContactsCallback() {
                        @Override
                        public void onSuccess(Contact[] contact, String dateLU) {
                            swipeRefreshLayout.setRefreshing(false);
                            dateLastUpdate = dateLU;
                            if(contact.length!=0){
                                ArrayList<Contact> defolt = new ArrayList<Contact>();
                                Collections.addAll(defolt, contact);
                                mcAdapter.updateContacts(defolt);
                            }
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {
                            swipeRefreshLayout.setRefreshing(true);
                        }

                        @Override
                        public void onInternetError() {
                            swipeRefreshLayout.setRefreshing(true);
                        }
                    });

                }});
            }
        }, 500, 3000);
    }

    @Override
    public void onResume(){
//получаем контент
        getContacts();

//запускаем автообновление
        getNewContacts();

        super.onResume();
    }

    @Override
    public void onPause(){
        timer.cancel();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        if (popUpWindow != null) popUpWindow.dismiss();
        timer.cancel();
        super.onDestroy();
    }
}