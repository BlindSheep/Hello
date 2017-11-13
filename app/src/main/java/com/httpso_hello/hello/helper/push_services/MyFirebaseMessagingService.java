package com.httpso_hello.hello.helper.push_services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.httpso_hello.hello.R;
import com.httpso_hello.hello.activity.ChatActivity;
import com.httpso_hello.hello.activity.MainActivity;
import com.httpso_hello.hello.activity.MessagesActivity;
import com.httpso_hello.hello.activity.NotisesActivity;
import com.httpso_hello.hello.helper.Messages;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Picasso;
import android.graphics.Rect;
import java.util.Map;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.radius;

/**
 * Created by mixir on 02.08.2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                // // TODO: 15.08.2017 Вылетает
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Map messageData = remoteMessage.getData();

        if ((messageData.get("avatar_string") != null) && (messageData.get("avatar") != null)) {
            if (messageData.get("title") != null) {
                sendNotification(
                        messageData.get("body").toString(),
                        messageData.get("title").toString(),
                        messageData.get("avatar_string").toString(),
                        null,
                        Integer.parseInt(messageData.get("profile_id").toString()),
                        messageData.get("profile_nickname").toString(),
                        messageData.get("avatar").toString(),
                        messageData.get("type").toString()
                );
            } else {
                sendNotification(
                        messageData.get("body").toString(),
                        null,
                        messageData.get("avatar_string").toString(),
                        null,
                        Integer.parseInt(messageData.get("profile_id").toString()),
                        messageData.get("profile_nickname").toString(),
                        messageData.get("avatar").toString(),
                        messageData.get("type").toString()
                );
            }
        } else {
            if (messageData.get("title") != null) {
                sendNotification(
                        messageData.get("body").toString(),
                        messageData.get("title").toString(),
                        null,
                        null,
                        Integer.parseInt(messageData.get("profile_id").toString()),
                        messageData.get("profile_nickname").toString(),
                        null,
                        messageData.get("type").toString()
                );
            } else {
                sendNotification(
                        messageData.get("body").toString(),
                        null,
                        null,
                        null,
                        Integer.parseInt(messageData.get("profile_id").toString()),
                        messageData.get("profile_nickname").toString(),
                        null,
                        messageData.get("type").toString()
                );
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(
            // Прямая информация для пуш уведомления
            String messageBody,
            String title,
            String base64_smallImage,
            String base64_bigImage,
            // Сведенья об отправителе
            int profile_id,
            String nickname,
            String avatar,
            // Сведенья о типе пуш уведомлении
            String type) {
        Settings stgs = new Settings(getApplicationContext());
        if(type != "flirtik")
            if(stgs.getSettingInt(type)!=0){
                //Если отключено в настройках то не выводить
                return;
            }
            else
            if(stgs.getSettingInt(type) == 0){
                //Если отключено в настройках то не выводить
                return;
            }
        Intent intent;
        PendingIntent pIntent;
        switch (type){
            case "message":
                intent = new Intent(this, ChatActivity.class);
                intent.putExtra("avatar", avatar);
                intent.putExtra("nickname", nickname);
                intent.putExtra("contact_id", profile_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case "rating":
                intent = new Intent(this, NotisesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case "comment":
                intent = new Intent(this, NotisesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case "gift":
                intent = new Intent(this, NotisesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case "flirtik":
                intent = new Intent(this, NotisesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            default:
                // Если тип не распознан то не выводить
                return;
//                break;
        }

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
// Вставка аватарки
        Bitmap bitmap;
        if(base64_smallImage!=null && base64_smallImage.length()!=0) {
            byte[] bytes = Base64.decode(base64_smallImage.getBytes(), Base64.DEFAULT);

            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.avatar);
        }
        int radius = 50;

        int diam = radius << 1;

        Bitmap scaledBitmap = scaleTo(bitmap, diam);

        final Path path = new Path();
        path.addCircle(radius, radius, radius, Path.Direction.CCW);

        Bitmap targetBitmap = Bitmap.createBitmap(diam, diam, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);

        canvas.clipPath(path);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        canvas.drawBitmap(scaledBitmap, 0, 0, paint);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_menu_messages)
                .setLargeIcon(targetBitmap)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1500, 500, 1500})
                .setContentIntent(pIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public static Bitmap scaleTo(Bitmap source, int size)
    {
        int destWidth = source.getWidth();

        int destHeight = source.getHeight();

        destHeight = destHeight * size / destWidth;
        destWidth = size;

        if (destHeight < size)
        {
            destWidth = destWidth * size / destHeight;
            destHeight = size;
        }

        Bitmap destBitmap = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(destBitmap);
        canvas.drawBitmap(source, new Rect(0, 0, source.getWidth(), source.getHeight()), new Rect(0, 0, destWidth, destHeight), new Paint(Paint.ANTI_ALIAS_FLAG));
        return destBitmap;
    }

}
