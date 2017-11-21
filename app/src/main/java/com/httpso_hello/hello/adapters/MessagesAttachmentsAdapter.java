package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Attachment;
import com.httpso_hello.hello.Structures.Contact;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Messages;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Общий on 08.10.2017.
 */

public class MessagesAttachmentsAdapter extends ArrayAdapter<Attachment> {

    private final Activity activity;
    private ArrayList<Attachment> attachments;
    private Messages parentClass;

    public MessagesAttachmentsAdapter(Activity activity, ArrayList<Attachment> attachments, Messages parentClass){
        super(activity, R.layout.content_chat, attachments);
        this.attachments = attachments;
        this.activity = activity;
        this.parentClass = parentClass;
    }

    static class ViewHolder{
        public ImageView imagePreview;
        public ProgressBar attachmentProgressLoad;
        public ImageView attachment_isLoad;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        final ViewHolder holder;
        View rowView = convertView;

        if (rowView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_chat_attachments_item, null, true);
            holder = new ViewHolder();
            holder.imagePreview = (ImageView) rowView.findViewById(R.id.chat_attachments_item_image_preview);
            holder.attachmentProgressLoad = (ProgressBar) rowView.findViewById(R.id.attachment_progress_load);
            holder.attachment_isLoad = (ImageView) rowView.findViewById(R.id.attachment_isLoad);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        final Attachment attachment = this.attachments.get(position);

        if(attachment.previewAttachmentUri != null){
            Picasso.with(this.activity.getApplicationContext())
                    .load(attachment.previewAttachmentUri)
                    .resize(Help.getPxFromDp(90, getContext()), Help.getPxFromDp(90, getContext()))
                    .centerCrop()
                    .into(holder.imagePreview, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        if(attachment.isUploaded){
            holder.attachmentProgressLoad.setVisibility(View.GONE);
            holder.attachment_isLoad.setVisibility(View.VISIBLE);
        } else {
            holder.attachmentProgressLoad.setVisibility(View.VISIBLE);
            holder.attachment_isLoad.setVisibility(View.GONE);
        }

        holder.imagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentClass.deleteAttachment(position);
                deleteAttachment(position);
            }
        });

        return rowView;

    }

    public int addAttachment(final Attachment attachment){
        int position = this.attachments.size();
        this.attachments.add(position, attachment);
        notifyDataSetChanged();
        return position;
    }

    public void setLoadedAttachment(final int position, final int id){
        try {
            this.attachments.get(position).isUploaded = true;
            this.attachments.get(position).id = id;
            notifyDataSetChanged();
        } catch (Exception e){

        }
    }

    public Attachment getItem(final int position){
        return this.attachments.get(position);
    }

    public void deleteAttachment(final int position){
        this.attachments.remove(position);
        notifyDataSetChanged();
    }
    public void deleteAllAttachments(){
        this.attachments.clear();
        notifyDataSetChanged();
    }

    public Attachment[] getAttachments(){
        Attachment[] defoltAttachment = new Attachment[this.attachments.size()];
        int i = 0;
        for (Attachment attachment : this.attachments) {
            defoltAttachment[i] = attachment;
            i++;
        };
        return defoltAttachment;
    }

    @Override
    public int getCount() {
        return attachments.size();
    }
}
