package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Contact;

import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class MessagesContactsAdapter extends ArrayAdapter<Contact>{

    private final Activity context;
    private ArrayList<Contact> contacts;
    private final Settings stgs;


    public MessagesContactsAdapter(Activity context, ArrayList<Contact> contacts) {
        super(context, R.layout.content_messages, contacts);
        this.contacts = contacts;
        this.context = context;
        this.stgs = new Settings(getContext());
    }

    static class ViewHolder{
        public ImageView avatar;
        public TextView contactNickname;
        public EmojiconTextView lastMessage;
        public TextView dateLastMessage;
        public TextView numberOfNewMessages;
        public ImageView isOnline;
        public TextView is_unreaded;
        public EmojIconActions emojIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_messages_contact, null, true);
            holder = new ViewHolder();
            holder.contactNickname = (TextView) rowView.findViewById(R.id.contactNickname);
            holder.lastMessage = (EmojiconTextView) rowView.findViewById(R.id.lastMessage);
            holder.avatar = (ImageView) rowView.findViewById(R.id.avatar);
            holder.dateLastMessage = (TextView) rowView.findViewById(R.id.dateLastMessage);
            holder.numberOfNewMessages = (TextView) rowView.findViewById(R.id.numberOfNewMessages);
            holder.isOnline = (ImageView) rowView.findViewById(R.id.isOnline);
            holder.is_unreaded = (TextView) rowView.findViewById(R.id.is_unreaded);
//            holder.emojIcon = new (this, rootView, messageContent, emojiKeyboard);
//            holder.emojIcon.ShowEmojIcon();
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Contact contact = this.contacts.get(position);

        holder.contactNickname.setText(contact.nickname);

        if(contact.last_message_content != null){
            if(contact.last_message_content.length()==0){
                contact.last_message_content = "Фотография";
            }
            // Если были сообщений
            if(contact.lm_from_contact!=1) {
                // Последнее сообщение НЕ от контакта
                holder.lastMessage.setText("Я: " + contact.last_message_content);
                if (contact.contact_readed_messages == 1) {
                    // Если контакт не прочитал сообщение
                    holder.is_unreaded.setVisibility(View.VISIBLE);
                } else {
                    // Если контакт прочитал сообщение
                    holder.is_unreaded.setVisibility(View.GONE);
                }
                // Скрываем счетчик входящих не прочитанных сообщений
                holder.numberOfNewMessages.setText("0");
                holder.numberOfNewMessages.setVisibility(View.GONE);
            } else {
                // Последнее сообщение от контакта
                holder.lastMessage.setText(contact.last_message_content);
                holder.is_unreaded.setVisibility(View.GONE);
                if(contact.inc_new_messages > 0) {
                    // Если количество не прочитанных сообщений больше нуля
                    holder.numberOfNewMessages.setText(Integer.toString(contact.inc_new_messages));
                    holder.numberOfNewMessages.setVisibility(View.VISIBLE);
                } else {
                    holder.numberOfNewMessages.setText("0");
                    holder.numberOfNewMessages.setVisibility(View.GONE);
                }
            }
            // Есди были сообщения устанавливаем дату последнего
            holder.dateLastMessage.setText(ConverterDate.convertDateForContacts(contact.date_update));
            holder.dateLastMessage.setVisibility(View.VISIBLE);
            holder.lastMessage.setTextColor(getContext().getResources().getColor(R.color.main_black_color_hello));
        } else {
            // если сообщений не было
            holder.lastMessage.setText("Нет сообщений");
            holder.lastMessage.setTextColor(getContext().getResources().getColor(R.color.main_grey_color_hello));
            holder.is_unreaded.setVisibility(View.INVISIBLE);
            holder.numberOfNewMessages.setVisibility(View.INVISIBLE);
            holder.dateLastMessage.setVisibility(View.INVISIBLE);
        }

        if(contact.is_online) {
            holder.isOnline.setVisibility(View.VISIBLE);
        } else {
            holder.isOnline.setVisibility(View.INVISIBLE);
        }

        //// TODO: 30.07.2017 Добавить проверку наличия изображений: проверка заполнености размеров
        if(contact.avatar != null) {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.upload + contact.avatar.micro))
                    .transform(new CircularTransformation(0))
                    .into(holder.avatar);
        } else {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.default_avatar))
                    .transform(new CircularTransformation(0))
                    .into(holder.avatar);
        }

        /**
         * Отображение статуса прочтения сообщения
         */


        return rowView;

    }

    public ArrayList<Contact> get(){
        return this.contacts;
    }

    public Contact getItem(int position){
        return this.contacts.get(position);
    }

    public void setItem(int position, Contact contact){
        this.contacts.set(position, contact);
        this.notifyDataSetChanged();
    }

    public void set(ArrayList<Contact> contacts){
        this.contacts = contacts;
        this.notifyDataSetChanged();
    }
    public void addAll(ArrayList<Contact> contacts){
        this.contacts.addAll(contacts);
        this.notifyDataSetChanged();
    }
    public void updateContacts(ArrayList<Contact> contacts){

        for(Contact newContact : contacts){
            int position = this.getContactById(newContact.id);
            if(position == -1){
                this.contacts.add(newContact);
            } else {
                this.contacts.set(position, newContact);
            }
        }
        this.notifyDataSetChanged();
    }
    public int getContactById(int id){
        int i = 0;
        for (Contact contact : this.contacts){
            if(contact.id == id){
                return i;
            }
            i++;
        }
        return -1;
    }
}


