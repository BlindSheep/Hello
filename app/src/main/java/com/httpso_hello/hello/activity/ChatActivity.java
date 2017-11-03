package com.httpso_hello.hello.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Attachment;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.adapters.MessagesAttachmentsAdapter;
import com.httpso_hello.hello.adapters.MessagesMessagesAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Messages;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.R.style.Animation_Dialog;

public class ChatActivity extends SuperMainActivity{

    private Messages messages;
    private ListView chatList;
    private MessagesMessagesAdapter mmAdapter;
    private String pathContactAvatar;
    private int contact_id;
    private String contact_nickname;
    private EmojiconEditText messageContent;
    private static Handler Tread1_Handler = new Handler(), sendedMessagesHandler = new Handler(), onlineHandler = new Handler();
    private boolean firstItemScrolled = false;
    private Timer timer = new Timer(), sendedMessagesTimer = new Timer(), onlineTimer = new Timer();
    private boolean isStartedSendedMessagesTimer;
    private ProgressBar progressBarChat;
    private ImageButton emojiKeyboard;
    private View headerForSupport;
    private View header;
    public EmojIconActions emojIcon;
    private PopupWindow popUpWindow;
    private View popupView;
    private LinearLayout ll;
    private float density;
    private String dateLastUpdate;
    private TextView textOnline;
    private ArrayList<Message> sendedMessages = new ArrayList<>();
    private Uri sendingImageUri;
    private int user_id;
    private MessagesAttachmentsAdapter maAdapter;
    private GridView attachmentsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_chat);
        setHeader();
        setMenuItem("MessagesActivity");

        user_id = stgs.getSettingInt("user_id");
        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.rootView);
        progressBarChat = (ProgressBar) findViewById(R.id.progressBarChat);
        messageContent = (EmojiconEditText) findViewById(R.id.messageContent);
        emojiKeyboard = (ImageButton) findViewById(R.id.emojiKeyboard) ;
        emojIcon = new EmojIconActions(this, rootView, messageContent, emojiKeyboard);
        header = getLayoutInflater().inflate(R.layout.header, null);
        headerForSupport = getLayoutInflater().inflate(R.layout.header_for_support, null);
        popupView = getLayoutInflater().inflate(R.layout.popup_for_msg, null);
        popUpWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll = (LinearLayout) findViewById(R.id.forImage);
        density = getApplicationContext().getResources().getDisplayMetrics().density;
        textOnline = (TextView) findViewById(R.id.textOnline);
        emojIcon.ShowEmojIcon();

        this.contact_nickname = extras.getString("nickname");
        ((TextView) findViewById(R.id.textName)).setText(this.contact_nickname);

        //  Uri imageUri - переменная где хранится ссылка на фотографию
        //Если хотят отправить фото через наше приложение
        //Передаем экстрас в данную активность с ссылкой на фотографию строкой "imageForSend"
        String image = extras.getString("imageForSend");
        if (image != null){
            //Показ миниатюры для отправки
            ImageView iv = new ImageView(getApplicationContext());
            LinearLayout ll = (LinearLayout) findViewById(R.id.forImage);
            float density = getApplicationContext().getResources().getDisplayMetrics().density;
            Uri imageUri = Uri.parse(image);
            iv.setPadding((int) (density * 5), (int) (density * 5), 0, (int) (density * 5));
            Picasso.with(getApplicationContext())
                    .load(imageUri)
                    .resize(0, (int) (density * 100))
                    .into(iv);
            ll.addView(iv);
        }

        if(extras.getString("avatar")!=null) {
            Picasso
                    .with(getApplicationContext())
                    .load(Constant.upload + extras.getString("avatar"))
                    .resize(300, 300)
                    .centerCrop()
                    .transform(new CircularTransformation(0))
                    .into((ImageView) findViewById(R.id.imageAvatar), new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso
                                    .with(getApplicationContext())
                                    .load(R.mipmap.avatar)
                                    .resize(300, 300)
                                    .centerCrop()
                                    .transform(new CircularTransformation(0))
                                    .into((ImageView) findViewById(R.id.imageAvatar));
                        }
                    });
            this.pathContactAvatar = extras.getString("avatar");
        } else {
            Picasso
                    .with(getApplicationContext())
                    .load(R.mipmap.avatar)
                    .resize(300, 300)
                    .centerCrop()
                    .transform(new CircularTransformation(0))
                    .into((ImageView) findViewById(R.id.imageAvatar));
            this.pathContactAvatar = Constant.default_avatar;
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
                intent.putExtra("profile_id", extras.getInt("contact_id"));
                startActivity(intent);
            }
        });

        chatList = (ListView) findViewById(R.id.chatList);

        this.contact_id = extras.getInt("contact_id", 0);
        if(this.contact_id == 0){
//            this.contact_id = Integer.getInteger(extras.getString("contact_id"));
        }
        messages = new Messages(getApplicationContext(), this);

        chatList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(totalItemCount-firstVisibleItem>visibleItemCount){
                    firstItemScrolled = true;
                } else {
                    firstItemScrolled = false;
                }
            }
        });

        messages.getMessages(this.contact_id, new Messages.GetMessagesCallback() {
            @Override
            public void onSuccess(
                    Message messages[],
                    Activity activity,
                    int user_id,
                    String user_avatar_micro,
                    Message[] sendedUnreadedMessagesIDs,
                    String dateLU,
                    boolean contactIsOnline
            ) {
                dateLastUpdate = dateLU;

                progressBarChat.setVisibility(View.GONE);
                ChatActivity chatActivity = ((ChatActivity) activity);

                ArrayList<Message> asd = new ArrayList<Message>();
                for (Message message : messages) {
                    asd.add(message);
                }

                mmAdapter = new MessagesMessagesAdapter(
                        activity,
                        asd,
                        user_id,
                        user_avatar_micro,
                        Constant.upload + chatActivity.pathContactAvatar
                );
                if(contact_id == 3008) chatList.addHeaderView(headerForSupport);
                else chatList.addHeaderView(header);
//                chatList.addHeaderView(header);
                chatList.addFooterView(header);
                chatList.setAdapter(mmAdapter);
                contactOnline(contactIsOnline);
                Collections.addAll(sendedMessages, sendedUnreadedMessagesIDs);
//                sendedMessages.addAll(sendedUnreadedMessagesIDs);
            }
        }, new Help.ErrorCallback() {
            @Override
            public void onError(int error_code, String error_msg) {
                Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onInternetError() {
                Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
            }
        });

        // Обработка кнопки прикрепления файлов
        ((ImageButton)findViewById(R.id.docSend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);

            }
        });

        // Обработка отправки сообщения
        final ImageButton messageSend = (ImageButton) findViewById(R.id.messageSend);
        messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContentString = messageContent.getText().toString();
                if (!messageContentString.isEmpty() || messages.getCountAttachments()!=0) {
                    ChatActivity.this.messageContent.setText(null);

                    Message message = new Message();
                    message.from_id = stgs.getSettingInt("user_id");
                    message.contact_id = ChatActivity.this.contact_id;
                    if(messageContentString.length()!=0)
                        message.content = messageContentString;
                    if(maAdapter!=null) {
                        message.attachments = maAdapter.getAttachments();
                    } else{
                        message.attachments = new Attachment[]{};
                    }

                    chatList.setTranscriptMode(2);
                    int message_number = ChatActivity.this.mmAdapter.addMessage(message);
                    if(maAdapter!=null){
                        maAdapter.deleteAllAttachments();
                        attachmentsListView.getLayoutParams().width = Help.getPxFromDp(130, getApplicationContext());
//                        maAdapter = null;
                    }
                    messages.sendMessage(
                            ChatActivity.this.contact_id,
                            messageContentString,
                            message_number-1,
                            new Messages.MessagesSendMessage() {
                                @Override
                                public void onSuccess(Message message, String dateLU, int message_number) {

                                    dateLastUpdate = dateLU;
                                    mmAdapter.setMessage(message, message_number);
                                    sendedMessages.add(message);
                                }

                            }, new Help.ErrorCallback() {

                                @Override
                                public void onError(int error_code, String error_msg) {
                                    Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onInternetError() {
                                    Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        attachmentsListView = (GridView)findViewById(R.id.messageAttachments);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();

            popUpWindow.setWidth(displaymetrics.widthPixels);
            popUpWindow.setHeight(displaymetrics.heightPixels);
            popUpWindow.setAnimationStyle(Animation_Dialog);
            popUpWindow.showAtLocation(chatList, Gravity.CENTER, 0, 0);

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
                    popUpWindow.dismiss();
                    messages.deleteContacts(contact_id, new Messages.DeleteContactsCallback() {
                        @Override
                        public void onSuccess() {
                            finish();
                        }

                        @Override
                        public void onError(int error_code, String error_msg) {
                            Toast.makeText(ChatActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onInternetError() {
                            Toast.makeText(ChatActivity.this, "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Tread1_Handler.post(new Runnable() {public void run() {
                    messages.refreshMessages(dateLastUpdate, contact_id, new Messages.RefreshMessagesCallback() {
                        @Override
                        public void onSuccess(Message[] messages, String dateLU, boolean contactIsOnline) {
                            dateLastUpdate = dateLU;
                            if(firstItemScrolled) {
                                chatList.setTranscriptMode(0);
                            } else {
                                chatList.setTranscriptMode(2);
                            }
                            if(messages.length!=0) {
                                for(Message message : messages){
                                    if(message.is_new==1 && message.from_id==user_id){
                                        sendedMessages.add(message);
                                    }
                                }

                                mmAdapter.addMessages(messages);
                            }
                            contactOnline(contactIsOnline);
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {

                        }

                        @Override
                        public void onInternetError() {

                        }
                    });
                }});
            }
        }, 3000, 3000);
//        if(!isStartedSendedMessagesTimer) {
        isStartedSendedMessagesTimer = true;
        sendedMessagesTimer = new Timer();
        sendedMessagesTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendedMessagesHandler.post(new Runnable() {
                    public void run() {
                        if (sendedMessages.size() != 0) {
                            messages.getStateMessages(sendedMessages, new Messages.GetStateMessagesCallback() {
                                @Override
                                public void onSuccess(Message[] messagesIDs) {
                                    int i = 0;
                                    for (Message sendedMessage : sendedMessages) {
                                        boolean isDeleted = true;
                                        for (Message unreadedMessage : messagesIDs) {
                                            if (sendedMessage.id == unreadedMessage.id) {
                                                isDeleted = false;
                                                break;
                                            }
                                        }
                                        if (isDeleted) {
                                            mmAdapter.setReadedMessage(sendedMessage.id);
//                                        sendedMessages.remove(sendedMessage);
                                        }
                                        i++;
                                    }
                                }
                            }, new Help.ErrorCallback() {
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
        }, 3000, 3000);
//        }

        super.onResume();
    }

    @Override
    public void onPause() {
        timer.cancel();
        if(isStartedSendedMessagesTimer) {
            sendedMessagesTimer.cancel();
            isStartedSendedMessagesTimer = false;
        }
        onlineTimer.cancel();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        if(isStartedSendedMessagesTimer) {
            sendedMessagesTimer.cancel();
            isStartedSendedMessagesTimer = false;
        }
        onlineTimer.cancel();
        super.onDestroy();
    }

    //Обрабатываем результат выбора фоток из галереи или с камеры
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    sendingImageUri = imageReturnedIntent.getData();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if(Help.runTaskAfterPermission(
                                ChatActivity.this,
                                new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                },
                                Help.REQUEST_ADD_PHOTO_MESSAGE
                        )){
                            sendImageFromGallery();
                        }


                    }
                }
                break;
            default:
                System.out.println("Какая-то ошибка");
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Help.REQUEST_ADD_PHOTO_MESSAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted.
                    sendImageFromGallery();
                } else {
                    // User refused to grant permission.
//                    Toast.makeText(this, "Для доступа к фото необходимо ваше разрешение", Toast.LENGTH_LONG).show();

                }
                break;
        }

    }


    public void sendImageFromGallery(){
        try {
            final InputStream imageStream = getContentResolver().openInputStream(this.sendingImageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            int position = 0;
            if(maAdapter != null){
                Attachment defoltAttachment = new Attachment();
                defoltAttachment.previewAttachmentUri = this.sendingImageUri;
                position = maAdapter.addAttachment(defoltAttachment);
//                attachmentsListView.setNumColumns(attachmentsListView.getColumnWidth()+1);
            } else {

                Attachment defoltAttachment = new Attachment();
                defoltAttachment.previewAttachmentUri = this.sendingImageUri;
                ArrayList <Attachment> defoltListAttachment = new ArrayList<>();
                defoltListAttachment.add(defoltAttachment);

                maAdapter = new MessagesAttachmentsAdapter(
                        ChatActivity.this,
                        defoltListAttachment,
                        messages
                );
                this.attachmentsListView.setAdapter(maAdapter);
                attachmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Attachment attachment = maAdapter.getItem(position);
//                        messages.deleteAttachment(position, attachment.id);
//                        maAdapter.deleteAttachment(position);
                    }
                });
            }

            attachmentsListView.getLayoutParams().width =
                    attachmentsListView.getLayoutParams().width +
                            Help.getPxFromDp(130, this);

            //Преобразование для отправки на серв
            final String file_base64 = Help.getBase64FromImage(
                    selectedImage,
                    Bitmap.CompressFormat.JPEG,
                    Help.getFileSize(sendingImageUri, getApplicationContext()),
                    0
            );
            messages.addFileToMessage(
                    "photo",
                    "jpg",
                    file_base64,
                    position,
                    new Messages.AddFileToMessageCallback() {
                        @Override
                        public void onSuccess(boolean response, final int id, final int position) {
                            maAdapter.setLoadedAttachment(position, id);
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {

                        }

                        @Override
                        public void onInternetError() {

                        }
                    });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void contactOnline(boolean contactIsOnline){
        if(contactIsOnline){
            textOnline.setText("в сети");
            if (textOnline.getText().equals("в сети")) textOnline.setTextColor(getResources().getColor(R.color.main_green_color_hello));
            else textOnline.setTextColor(getResources().getColor(R.color.main_white_color_hello));
        } else {
            textOnline.setTextColor(getResources().getColor(R.color.main_white_color_hello));
            textOnline.setText("Не в сети");
        }
    }
}
