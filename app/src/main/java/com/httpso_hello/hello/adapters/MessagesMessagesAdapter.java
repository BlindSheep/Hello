package com.httpso_hello.hello.adapters;

import android.app.Activity;
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

    private final Activity context;
    private ArrayList<Message> messages;// = new ArrayList<>();
    private final Settings stgs;
    private String userAvatar = null;
    private String contactAvatar = null;
    private int user_id;
    private int last_id = 0;
    private int is_new = 0;
    public ChatActivity ca;

    public MessagesMessagesAdapter(Activity context, ArrayList<Message> messages, int user_id, String userAvatar, String contactAvatar){
        super(context, R.layout.content_chat, messages);
        this.context = context;
        this.userAvatar = userAvatar;
        this.contactAvatar = contactAvatar;

        this.messages = messages;
        stgs = new Settings(getContext());
        this.user_id = user_id;
    }

    static class ViewHolder{
        public ImageView avatar;
        public TextView chatMessage;
        public TextView chatMessageDate;
        public GridLayout cloudMessage;
        public TextView readornot;
        public ProgressBar progress;
        public ImageView chat_message_imgae1;
        public ImageView chat_message_imgae2;
        public ImageView chat_message_imgae3;
        public TextView otherPhotos;
        public View customView;
        public ProgressBar progressPhoto1;
        public ProgressBar progressPhoto2;
        public ProgressBar progressPhoto3;
        public LinearLayout LLchat;
        public TextView cloud_1;
        public TextView cloud_2;
        public TextView cloud_3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        View rowView = convertView;
        ca = ((ChatActivity) getContext());
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_chat_item, null, true);
            holder = new ViewHolder();
            holder.avatar = (ImageView) rowView.findViewById(R.id.chat_avatar);
            holder.chatMessage = (TextView) rowView.findViewById(R.id.chat_message);
            holder.chatMessageDate = (TextView) rowView.findViewById(R.id.chat_message_date);
            holder.cloudMessage = (GridLayout) rowView.findViewById(R.id.cloud_meassage);
            holder.chat_message_imgae1 = (ImageView) rowView.findViewById(R.id.chat_message_image1);
            holder.chat_message_imgae2 = (ImageView) rowView.findViewById(R.id.chat_message_image2);
            holder.chat_message_imgae3 = (ImageView) rowView.findViewById(R.id.chat_message_image3);
            holder.readornot = (TextView) rowView.findViewById(R.id.read_or_not);
            holder.progress = (ProgressBar) rowView.findViewById(R.id.progress);
            holder.otherPhotos = (TextView) rowView.findViewById(R.id.otherPhotos);
            holder.customView = (View) rowView.findViewById(R.id.customView);
            holder.progressPhoto1 = (ProgressBar) rowView.findViewById(R.id.progressPhoto1);
            holder.progressPhoto2 = (ProgressBar) rowView.findViewById(R.id.progressPhoto2);
            holder.progressPhoto3 = (ProgressBar) rowView.findViewById(R.id.progressPhoto3);
            holder.LLchat = (LinearLayout) rowView.findViewById(R.id.LLchat);
            holder.cloud_1 = (TextView) rowView.findViewById(R.id.cloud_1);
            holder.cloud_2 = (TextView) rowView.findViewById(R.id.cloud_2);
            holder.cloud_3 = (TextView) rowView.findViewById(R.id.cloud_3);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        // Если прошлоее сообщение не от того же контакта
//        Debug.systemLog("Last id " + this.last_id + " from id " + this.messages[position].from_id);


        final Message message = this.messages.get(position);

        if(position!=0) {

            Message lastMessage = this.messages.get(position-1);
            if (message.from_id != lastMessage.from_id) {

                holder.cloudMessage.setVisibility(View.VISIBLE);
                holder.avatar.setVisibility(View.VISIBLE);

                if (this.user_id == message.from_id) {
                    Picasso
                            .with(getContext())
                            .load(userAvatar)
                            .transform(new CircularTransformation(0))
                            .into(holder.avatar, new Callback(){
                                @Override
                                public void onSuccess(){

                                }
                                @Override
                                public void onError(){
                                    Picasso
                                            .with(getContext())
                                            .load(R.mipmap.avatar)
                                            .transform(new CircularTransformation(0))
                                            .into(holder.avatar);
                                }
                            });
                } else {
                    Picasso
                            .with(getContext())
                            .load(contactAvatar)
                            .transform(new CircularTransformation(0))
                            .into(holder.avatar, new Callback(){
                                @Override
                                public void onSuccess(){
//                                    notifyDataChanged();
                                }
                                @Override
                                public void onError(){
                                    Picasso
                                            .with(getContext())
                                            .load(R.mipmap.avatar)
                                            .transform(new CircularTransformation(0))
                                            .into(holder.avatar);
                                }
                            });;
                }
            } else {
                holder.cloudMessage.setVisibility(View.INVISIBLE);
                holder.avatar.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.cloudMessage.setVisibility(View.VISIBLE);
            holder.avatar.setVisibility(View.VISIBLE);

            if (this.user_id == message.from_id) {
                Picasso
                        .with(getContext())
                        .load(userAvatar)
                        .transform(new CircularTransformation(0))
                        .into(holder.avatar);
            } else {
                Picasso
                        .with(getContext())
                        .load(contactAvatar)
                        .transform(new CircularTransformation(0))
                        .into(holder.avatar);
            }

        }

        if (this.user_id == message.from_id) {
            holder.LLchat.setBackgroundResource(R.drawable.message);
            holder.cloud_1.setBackgroundResource(R.drawable.message);
            holder.cloud_2.setBackgroundResource(R.drawable.message);
            holder.cloud_3.setBackgroundResource(R.drawable.message);
            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("profile_id", user_id);
                    ca.startActivity(intent);
                }
            });
        } else {
            holder.LLchat.setBackgroundResource(R.drawable.message2);
            holder.cloud_1.setBackgroundResource(R.drawable.message2);
            holder.cloud_2.setBackgroundResource(R.drawable.message2);
            holder.cloud_3.setBackgroundResource(R.drawable.message2);
            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("profile_id", message.from_id);
                    ca.startActivity(intent);
                }
            });
        }

        if ((message.is_new == 1) && (this.user_id == message.from_id)) {
            holder.readornot.setVisibility(View.VISIBLE);
        }
        else {
            holder.readornot.setVisibility(View.INVISIBLE);
        }

        if(message.date_pub != null) {
            holder.chatMessageDate.setText(ConverterDate.convertDateForChat(message.date_pub));
            holder.progress.setVisibility(View.GONE);
        }
        else holder.progress.setVisibility(View.VISIBLE);

        holder.chatMessage.setText(message.content);

        DisplayMetrics displaymetrics = getContext().getResources().getDisplayMetrics();
        final int width = displaymetrics.widthPixels / 4;
        final float density = displaymetrics.density;
        if(message.attachments!=null)
            if(message.attachments.length != 0){
                final ChatActivity CA = (ChatActivity) getContext();

                if(message.attachments.length > 0) {
                    holder.chat_message_imgae1.setMinimumHeight(width);
                    holder.chat_message_imgae1.setMinimumWidth(width);
                    holder.progressPhoto1.setVisibility(View.VISIBLE);
                    Picasso
                            .with(this.context.getApplicationContext())
                            .load(Uri.parse(Constant.upload + message.attachments[0].image.small))
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
                                    Picasso
                                            .with(getContext().getApplicationContext())
                                            .load(R.mipmap.avatar)
                                            .resize(width, width)
                                            .centerCrop()
                                            .into(holder.chat_message_imgae1);
                                }
                            });

                    holder.chat_message_imgae1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), FullscreenPhotoActivity.class);
                            ArrayList<String> photoOrig = new ArrayList<String>();
                            for (int j = 0; j < message.attachments.length; j++){
                                if (message.attachments[j].image.original != null) photoOrig.add(j, Constant.upload + message.attachments[j].image.original);
                                else photoOrig.add(j, Constant.upload + message.attachments[j].image.big);
                            }
                            intent.putStringArrayListExtra("photoOrig", photoOrig);
                            intent.putExtra("likeble", false);
                            intent.putExtra("position", 0);
                            CA.startActivity(intent);
                        }
                    });

                    holder.chat_message_imgae1.setVisibility(View.VISIBLE);
                    holder.chat_message_imgae2.setVisibility(View.GONE);
                    holder.chat_message_imgae3.setVisibility(View.GONE);
                } if (message.attachments.length > 1) {
                    holder.progressPhoto2.setVisibility(View.VISIBLE);
                    if(message.attachments.length == 2) {
                        holder.chat_message_imgae2.setMinimumHeight(width);
                        holder.chat_message_imgae2.setMinimumWidth(width);
                        Picasso
                                .with(this.context.getApplicationContext())
                                .load(Uri.parse(Constant.upload + message.attachments[1].image.small))
                                .resize(width, width)
                                .centerCrop()
                                .into(holder.chat_message_imgae2, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        holder.progressPhoto2.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {
                                        holder.progressPhoto2.setVisibility(View.GONE);
                                        Picasso
                                                .with(getContext().getApplicationContext())
                                                .load(R.mipmap.avatar)
                                                .resize(width, width)
                                                .centerCrop()
                                                .into(holder.chat_message_imgae2);
                                    }
                                });
                    } else {
                        holder.chat_message_imgae2.setMinimumHeight((int) (width / 2 - (density*2.5)));
                        holder.chat_message_imgae2.setMinimumWidth((int) (width / 2 - (density*2.5)));
                        Picasso
                                .with(this.context.getApplicationContext())
                                .load(Uri.parse(Constant.upload + message.attachments[1].image.small))
                                .resize((int) (width / 2 - (density*2.5)), (int) (width / 2 - (density*2.5)))
                                .centerCrop()
                                .into(holder.chat_message_imgae2, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        holder.progressPhoto2.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {
                                        holder.progressPhoto2.setVisibility(View.GONE);
                                        Picasso
                                                .with(getContext().getApplicationContext())
                                                .load(R.mipmap.avatar)
                                                .resize((int) (width / 2 - (density*2.5)), (int) (width / 2 - (density*2.5)))
                                                .centerCrop()
                                                .into(holder.chat_message_imgae2);
                                    }
                                });
                    }

                    holder.chat_message_imgae2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), FullscreenPhotoActivity.class);
                            ArrayList<String> photoOrig = new ArrayList<String>();
                            for (int j = 0; j < message.attachments.length; j++){
                                if (message.attachments[j].image.original != null) photoOrig.add(j, Constant.upload + message.attachments[j].image.original);
                                else photoOrig.add(j, Constant.upload + message.attachments[j].image.big);
                            }
                            intent.putStringArrayListExtra("photoOrig", photoOrig);
                            intent.putExtra("likeble", false);
                            intent.putExtra("position", 1);
                            CA.startActivity(intent);
                        }
                    });

                    holder.chat_message_imgae2.setVisibility(View.VISIBLE);
                    holder.chat_message_imgae3.setVisibility(View.GONE);
                } if (message.attachments.length > 2) {
                    holder.chat_message_imgae3.setMinimumHeight((int) (width / 2 - (density*2.5)));
                    holder.chat_message_imgae3.setMinimumWidth((int) (width / 2 - (density*2.5)));
                    holder.progressPhoto3.setVisibility(View.VISIBLE);
                    Picasso
                            .with(this.context.getApplicationContext())
                            .load(Uri.parse(Constant.upload + message.attachments[2].image.small))
                            .resize((int) (width / 2 - (density*2.5)), (int) (width / 2 - (density*2.5)))
                            .centerCrop()
                            .into(holder.chat_message_imgae3, new Callback() {
                                @Override
                                public void onSuccess() {
                                    holder.progressPhoto3.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    holder.progressPhoto3.setVisibility(View.GONE);
                                    Picasso
                                            .with(getContext().getApplicationContext())
                                            .load(R.mipmap.avatar)
                                            .resize((int) (width / 2 - (density*2.5)), (int) (width / 2 - (density*2.5)))
                                            .centerCrop()
                                            .into(holder.chat_message_imgae3);
                                }
                            });

                    holder.chat_message_imgae3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), FullscreenPhotoActivity.class);
                            ArrayList<String> photoOrig = new ArrayList<String>();
                            for (int j = 0; j < message.attachments.length; j++){
                                if (message.attachments[j].image.original != null) photoOrig.add(j, Constant.upload + message.attachments[j].image.original);
                                else photoOrig.add(j, Constant.upload + message.attachments[j].image.big);
                            }
                            intent.putStringArrayListExtra("photoOrig", photoOrig);
                            intent.putExtra("likeble", false);
                            intent.putExtra("position", 2);
                            CA.startActivity(intent);
                        }
                    });

                    holder.chat_message_imgae3.setVisibility(View.VISIBLE);
                } if(message.attachments.length > 3) {
                    holder.otherPhotos.setVisibility(View.VISIBLE);
                    holder.otherPhotos.setText("+" + Integer.toString(message.attachments.length - 3) + " фото");

                    holder.otherPhotos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), FullscreenPhotoActivity.class);
                            ArrayList<String> photoOrig = new ArrayList<String>();
                            for (int j = 0; j < message.attachments.length; j++){
                                if (message.attachments[j].image.original != null) photoOrig.add(j, Constant.upload + message.attachments[j].image.original);
                                else photoOrig.add(j, Constant.upload + message.attachments[j].image.big);
                            }
                            intent.putStringArrayListExtra("photoOrig", photoOrig);
                            intent.putExtra("likeble", false);
                            intent.putExtra("position", 3);
                            CA.startActivity(intent);
                        }
                    });

                } else holder.otherPhotos.setVisibility(View.GONE);
                if(message.content!=null)
                if(message.content.isEmpty())
                    holder.customView.setVisibility(View.GONE);
                else
                    holder.customView.setVisibility(View.VISIBLE);

            } else {
                holder.chat_message_imgae1.setVisibility(View.GONE);
                holder.chat_message_imgae2.setVisibility(View.GONE);
                holder.chat_message_imgae3.setVisibility(View.GONE);
                holder.otherPhotos.setVisibility(View.GONE);
                holder.customView.setVisibility(View.GONE);
            }
        if(message.content!=null)
            if(message.content.isEmpty())
                holder.chatMessage.setVisibility(View.GONE);
            else
                holder.chatMessage.setVisibility(View.VISIBLE);

        return rowView;
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
//        this.messages.addAll(message);
        this.notifyDataSetChanged();
        return this.messages.size();
    }

    public void setMessage(Message message, int message_number){
        this.messages.set(message_number, message);
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
