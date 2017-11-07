package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.activity.ChatActivity;
import com.httpso_hello.hello.activity.FullscreenPhotoActivity;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by mixir on 01.08.2017.
 */

public class MessagesMessagesAdapter extends ArrayAdapter<Message> {

    private static final int FROM = 0;
    private static final int TO = 1;
    private static final int TIME = 2;

    private final Activity context;
    private ArrayList<Message> messages;
    private final Settings stgs;
    private String userAvatar = null;
    private String contactAvatar = null;
    private int user_id;
    public ChatActivity ca;
    private LayoutInflater mInflater;

    public MessagesMessagesAdapter(Activity context, ArrayList<Message> messages, int user_id, String userAvatar, String contactAvatar){
        super(context, R.layout.content_chat, messages);
        this.context = context;
        this.userAvatar = userAvatar;
        this.contactAvatar = contactAvatar;
        mInflater = (LayoutInflater) context.getLayoutInflater();

        this.messages = messages;
        stgs = new Settings(getContext());
        this.user_id = user_id;
    }

    static class ViewHolder{

        //Лайаут отправителя
        public RelativeLayout senderLayout;
        public ImageView avatar_sender;
        public TextView chatMessage_sender;
        public TextView chatMessageDate_sender;
        public GridLayout cloudMessage_sender;
        public TextView readornot_sender;
        public LinearLayout forPhoto_sender;
        public ImageView chat_message_imgae1_sender;
        public TextView otherPhotos_sender;
        public View customView_sender;
        public ProgressBar progressPhoto1_sender;
        public ProgressBar progress_sender;

        //Лайаут получателя
        public RelativeLayout getterLayout;
        public ImageView avatar;
        public TextView chatMessage;
        public TextView chatMessageDate;
        public LinearLayout forPhoto;
        public ImageView chat_message_imgae1;
        public TextView otherPhotos;
        public View customView;
        public GridLayout cloudMessage;
        public ProgressBar progressPhoto1;

        //Лайаут новой даты
        public TextView isNewDate;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ca = ((ChatActivity) getContext());
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.content_chat_item_first, null);

            //отправитель
            holder.senderLayout = (RelativeLayout)  convertView.findViewById(R.id.senderLayout);
            holder.avatar_sender = (ImageView) convertView.findViewById(R.id.chat_avatar_sender);
            holder.chatMessage_sender = (TextView) convertView.findViewById(R.id.chat_message_sender);
            holder.forPhoto_sender = (LinearLayout) convertView.findViewById(R.id.forPhoto_sender);
            holder.customView_sender = (View) convertView.findViewById(R.id.customView_sender);
            holder.chat_message_imgae1_sender = (ImageView) convertView.findViewById(R.id.chat_message_image1_sender);
            holder.chatMessageDate_sender = (TextView) convertView.findViewById(R.id.chat_message_date_sender);
            holder.readornot_sender = (TextView) convertView.findViewById(R.id.read_or_not_sender);
            holder.cloudMessage_sender = (GridLayout) convertView.findViewById(R.id.cloud_meassage_sender);
            holder.otherPhotos_sender = (TextView) convertView.findViewById(R.id.otherPhotos_sender);
            holder.progressPhoto1_sender = (ProgressBar) convertView.findViewById(R.id.progressPhoto1_sender);
            holder.progress_sender = (ProgressBar) convertView.findViewById(R.id.progress_sender);

            //получатель
            holder.getterLayout = (RelativeLayout)  convertView.findViewById(R.id.getterLayout);
            holder.avatar = (ImageView) convertView.findViewById(R.id.chat_avatar);
            holder.chatMessage = (TextView) convertView.findViewById(R.id.chat_message);
            holder.forPhoto = (LinearLayout) convertView.findViewById(R.id.forPhoto);
            holder.customView = (View) convertView.findViewById(R.id.customView);
            holder.chat_message_imgae1 = (ImageView) convertView.findViewById(R.id.chat_message_image1);
            holder.chatMessageDate = (TextView) convertView.findViewById(R.id.chat_message_date);
            holder.cloudMessage = (GridLayout) convertView.findViewById(R.id.cloud_meassage);
            holder.otherPhotos = (TextView) convertView.findViewById(R.id.otherPhotos);
            holder.progressPhoto1 = (ProgressBar) convertView.findViewById(R.id.progressPhoto1);

            //дата
            holder.isNewDate = (TextView) convertView.findViewById(R.id.isNewDate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Message thisMessage;
        final Message lastMessage;
        final boolean isNewDate;
        final boolean lastPosition;
        thisMessage = this.messages.get(position);
        if (position!=0) {
            lastMessage = this.messages.get(position - 1);
            if ((thisMessage.date_pub != null) && (lastMessage.date_pub != null)) {
                if (!ConverterDate.convertDayForChat(thisMessage.date_pub).equals(ConverterDate.convertDayForChat(lastMessage.date_pub)))
                    isNewDate = true;
                else isNewDate = false;
            } else isNewDate = false;
        }
        else {
            isNewDate = false;
            lastMessage = null;
        }
        if (position == 0) lastPosition = true;
        else lastPosition = false;

            //Если отправитель
            if (user_id == thisMessage.from_id) {
                holder.senderLayout.setVisibility(View.VISIBLE);
                holder.getterLayout.setVisibility(View.GONE);

                //Аватарка
                Picasso
                        .with(getContext())
                        .load(userAvatar)
                        .transform(new CircularTransformation(0))
                        .into(holder.avatar_sender);

                //Текст
                holder.chatMessage_sender.setText(thisMessage.content);

                //Дата
                if (thisMessage.date_pub != null) {
                    holder.progress_sender.setVisibility(View.GONE);
                    holder.chatMessageDate_sender.setText(ConverterDate.convertDateForChat(thisMessage.date_pub));
                } else {
                    holder.progress_sender.setVisibility(View.VISIBLE);
                }

                //Прочитано ли
                if (thisMessage.is_new == 1) holder.readornot_sender.setVisibility(View.VISIBLE);
                else holder.readornot_sender.setVisibility(View.GONE);

                // Скрываем аву и облака где надо
                if (lastMessage != null) {
                    if ((lastMessage.from_id != thisMessage.from_id) || (isNewDate)) {
                        holder.avatar_sender.setVisibility(View.VISIBLE);
                        holder.cloudMessage_sender.setVisibility(View.VISIBLE);
                    } else {
                        holder.avatar_sender.setVisibility(View.INVISIBLE);
                        holder.cloudMessage_sender.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.avatar_sender.setVisibility(View.VISIBLE);
                    holder.cloudMessage_sender.setVisibility(View.VISIBLE);
                }

                //Скрыть атачменты
                if (thisMessage.attachments != null) {
                    if (thisMessage.attachments.length == 0) {
                        holder.forPhoto_sender.setVisibility(View.GONE);
                        holder.customView_sender.setVisibility(View.GONE);
                        holder.otherPhotos_sender.setVisibility(View.GONE);
                        holder.chatMessage_sender.setVisibility(View.VISIBLE);
                    } else {
                        holder.forPhoto_sender.setVisibility(View.VISIBLE);
                        if (thisMessage.content == null) {
                            holder.customView_sender.setVisibility(View.GONE);
                            holder.chatMessage_sender.setVisibility(View.GONE);
                        } else {
                            holder.customView_sender.setVisibility(View.VISIBLE);
                            holder.chatMessage_sender.setVisibility(View.VISIBLE);
                        }
                        DisplayMetrics displaymetrics = getContext().getResources().getDisplayMetrics();
                        int width = (int) (displaymetrics.widthPixels / 2);
                        holder.chat_message_imgae1_sender.setMinimumWidth(width);
                        holder.chat_message_imgae1_sender.setMinimumHeight(width);
                        holder.progressPhoto1_sender.setVisibility(View.VISIBLE);
                        String downloadPath = "";
                        if(thisMessage.attachments[0].previewAttachmentUri!=null){
                            downloadPath = thisMessage.attachments[0].previewAttachmentUri.toString();
                        }else{
                            downloadPath = Constant.upload + thisMessage.attachments[0].image.small;
                        }
                        Picasso.with(getContext())
                                .load(downloadPath)
                                .resize(width, width)
                                .centerCrop()
                                .into(holder.chat_message_imgae1_sender, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        holder.progressPhoto1_sender.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {
                                        holder.progressPhoto1_sender.setVisibility(View.GONE);
                                    }
                                });

                        holder.chat_message_imgae1_sender.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<String> photoOrig = new ArrayList<String>();
                                for (int j = 0; j < thisMessage.attachments.length; j++) {
                                    photoOrig.add(j, Constant.upload + thisMessage.attachments[j].image.big);
                                }
                                Intent intent = new Intent(getContext(), FullscreenPhotoActivity.class);
                                intent.putStringArrayListExtra("photoOrig", photoOrig);
                                intent.putExtra("likeble", false);
                                intent.putExtra("position", 0);
                                ca.startActivity(intent);
                            }
                        });

                        if (thisMessage.attachments.length > 1) {
                            holder.otherPhotos_sender.setText("+ " + Integer.toString(thisMessage.attachments.length - 1) + " фото");
                            holder.otherPhotos_sender.setVisibility(View.VISIBLE);
                        } else {
                            holder.otherPhotos_sender.setVisibility(View.GONE);
                        }
                    }
                } else {
                    holder.forPhoto_sender.setVisibility(View.GONE);
                    holder.customView_sender.setVisibility(View.GONE);
                    holder.otherPhotos_sender.setVisibility(View.GONE);
                    holder.chatMessage_sender.setVisibility(View.VISIBLE);
                }

                //Дата по центру
                if (isNewDate || lastPosition) {
                    if ((thisMessage.date_pub != null)) {
                        holder.isNewDate.setText(ConverterDate.convertDayForChat(thisMessage.date_pub));
                    } else holder.isNewDate.setText("сегодня");
                    holder.isNewDate.setVisibility(View.VISIBLE);
                } else holder.isNewDate.setVisibility(View.GONE);

            //Если получатель
            } else {
                holder.senderLayout.setVisibility(View.GONE);
                holder.getterLayout.setVisibility(View.VISIBLE);

                //Аватарка
                Picasso
                        .with(getContext())
                        .load(contactAvatar)
                        .transform(new CircularTransformation(0))
                        .into(holder.avatar);

                //Текст
                holder.chatMessage.setText(thisMessage.content);

                //Дата
                holder.chatMessageDate.setText(ConverterDate.convertDateForChat(thisMessage.date_pub));

                // Скрываем аву и облака где надо
                if (lastMessage != null) {
                    if ((lastMessage.from_id != thisMessage.from_id) || (isNewDate)) {
                        holder.avatar.setVisibility(View.VISIBLE);
                        holder.cloudMessage.setVisibility(View.VISIBLE);
                    } else {
                        holder.avatar.setVisibility(View.INVISIBLE);
                        holder.cloudMessage.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.avatar.setVisibility(View.VISIBLE);
                    holder.cloudMessage.setVisibility(View.VISIBLE);
                }

                //Скрыть атачменты
                if (thisMessage.attachments.length == 0) {
                    holder.forPhoto.setVisibility(View.GONE);
                    holder.customView.setVisibility(View.GONE);
                    holder.otherPhotos.setVisibility(View.GONE);
                    holder.chatMessage.setVisibility(View.VISIBLE);
                } else {
                    holder.forPhoto.setVisibility(View.VISIBLE);
                    if (thisMessage.content == null) {
                        holder.customView.setVisibility(View.GONE);
                        holder.chatMessage.setVisibility(View.GONE);
                    } else {
                        holder.customView.setVisibility(View.VISIBLE);
                        holder.chatMessage.setVisibility(View.VISIBLE);
                    }
                    DisplayMetrics displaymetrics = getContext().getResources().getDisplayMetrics();
                    int width = (int) (displaymetrics.widthPixels/2);
                    holder.chat_message_imgae1.setMinimumWidth(width);
                    holder.chat_message_imgae1.setMinimumHeight(width);
                    holder.progressPhoto1.setVisibility(View.VISIBLE);
                    Picasso.with(getContext())
                            .load(Constant.upload + thisMessage.attachments[0].image.small)
                            .resize(width, width)
                            .centerCrop()
                            .into(holder.chat_message_imgae1, new Callback() {
                                @Override
                                public void onSuccess() {
                                    holder.progressPhoto1.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    holder.progressPhoto1.setVisibility(View.GONE);
                                }
                            });

                    holder.chat_message_imgae1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<String> photoOrig = new ArrayList<String>();
                            for (int j = 0; j < thisMessage.attachments.length; j++) {
                                photoOrig.add(j, Constant.upload + thisMessage.attachments[j].image.big);
                            }
                            Intent intent = new Intent(getContext(), FullscreenPhotoActivity.class);
                            intent.putStringArrayListExtra("photoOrig", photoOrig);
                            intent.putExtra("likeble", false);
                            intent.putExtra("position", 0);
                            ca.startActivity(intent);
                        }
                    });

                    if (thisMessage.attachments.length > 1) {
                        holder.otherPhotos.setText("+ " + Integer.toString(thisMessage.attachments.length - 1) + " фото");
                        holder.otherPhotos.setVisibility(View.VISIBLE);
                    } else {
                        holder.otherPhotos.setVisibility(View.GONE);
                    }
                }

                //Дата по центру
                if (isNewDate || lastPosition) {
                    holder.isNewDate.setText(ConverterDate.convertDayForChat(thisMessage.date_pub));
                    holder.isNewDate.setVisibility(View.VISIBLE);
                } else holder.isNewDate.setVisibility(View.GONE);
            }
        return convertView;
    }

    public void setAllMessages(Message[] messages){
        this.messages.clear();
        for (Message message : messages ) {
            this.messages.add(this.messages.size(), message);
        }
        this.notifyDataSetChanged();
    }
    public int addMessage(Message message){
        this.messages.add(this.messages.size(), message);
        this.notifyDataSetChanged();
        return this.messages.size();
    }

    public void setMessage(Message message, int message_number){
        /*if(this.messages.get(message_number).deviceMessageId == message.deviceMessageId){
            this.messages.set(message_number, message);
        } else {

        }*/
        int i = this.messages.size()-1;
        while (i>=0){
            if(this.messages.get(i).deviceMessageId == message.deviceMessageId)
                break;
            else
                i--;
        }
        this.messages.set(i, message);
        this.notifyDataSetChanged();
    }

    public int containMessage(Message message){
        int i = 0;
        for (Message msg: this.messages){
            if(msg.id == message.id){
                return i;
            }
            i++;
        }
        return -1;
    }

    public void setReadedMessage(int messageID){
        for(Message message : this.messages){
            if(message.id == messageID){
                message.is_new = 0;
                this.notifyDataSetChanged();
                break;
            }
        }

    }

    public  void addMessages(Message[] messages){
        for (Message message: messages) {
            int index = this.containMessage(message);
            Log.d("index", Integer.toString(index));
            Log.d("index", Boolean.toString(this.messages.contains(message)));
            if(index == -1){
                this.messages.add(message);
            }
        }
        this.notifyDataSetChanged();
    }
}
