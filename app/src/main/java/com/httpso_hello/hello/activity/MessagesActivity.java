package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Contact;
import com.httpso_hello.hello.Structures.SocketObject;
import com.httpso_hello.hello.activity.Super.SocketActivity;
import com.httpso_hello.hello.adapters.MessagesContactsAdapter;
import com.httpso_hello.hello.helper.Constant;

import java.util.ArrayList;
import java.util.Collections;

import static android.R.style.Animation_Dialog;


public class MessagesActivity extends SocketActivity {

    private ListView lv;
    private View header;
    private MessagesContactsAdapter mcAdapter;
    private PopupWindow popUpWindow;
    private View popupView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Contact> contactsArray = new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setHeader();
        setMenuItem("MessagesActivity");

        header = getLayoutInflater().inflate(R.layout.footer6dp, null);
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
    }

    public void setDisbalance(ArrayList<Contact> contacts) {
        contactsArray = contacts;
    }

    // Подстановка списка контактов
    public void setContacts(Contact[] contacts) {
        contactsArray.clear();
        Collections.addAll(contactsArray, contacts);
        ArrayList<Contact> defolt = new ArrayList<Contact>();
        Collections.addAll(defolt, contacts);
        mcAdapter = new MessagesContactsAdapter(this, defolt);
        lv.setAdapter(mcAdapter);

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = contactsArray.get(position - 1);
                Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
                intent.putExtra("recipientId", contact.contactId);
                intent.putExtra("contact_id", contact.id);
                intent.putExtra("nickname", contact.user.nickname);
                if (contact.user.avatar == null) {
                    intent.putExtra("avatar", Constant.default_avatar);
                } else {
                    intent.putExtra("avatar", contact.user.avatar.micro);
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
                        Contact contact = contactsArray.get(position - 1);
                        popUpWindow.dismiss();
//                        messages.deleteContacts(contact.id, new Messages.DeleteContactsCallback() {
//                            @Override
//                            public void onSuccess() {
////                                getContacts();
//                                Toast.makeText(MessagesActivity.this, "Контакт удален", Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onError(int error_code, String error_msg) {
//                                Toast.makeText(MessagesActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onInternetError() {
//                                Toast.makeText(MessagesActivity.this, "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
//                            }
//                        });
                    }
                });

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (popUpWindow.isShowing()) popUpWindow.dismiss();
        else super.onBackPressed();
    }

    @Override
    public void onPause(){
        if (popUpWindow != null) popUpWindow.dismiss();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        if (popUpWindow != null) popUpWindow.dismiss();
        super.onDestroy();
    }

    @Override
    public void onSocketConnect() {
        getContactsFromSocket();
        super.onSocketConnect();
    }

    @Override
    public void onSocketResponse (SocketObject info) {
        super.onSocketResponse(info);
        if (info.type.equals("get-contacts")) setContacts(info.payload.contacts);
    }
}