package com.httpso_hello.hello.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.httpso_hello.hello.Structures.SocketObject;
import com.httpso_hello.hello.activity.Super.SocketActivity;
import com.httpso_hello.hello.adapters.MessagesAttachmentsAdapter;
import com.httpso_hello.hello.adapters.MessagesMessagesAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Complaint;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Logs;
import com.httpso_hello.hello.helper.Messages;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.InputStream;
import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.R.style.Animation_Dialog;

public class ChatActivity extends SocketActivity {

    private Messages messages;
    private ListView chatList;
    private MessagesMessagesAdapter mmAdapter;
    private String pathContactAvatar;
    private int contact_id;
    private String contact_nickname;
    private EmojiconEditText messageContent;
    private ProgressBar progressBarChat;
    private ImageButton emojiKeyboard;
    private View header;
    public EmojIconActions emojIcon;
    private PopupWindow popUpWindow;
    private View popupView;
    private PopupWindow popUpWindowForMenu;
    private View popupViewForMenu;
    private TextView textOnline;
    private Uri sendingImageUri;
    private MessagesAttachmentsAdapter maAdapter;
    private GridView attachmentsListView;
    private AVLoadingIndicatorView avi;
    private boolean  canSendMessage = true;
    private int lastId = 0;
    private ArrayList<Message> allMsg;
    private ArrayList<Message> allMessagesForMenu;
    private int recipientId;
    private Time curentDate;
    private Time lastWriteMsgDate;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_chat);
        setHeader();
        setMenuItem("MessagesActivity");

        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.rootView);
        progressBarChat = (ProgressBar) findViewById(R.id.progressBarChat);
        messageContent = (EmojiconEditText) findViewById(R.id.messageContent);
        emojiKeyboard = (ImageButton) findViewById(R.id.emojiKeyboard) ;
        emojIcon = new EmojIconActions(this, rootView, messageContent, emojiKeyboard);
        header = getLayoutInflater().inflate(R.layout.header, null);
        popupView = getLayoutInflater().inflate(R.layout.popup_for_msg, null);
        popUpWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupViewForMenu = getLayoutInflater().inflate(R.layout.popup_for_message_menu, null);
        popUpWindowForMenu = new PopupWindow(popupViewForMenu, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textOnline = (TextView) findViewById(R.id.textOnline);
        emojIcon.ShowEmojIcon();
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        allMsg = new ArrayList<Message>();
        allMessagesForMenu = new ArrayList<Message>();
        lastWriteMsgDate = new Time(Time.getCurrentTimezone());
        curentDate = new Time(Time.getCurrentTimezone());

        this.contact_nickname = extras.getString("nickname");
        ((TextView) findViewById(R.id.textName)).setText(this.contact_nickname);

        textOnline.setText("не в сети");

        if(extras.getString("avatar")!=null) {
            if (extras.getString("avatar").equals("ic_launcher.png")) {
                Picasso
                        .with(getApplicationContext())
                        .load(R.mipmap.ic_launcher)
                        .resize(300, 300)
                        .centerCrop()
                        .transform(new CircularTransformation(0))
                        .into((ImageView) findViewById(R.id.imageAvatar));
                this.pathContactAvatar = "ic_launcher.png";
            } else {
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
            }
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
        recipientId = extras.getInt("recipientId", 0);
        messages = new Messages(getApplicationContext(), this);

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
                if(mmAdapter==null)
                    return;
                String messageContentString = messageContent.getText().toString();
                if ((!messageContentString.isEmpty() || messages.getCountAttachments()!=0) && canSendMessage) {
                    messageContent.setText(null);

                    Message message = new Message();
                    message.contactId = recipientId;
                    if(messageContentString.length()!=0)
                        message.content = messageContentString;
                    if(maAdapter!=null) {
                        message.photos = maAdapter.getAttachments();
                    } else{
                        message.photos = new Attachment[]{};
                    }
                    message.tempMessageId = System.currentTimeMillis();
                    chatList.setTranscriptMode(2);
                    mmAdapter.addMessage(message);
                    if(maAdapter!=null){
                        maAdapter.deleteAllAttachments();
                        attachmentsListView.getLayoutParams().width = Help.getPxFromDp(130, getApplicationContext());
                    }

                    sendMessageToSocket(message, recipientId, contact_id);
                } else {
                    if(!canSendMessage) {
                        showMessage("Дождитесь загрузки фотографии...");
                    } else {
                        showMessage("Напишите Ваше сообщение...");
                    }
                }
            }
        });
        attachmentsListView = (GridView)findViewById(R.id.messageAttachments);
    }

    private void setMsg(Message[] messages) {
        ((LinearLayout) findViewById(R.id.chatWindow)).setVisibility(View.VISIBLE);
        progressBarChat.setVisibility(View.GONE);
        ChatActivity chatActivity = ((ChatActivity) this);

        ArrayList<Message> asd = new ArrayList<Message>();
        if (messages.length != 0) {
            lastId = messages[0].id;
            for (Message message : messages) {
                asd.add(message);
                if (message.id < lastId) lastId = message.id;
                allMessagesForMenu.add(message);
            }
            allMsg.addAll(asd);
        }

        mmAdapter = new MessagesMessagesAdapter(
                this,
                asd,
                stgs.getSettingStr("avatar.micro"),
                Constant.upload + chatActivity.pathContactAvatar
        );
        chatList.addFooterView(header);
        chatList.setAdapter(mmAdapter);
        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupForMsgMenu(allMessagesForMenu.get(position), position);
            }
        });
    }

    private void popupForMsgMenu (final Message message, final int position) {
        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();

        popUpWindowForMenu.setWidth(displaymetrics.widthPixels);
        popUpWindowForMenu.setHeight(displaymetrics.heightPixels);
        popUpWindowForMenu.setAnimationStyle(Animation_Dialog);
        popUpWindowForMenu.showAtLocation(chatList, Gravity.CENTER, 0, 0);

        //Копировать сообщение
        ((TextView) popupViewForMenu.findViewById(R.id.copy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", message.content);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ChatActivity.this, "Текст скопирован в буфер обмена", Toast.LENGTH_LONG).show();
                popUpWindowForMenu.dismiss();
            }
        });
        //Отправить жалобу
        ((TextView) popupViewForMenu.findViewById(R.id.badContent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Complaint complaint = new Complaint(getApplicationContext());
                complaint.addComplaint(message.content, "Message", "Message", message.id, new Complaint.SendComplaintCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ChatActivity.this, "Жалоба успешно отправлена", Toast.LENGTH_LONG).show();
                        popUpWindowForMenu.dismiss();
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        Toast.makeText(ChatActivity.this, "Что-то пошло не так", Toast.LENGTH_LONG).show();
                        popUpWindowForMenu.dismiss();
                    }

                    @Override
                    public void onInternetError() {
                        Toast.makeText(ChatActivity.this, "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                        popUpWindowForMenu.dismiss();
                    }
                });
            }
        });
        //Удалить сообщение
        ((TextView) popupViewForMenu.findViewById(R.id.deleteContent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int from = 0;
                if (!message.incoming) from = 1;
                else from = 0;
                messages.deleteMessage(message.id, from, new Messages.DeleteMessageCallback() {
                    @Override
                    public void onSuccess() {
                        mmAdapter.deleteMessege(position);
                        Toast.makeText(ChatActivity.this, "Сообщение удалено", Toast.LENGTH_LONG).show();
                        popUpWindowForMenu.dismiss();
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        Toast.makeText(ChatActivity.this, "Что-то пошло не так", Toast.LENGTH_LONG).show();
                        popUpWindowForMenu.dismiss();
                    }

                    @Override
                    public void onInternetError() {
                        Toast.makeText(ChatActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
                        popUpWindowForMenu.dismiss();
                    }
                });
                popUpWindowForMenu.dismiss();
            }
        });
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
                    } else {
                        sendImageFromGallery();
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
                    sendImageFromGallery();
                } else {
                }
                break;
        }

    }


    public void sendImageFromGallery(){
        String file_base64 = "";
        final ArrayList<String> points = new ArrayList<>();
        try {
            final InputStream imageStream = getContentResolver().openInputStream(this.sendingImageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            int position = 0;
            points.add("1");
            if(maAdapter != null){
                Attachment defoltAttachment = new Attachment();
                defoltAttachment.previewAttachmentUri = this.sendingImageUri;
                position = maAdapter.addAttachment(defoltAttachment);
                points.add("2");
//                attachmentsListView.setNumColumns(attachmentsListView.getColumnWidth()+1);
            } else {
                Attachment defoltAttachment = new Attachment();
                defoltAttachment.previewAttachmentUri = this.sendingImageUri;
                ArrayList <Attachment> defoltListAttachment = new ArrayList<>();
                defoltListAttachment.add(defoltAttachment);
                points.add("3");
                maAdapter = new MessagesAttachmentsAdapter(
                        ChatActivity.this,
                        defoltListAttachment,
                        messages
                );
                this.attachmentsListView.setAdapter(maAdapter);
                points.add("4");
            }

            attachmentsListView.getLayoutParams().width =
                    attachmentsListView.getLayoutParams().width +
                            Help.getPxFromDp(130, this);
            points.add("5");
            file_base64 = Help.getBase64FromImage(
                    selectedImage,
                    Bitmap.CompressFormat.JPEG,
                    Help.getFileSize(sendingImageUri, getApplicationContext()),
                    0
            );

            points.add("6");
            canSendMessage = false;
            String tag = messages.addFileToMessage(
                    "photo",
                    "jpg",
                    file_base64,
                    position,
                    new Messages.AddFileToMessageCallback() {
                        @Override
                        public void onSuccess(boolean response, final int id, final int position) {
                            points.add("7");
                            maAdapter.setLoadedAttachment(position, id);
                            canSendMessage = true;
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {
                            canSendMessage = true;
                        }

                        @Override
                        public void onInternetError() {
                            canSendMessage = true;
                        }
                    });

        } catch (Exception e){
            e.printStackTrace();
        }
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
        } else if (id == R.id.action_ignor) {
            Messages ms = new Messages(getApplicationContext(), this);
            ms.ignorContact(contact_id, new Messages.IgnorContactCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(ChatActivity.this, contact_nickname + " теперь у Вас в черном списке", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(int error_code, String error_msg) {
                    Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onInternetError() {
                    Toast.makeText(ChatActivity.this, "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setWriteMsg() {
        textOnline.setText("печатает");
        avi.setVisibility(View.VISIBLE);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override public void run() {
                curentDate.setToNow();
                if (curentDate.second - lastWriteMsgDate.second > 1) {
                    textOnline.setText("в сети");
                    avi.setVisibility(View.GONE);
                }
            }
        }, 2000);
    }

    public void setAllMsgForMenu(ArrayList<Message> allMessagesForMenu) {
        this.allMessagesForMenu = allMessagesForMenu;
    }

    @Override
    public void onBackPressed() {
        if (popUpWindowForMenu.isShowing()) popUpWindowForMenu.dismiss();
        else if (popUpWindow.isShowing()) popUpWindow.dismiss();
        else super.onBackPressed();
    }

    public void onResume() {
        stgs.setSettingInt("showingChatId", this.contact_id);
        super.onResume();
    }

    @Override
    public void onPause() {
        stgs.setSettingInt("showingChatId", 0);
        if (popUpWindowForMenu != null) popUpWindowForMenu.dismiss();
        if (popUpWindow != null) popUpWindow.dismiss();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (popUpWindowForMenu != null) popUpWindowForMenu.dismiss();
        if (popUpWindow != null) popUpWindow.dismiss();
        super.onDestroy();
    }

    @Override
    public void onSocketConnect() {
        getMessagesFromSocket(contact_id, recipientId);
        super.onSocketConnect();
    }

    @Override
    public void onSocketResponse (SocketObject info) {
        if (info.type.equals("get-messages")) {
            if ((this.contact_id == 0) && info.payload.dialogId != 0) this.contact_id = info.payload.dialogId;
            if (this.contact_id == info.payload.dialogId) {
                setMsg(info.payload.messages);
                for (int i = 0; i < info.payload.messages.length; i++) {
                    if (info.payload.messages[i].isNew && info.payload.messages[i].incoming) {
                        sendIsMessageReadedToSocket(info.payload.messages[i].id, recipientId);
                    }
                }
            }
        }
        if (info.type.equals("read-message")) {
            mmAdapter.setReadedMessages();
        }
        if (info.type.equals("on-write-message")) {
            if (contact_id == info.payload.dialogId) setWriteMsg();
            lastWriteMsgDate.setToNow();
        }
        if (info.type.equals("incoming-message")) {
            if (info.payload.message.contactId == this.contact_id) {
                mmAdapter.add(info.payload.message);
                sendIsMessageReadedToSocket(info.payload.message.id, recipientId);
            }
        }
        if (info.type.equals("send-message")) {
            if (this.contact_id != 0) {
                if (info.payload.message.contactId == this.contact_id) {
                    mmAdapter.setMessage(info.payload.message);
                }
            } else {
                mmAdapter.setMessage(info.payload.message);
                this.contact_id = info.payload.message.contactId;
            }
        }
        super.onSocketResponse(info);
    }
}
