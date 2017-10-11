package com.httpso_hello.hello.Structures;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Общий on 25.09.2017.
 */

public class Attachment {

    public int id;
    public String type;
    public Image image;
//    public Object previewAttachment;
    public String pathPreviewAttachment;
    public Uri previewAttachmentUri;
    public boolean isUploaded;

    public Attachment(){

    }
}
