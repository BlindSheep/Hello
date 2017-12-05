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
import com.httpso_hello.hello.helper.Files;
import com.httpso_hello.hello.helper.Help;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Общий on 08.10.2017.
 */

public class FilesAdapter extends ArrayAdapter<Attachment> {

    private final Activity activity;
    private ArrayList<Attachment> files;

    public FilesAdapter(Activity activity, ArrayList<Attachment> files, int resource){
        super(activity, resource, files);
        this.files = files;
        this.activity = activity;
    }

    static class ViewHolder{
        public ImageView imagePreview;
        public ProgressBar fileProgressLoad;
        public ImageView attachment_delete;
        public ImageView attachment_isLoad;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        final ViewHolder holder;
        View rowView = convertView;

        if (rowView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_files_adapter_item, null, true);
            holder = new ViewHolder();
            holder.imagePreview = (ImageView) rowView.findViewById(R.id.files_adapter_item_image_preview);
            holder.fileProgressLoad = (ProgressBar) rowView.findViewById(R.id.files_adapter_item_progress_load);
            holder.attachment_delete = (ImageView) rowView.findViewById(R.id.files_adapter_item_delete);
            holder.attachment_isLoad = (ImageView) rowView.findViewById(R.id.attachment_isLoad);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        final Attachment file = this.files.get(position);
        if(file.previewAttachmentUri != null){
//            holder.imagePreview.setImageDrawable(((ImageView)attachment.previewAttachment).getDrawable());
            Picasso.with(this.activity.getApplicationContext())
                    .load(file.previewAttachmentUri)
                    .resize(Help.getPxFromDp(90, getContext()), Help.getPxFromDp(90, getContext()))
                    .centerCrop()
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

        if(file.isUploaded){
            holder.fileProgressLoad.setVisibility(View.GONE);
            holder.attachment_isLoad.setVisibility(View.VISIBLE);
        } else {
            holder.fileProgressLoad.setVisibility(View.VISIBLE);
            holder.attachment_isLoad.setVisibility(View.GONE);
        }

        holder.attachment_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(position);
            }
        });

        return rowView;

    }

    public void setRequestTag(int position, String tag){
        files.get(position).requestTag = "Files.uploadFile" + Integer.toString(position);
    }

    public int addAttachment(final Attachment attachment){
        int position = this.files.size();
        this.files.add(position, attachment);
        this.notifyDataSetChanged();
        return position;
    }

    public void setLoadedFile(final int position, final int id){
        Attachment attachment = null;
        try {
            attachment = this.files.get(position);
        } catch(Exception e){

        }
        if(attachment!=null) {
            attachment.isUploaded = true;
            attachment.id = id;
            this.notifyDataSetChanged();
        }
    }

    public Attachment getItem(final int position){
        return this.files.get(position);
    }

    public void deleteFile(final int position){
        Attachment file = this.files.get(position);
        if(file.isUploaded) {
            Files.getInstance(getContext(), activity).deleteFile(
                    file.id,
                    new Files.DeleteFileCallback() {
                        @Override
                        public void onSuccess() {
                            files.remove(position);
                            notifyDataSetChanged();
                        }
                    },
                    new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {

                        }

                        @Override
                        public void onInternetError() {

                        }
                    }

            );
        } else {
            Files.getInstance(getContext(), activity).breakUploadRequest(file.requestTag);
            files.remove(position);
            notifyDataSetChanged();
        }

    }
    public void deleteAllFiles(){
        this.files.clear();
    }

}
