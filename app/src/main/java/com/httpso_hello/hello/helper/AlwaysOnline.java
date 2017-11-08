package com.httpso_hello.hello.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.AllCounts;
import com.httpso_hello.hello.activity.BoardActivity;
import com.httpso_hello.hello.activity.MessagesActivity;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.activity.SearchActivity;

/**
 * Created by mixir on 08.11.2017.
 */

public class AlwaysOnline extends Service {

    private static Handler Tread1_Handler = new Handler();
    private Timer timer;
    private Settings stgs;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        stgs = new Settings(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (stgs.getSettingInt("always_online") == 1) {
            timer = new Timer();
            Context context = getApplicationContext();

            Intent notificationIntent;
            if (stgs.getSettingInt("startPage") == 3) {
                notificationIntent = new Intent(context, ProfileActivity.class);
                notificationIntent.putExtra("profile_id", stgs.getSettingInt("user_id"));
            } else if (stgs.getSettingInt("startPage") == 2) {
                notificationIntent = new Intent(context, MessagesActivity.class);
            } else if (stgs.getSettingInt("startPage") == 1) {
                notificationIntent = new Intent(context, SearchActivity.class);
            } else if (stgs.getSettingInt("startPage") == 0) {
                notificationIntent = new Intent(context, BoardActivity.class);
            } else {
                notificationIntent = new Intent(context, BoardActivity.class);
            }
            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Resources res = context.getResources();
            Notification.Builder builder = new Notification.Builder(context);

            String content = "Онлайн";

            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_action_star)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)
                    .setContentTitle("Hello")
                    .setContentText(content);

            Notification notification = builder.getNotification();
            notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(666, notification);
        } else {
            if (timer != null) {
                timer.cancel();
            }
            Context context = getApplicationContext();
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(666);
        }

        if (stgs.getSettingInt("always_online") == 1) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Tread1_Handler.post(new Runnable() {
                        public void run() {
                            if (stgs.getSettingInt("always_online") == 1) {
                                Profile.getInstance(getApplicationContext())
                                        .getCount(new Profile.GetCountCallback() {
                                            @Override
                                            public void onSuccess(AllCounts allCounts) {
                                                Context context = getApplicationContext();

                                                Intent notificationIntent;
                                                if (stgs.getSettingInt("startPage") == 3) {
                                                    notificationIntent = new Intent(context, ProfileActivity.class);
                                                    notificationIntent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                                                } else if (stgs.getSettingInt("startPage") == 2) {
                                                    notificationIntent = new Intent(context, MessagesActivity.class);
                                                } else if (stgs.getSettingInt("startPage") == 1) {
                                                    notificationIntent = new Intent(context, SearchActivity.class);
                                                } else if (stgs.getSettingInt("startPage") == 0) {
                                                    notificationIntent = new Intent(context, BoardActivity.class);
                                                } else {
                                                     notificationIntent = new Intent(context, BoardActivity.class);
                                                }
                                                PendingIntent contentIntent = PendingIntent.getActivity(context,
                                                        0, notificationIntent,
                                                        PendingIntent.FLAG_CANCEL_CURRENT);

                                                Resources res = context.getResources();
                                                Notification.Builder builder = new Notification.Builder(context);

                                                String content = null;
                                                if (allCounts.new_messages != 0)
                                                    content = "Новые сообщения: " + Integer.toString(allCounts.new_messages);
                                                else if (allCounts.new_notices != 0)
                                                    content = "Новые уведомления: " + Integer.toString(allCounts.new_notices);
                                                else if (allCounts.new_guests != 0)
                                                    content = "Новые гости: " + Integer.toString(allCounts.new_guests);
                                                else if (allCounts.requests_in_friends != 0)
                                                    content = "Заявки в друзья: " + Integer.toString(allCounts.requests_in_friends);
                                                else content = "Онлайн";

                                                builder.setContentIntent(contentIntent)
                                                        .setSmallIcon(R.mipmap.ic_action_star)
                                                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                                                        .setWhen(System.currentTimeMillis())
                                                        .setAutoCancel(false)
                                                        .setContentTitle("Hello")
                                                        .setContentText(content);

                                                Notification notification = builder.getNotification();
                                                notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
                                                NotificationManager notificationManager = (NotificationManager) context
                                                        .getSystemService(Context.NOTIFICATION_SERVICE);
                                                notificationManager.notify(666, notification);
                                            }

                                            @Override
                                            public void onError(int error_code, String error_msg) {

                                            }

                                            @Override
                                            public void onInternetError() {

                                            }
                                        });
                            }
                        }
                    });
                }
            }, 0, 60000);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
