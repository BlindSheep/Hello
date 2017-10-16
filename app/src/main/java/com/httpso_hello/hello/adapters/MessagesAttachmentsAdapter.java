package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        final Attachment attachment = this.attachments.get(position);
        if(attachment.previewAttachmentUri != null){
//            holder.imagePreview.setImageDrawable(((ImageView)attachment.previewAttachment).getDrawable());
            Picasso.with(this.activity.getApplicationContext())
                    .load(attachment.previewAttachmentUri)
                    .resize(0, Help.getPxFromDp(100, getContext()))
                    .into(holder.imagePreview, new Callback() {
                        @Override
                        public void onSuccess() {
//                            attachment.widthPreviewAttachment = holder.imagePreview.getWidth();
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

        if(attachment.isUploaded){
            holder.attachmentProgressLoad.setVisibility(View.GONE);
        } else {
            holder.attachmentProgressLoad.setVisibility(View.VISIBLE);
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
        this.notifyDataSetChanged();
        return position;
    }

    public void setLoadedAttachment(final int position, final int id){
        Attachment attachment = this.attachments.get(position);
        attachment.isUploaded = true;
        attachment.id = id;
        this.notifyDataSetChanged();
    }

    public Attachment getItem(final int position){
        return this.attachments.get(position);
    }

    public void deleteAttachment(final int position){
        this.attachments.remove(position);
        this.notifyDataSetChanged();
    }
    public void deleteAllAttachments(){
        this.attachments.clear();
    }

//    public int getWidthPreview(int position){
//        return this.attachments.get(position).widthPreviewAttachment;
//    }

}
