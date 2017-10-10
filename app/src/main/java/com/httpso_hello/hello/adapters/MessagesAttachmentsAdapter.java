package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Attachment;
import com.httpso_hello.hello.Structures.Contact;
import com.httpso_hello.hello.helper.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Общий on 08.10.2017.
 */

public class MessagesAttachmentsAdapter /*extends ArrayAdapter<Attachment> */{
/*

    private final Activity activity;
    private ArrayList<Attachment> attachments;

    public MessagesAttachmentsAdapter(Activity activity, ArrayList<Attachment> attachments){
        super(activity, R.layout.content_chat, attachments);
        this.attachments = attachments;
        this.activity = activity;
    }

    static class ViewHolder{
        public ImageView imagePreview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final ViewHolder holder;
        View rowView = convertView;

        if (rowView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_chat_attachments_item, null, true);
            holder = new ViewHolder();
            holder.imagePreview = (ImageView) rowView.findViewById(R.id.chat_attachments_item_image_preview);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        final Attachment attachment = this.attachments.get(position);
        if(attachment.previewAttachment != null){
            holder.imagePreview = attachment.previewAttachment;
        }
*/
/*
        Picasso.with(getContext())
                .load(Constant.upload + attachment.image.micro)
                .into(holder.imagePreview);
   *//*




        return rowView;

    }
*/

}
