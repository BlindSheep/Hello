package com.httpso_hello.hello.helper.Socket;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationChannel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.SocketObject;
import com.httpso_hello.hello.activity.BoardActivity;
import com.httpso_hello.hello.helper.Settings;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketConnect extends Service {

    public Socket socket;
    public Settings settings;

    IncomingHandler inHandler;
    Messenger messanger;
    Messenger toActivityMessenger;
    HandlerThread thread;

    public SocketConnect() {
        if (socket == null) {
            SocketThreed socketThreed = new SocketThreed();
            socketThreed.execute();
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return messanger.getBinder();
    }

    @Override
    public void onCreate(){
        super.onCreate();

        settings = new Settings(getApplicationContext());

        thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        inHandler = new IncomingHandler(thread.getLooper());
        messanger = new Messenger(inHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    class SocketThreed extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                socket = IO.socket("https://o-hello.com");
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        //Получаем счётчики
                        if (settings.getSettingStr("token") != null) {
                            JSONObject obj = new JSONObject();
                            JSONObject payload = new JSONObject();
                            try {
                                obj.put("type", "get-counters");
                                payload.put("token", settings.getSettingStr("token"));
                                obj.put("payload", payload);
                            } catch (Exception e) {
                            }
                            socket.emit("action", obj);
                        }
                        System.out.println("!!!!!!!!!!!!!Коннект сокета");
                    }

                }).on("action", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Message outMsg = Message.obtain(inHandler, 1);
                        outMsg.obj = args[0];
                        outMsg.replyTo = messanger;
                        GsonBuilder GB = new GsonBuilder();
                        Gson gson = GB.create();
                        SocketObject msg = gson.fromJson(args[0].toString(), SocketObject.class);
                        try {
                            if (toActivityMessenger != null)
                                toActivityMessenger.send(outMsg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    }

                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        System.out.println("!!!!!!!!!!!!!Дисконект сокета");
                    }

                });
                socket.connect();

                // Sending an object


                // Receiving an object
                socket.on("foo", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject obj = (JSONObject)args[0];
                    }
                });
            } catch(Exception e) {
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    //обработчик сообщений активити
    private class IncomingHandler extends Handler {
        public IncomingHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){
            JSONObject obj = new JSONObject();
            JSONObject payload = new JSONObject();
            switch (msg.what) {
                case 2:// Запрос списка контактов
                    try{
                        obj.put("type", "get-contacts");
                        payload.put("token", settings.getSettingStr("token"));
                        payload.put("page", Integer.toString(0));
                        payload.put("perPage", Integer.toString(100));
                        obj.put("payload", payload);
                    }
                    catch (Exception e){}
                    socket.emit("action", obj);
                    break;
                case 3:// Запрос списка сообщений
                    try{
                        obj.put("type", "get-messages");
                        payload.put("token", settings.getSettingStr("token"));
                        if (msg.arg1 != 0) payload.put("dialogId", msg.arg1);
                        else payload.put("profileId", msg.arg2);
                        payload.put("page", Integer.toString(0));
                        payload.put("perPage", Integer.toString(1000));
                        obj.put("payload", payload);
                    }
                    catch (Exception e){}
                    socket.emit("action", obj);
                    break;
                case 4: // Отправка сообщения
                    try{
                        obj.put("type", "send-message");
                        payload.put("token", settings.getSettingStr("token"));
                        payload.put("dialogId", msg.arg1);
                        payload.put("recipientId", msg.arg2);
                        com.httpso_hello.hello.Structures.Message object = (com.httpso_hello.hello.Structures.Message) msg.obj;
                        payload.put("content", object.content);
                        payload.put("tempMessageId", object.tempMessageId);
                        obj.put("payload", payload);
                    }
                    catch (Exception e){}
                    socket.emit("action", obj);
                    break;
                case 5: // Сообщение прочитанно
                    try{
                        obj.put("type", "read-message");
                        payload.put("token", settings.getSettingStr("token"));
                        payload.put("messageId", msg.arg1);
                        payload.put("contactId", msg.arg2);
                        obj.put("payload", payload);
                    }
                    catch (Exception e){}
                    socket.emit("action", obj);
                    break;
            }
            toActivityMessenger = msg.replyTo;
        }
    }
}