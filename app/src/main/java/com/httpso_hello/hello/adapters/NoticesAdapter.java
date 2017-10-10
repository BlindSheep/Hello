package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.NoticeItem;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 24.08.2017.
 */

public class NoticesAdapter extends ArrayAdapter<NoticeItem> {

    private ArrayList<NoticeItem> noticeItem;
    private final Activity context;
    private Settings stgs;

    public NoticesAdapter(Activity context, ArrayList<NoticeItem> noticeItem){
        super(context, R.layout.content_notises, noticeItem);
        this.noticeItem = noticeItem;
        this.context = context;
        this.stgs = new Settings(getContext());
    }

    private class ViewHolder{
        public TextView noticeText;
        public TextView noticeDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_notises_item, null, true);
            holder = new ViewHolder();
            holder.noticeText = (TextView) rowView.findViewById(R.id.noticeText);
            holder.noticeDate = (TextView) rowView.findViewById(R.id.noticeDate);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        NoticeItem noticeItem = this.noticeItem.get(position);

        holder.noticeDate.setText(ConverterDate.convertDateForGuest(noticeItem.date_pub));
        holder.noticeText.setText(noticeItem.content);


        return rowView;

    }

}