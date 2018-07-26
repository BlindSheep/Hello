package com.httpso_hello.hello.activity.Super;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.httpso_hello.hello.Structures.SocketObject;
import com.httpso_hello.hello.helper.Socket.SocketConnect;

public class SocketActivity extends MethodsActivity{

    public SocketServiceConnection socketServiceConnection;
    public Messenger messenger = new Messenger(new IncomingHandler());
    public Messenger toServiceMessenger;
    public boolean serviseIsConnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent(this, SocketConnect.class),
                (socketServiceConnection = new SocketServiceConnection()),
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(socketServiceConnection);
    }




    //Запрос списка сообщений из сокета
    public void getMessagesFromSocket(final int contact_id ) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                if (serviseIsConnect) {
                    android.os.Message msg = android.os.Message.obtain(null, 3);
                    msg.replyTo = messenger;
                    msg.arg1 = contact_id;
                    try {toServiceMessenger.send(msg);}
                    catch (RemoteException e) {
                    }
                } else {
                    getMessagesFromSocket(contact_id);
                }
            }
        }, 1000);
    }

    //Отправка сообщения на сокет
    public void sendMessageToSocket (final String content, final int recipientId, final int contact_id) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                if (serviseIsConnect) {
                    android.os.Message msg = android.os.Message.obtain(null, 4);
                    msg.replyTo = messenger;
                    msg.arg1 = contact_id;
                    msg.arg2 = recipientId;
                    msg.obj = content;
                    try {toServiceMessenger.send(msg);}
                    catch (RemoteException e) {
                    }
                } else {
                    sendMessageToSocket (content, recipientId, contact_id);
                }
            }
        }, 1000);
    }

    //Запрос инфы из сокета
    public void getContactsFromSocket () {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                if (serviseIsConnect) {
                    Message msg = Message.obtain(null, 2);
                    msg.replyTo = messenger;
                    try {toServiceMessenger.send(msg);}
                    catch (RemoteException e) {
                    }
                } else {
                    getContactsFromSocket();
                }
            }
        }, 1000);
    }





    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            GsonBuilder GB = new GsonBuilder();
            Gson gson = GB.create();
            SocketObject obj = gson.fromJson(msg.obj.toString(), SocketObject.class);
            onSocketResponse(obj);
        }
    }

    private class SocketServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            toServiceMessenger = new Messenger(service);
            Message msg = Message.obtain(null, 1);
            msg.replyTo = messenger;
            try {toServiceMessenger.send(msg);}
            catch (RemoteException e) {
            }
            serviseIsConnect = true;
            onSocketConnect();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            onSocketDisconnect();
            serviseIsConnect = false;
        }
    }

    //Сокет и сервис подключились
    public void onSocketConnect () {
    }

    //Сокет и сервис отключились
    public void onSocketDisconnect () {
    }

    //Если счётчики изменились
    public void onCountersChange() {

    }


    //Работа с информацией из СОКЕТОВ
    public void onSocketResponse (SocketObject info) {
        System.out.println("!!!!!!!!!! Тип сокета - " + info.type);
        System.out.println("!!!!!!!!!! Респонс сокета - " + info.payload);
//        Установка счётчиков
        if (info.type.equals("SET_COUNTERS")) {
            stgs.setSettingInt("messages", info.payload.counters.messages);
            stgs.setSettingInt("guests", info.payload.counters.guests);
            stgs.setSettingInt("newFriends", info.payload.counters.newFriends);
            stgs.setSettingInt("mutuallySimpations", info.payload.counters.mutuallySimpations);
            stgs.setSettingInt("incomingSimpations", info.payload.counters.incomingSimpations);
            setCounters();
        } else if (info.type.equals("ADD_COUNT")) {
//            Изменения счётчиков +1
            onCountersChange();
            switch (info.payload.counterName) {
                case "messages":
                    stgs.setSettingInt("messages", stgs.getSettingInt("messages") + 1);
                    break;
                case "guests":
                    stgs.setSettingInt("guests", stgs.getSettingInt("guests") + 1);
                    break;
                case "newFriends":
                    stgs.setSettingInt("newFriends", stgs.getSettingInt("newFriends") + 1);
                    break;
                case "mutuallySimpations":
                    stgs.setSettingInt("mutuallySimpations", stgs.getSettingInt("mutuallySimpations") + 1);
                    break;
                case "incomingSimpations":
                    stgs.setSettingInt("incomingSimpations", stgs.getSettingInt("incomingSimpations") + 1);
                    break;
            }
            setCounters();
        } else if (info.type.equals("DEC_COUNT")) {
//            Изменения счётчиков -1
            onCountersChange();
            switch (info.payload.counterName) {
                case "messages":
                    stgs.setSettingInt("messages", stgs.getSettingInt("messages") - 1);
                    break;
                case "guests":
                    stgs.setSettingInt("guests", stgs.getSettingInt("guests") - 1);
                    break;
                case "newFriends":
                    stgs.setSettingInt("newFriends", stgs.getSettingInt("newFriends") - 1);
                    break;
                case "mutuallySimpations":
                    stgs.setSettingInt("mutuallySimpations", stgs.getSettingInt("mutuallySimpations") - 1);
                    break;
                case "incomingSimpations":
                    stgs.setSettingInt("incomingSimpations", stgs.getSettingInt("incomingSimpations") - 1);
                    break;
            }
            setCounters();
        }
    }
}